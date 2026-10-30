package br.com.makersweb.smartparkingsystem.allocation;

import br.com.makersweb.smartparkingsystem.event.VehicleEnteredEvent;
import br.com.makersweb.smartparkingsystem.event.VehicleExitedEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testa o módulo {@code allocation} reagindo aos eventos, sem subir entry.
 * O listener é assíncrono, então a verificação espera a mudança de estado.
 */
@ApplicationModuleTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class AllocationModuleTests {

    private static final String PLATE = "TST-0001";
    private static final String SLOT_CODE = "T1";

    private final SlotRepository repository;

    AllocationModuleTests(final SlotRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setUp() {
        // Remove o seed do SlotInitializer: cada teste controla as vagas que existem.
        repository.deleteAll();
    }

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void vehicleEnteredAllocatesAvailableSlot(final Scenario scenario) {
        repository.save(new Slot(null, SLOT_CODE, true, null));

        scenario.publish(new VehicleEnteredEvent(PLATE, LocalDateTime.now()))
                .andWaitForStateChange(() -> repository.findByVehicleNumber(PLATE), Optional::isPresent)
                .andVerify(slot -> assertThat(slot).hasValueSatisfying(it -> {
                    assertThat(it.getSlotCode()).isEqualTo(SLOT_CODE);
                    assertThat(it.isAvailable()).isFalse();
                }));
    }

    @Test
    void vehicleExitedFreesOccupiedSlot(final Scenario scenario) {
        var occupied = repository.save(new Slot(null, SLOT_CODE, false, PLATE));
        var exitTime = LocalDateTime.now();

        scenario.publish(new VehicleExitedEvent(PLATE, exitTime.minusHours(1), exitTime))
                .andWaitForStateChange(() -> repository.findById(occupied.getId()),
                        slot -> slot.map(Slot::isAvailable).orElse(false))
                .andVerify(slot -> assertThat(slot).hasValueSatisfying(it ->
                        assertThat(it.getVehicleNumber()).isNull()));
    }

}
