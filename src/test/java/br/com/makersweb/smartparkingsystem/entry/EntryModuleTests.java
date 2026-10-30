package br.com.makersweb.smartparkingsystem.entry;

import br.com.makersweb.smartparkingsystem.event.VehicleEnteredEvent;
import br.com.makersweb.smartparkingsystem.event.VehicleExitedEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testa o módulo {@code entry} isolado: só ele sobe, então o que se verifica é o
 * evento que ele publica, sem depender de allocation/billing/notification.
 */
@ApplicationModuleTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class EntryModuleTests {

    private static final String PLATE = "TST-0001";

    private final EntryService entryService;
    private final ExitService exitService;
    private final ParkingEntryRepository repository;

    EntryModuleTests(final EntryService entryService,
                     final ExitService exitService,
                     final ParkingEntryRepository repository) {
        this.entryService = entryService;
        this.exitService = exitService;
        this.repository = repository;
    }

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void vehicleEntryPersistsActiveEntryAndPublishesEvent(final Scenario scenario) {
        scenario.stimulate(() -> entryService.vehicleEntry(PLATE))
                .andWaitForEventOfType(VehicleEnteredEvent.class)
                .matching(event -> PLATE.equals(event.vehicleNumber()))
                .toArriveAndVerify(event -> {
                    assertThat(event.entryTime()).isNotNull();
                    assertThat(repository.findByVehicleNumberAndActiveTrue(PLATE))
                            .hasValueSatisfying(entry -> assertThat(entry.getExitTime()).isNull());
                });
    }

    @Test
    void vehicleExitClosesEntryAndPublishesEvent(final Scenario scenario) {
        var entryTime = LocalDateTime.now().minusHours(1);
        repository.save(new ParkingEntry(null, PLATE, entryTime, null, true));

        scenario.stimulate(() -> exitService.vehicleExit(PLATE))
                .andWaitForEventOfType(VehicleExitedEvent.class)
                .matching(event -> PLATE.equals(event.vehicleNumber()))
                .toArriveAndVerify(event -> {
                    assertThat(event.exitTime()).isAfter(event.entryTime());
                    assertThat(repository.findByVehicleNumberAndActiveTrue(PLATE)).isEmpty();
                    assertThat(repository.findAll())
                            .singleElement()
                            .satisfies(entry -> assertThat(entry.getExitTime()).isNotNull());
                });
    }

    @Test
    void vehicleExitWithoutActiveEntryFails() {
        assertThatThrownBy(() -> exitService.vehicleExit(PLATE))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(PLATE);
    }

}
