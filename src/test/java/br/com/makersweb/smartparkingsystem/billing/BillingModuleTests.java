package br.com.makersweb.smartparkingsystem.billing;

import br.com.makersweb.smartparkingsystem.event.VehicleExitedEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Testa a regra de cobrança do módulo {@code billing}: proporcional ao tempo
 * (50 por hora) com mínimo de 20, conforme {@link BillingService}.
 */
@ApplicationModuleTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BillingModuleTests {

    private static final String PLATE = "TST-0001";
    private static final LocalDateTime EXIT_TIME = LocalDateTime.of(2026, 1, 1, 12, 0);

    private final BillingRecordRepository repository;

    BillingModuleTests(final BillingRecordRepository repository) {
        this.repository = repository;
    }

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void chargesProportionallyToStayDuration(final Scenario scenario) {
        scenario.publish(new VehicleExitedEvent(PLATE, EXIT_TIME.minusHours(2), EXIT_TIME))
                .andWaitForStateChange(repository::findAll, records -> !records.isEmpty())
                .andVerify(records -> assertThat(records).singleElement().satisfies(record -> {
                    assertThat(record.getVehicleNumber()).isEqualTo(PLATE);
                    assertThat(record.getAmount()).isCloseTo(100.0, within(0.001));
                }));
    }

    @Test
    void chargesMinimumFeeForShortStay(final Scenario scenario) {
        scenario.publish(new VehicleExitedEvent(PLATE, EXIT_TIME.minusMinutes(6), EXIT_TIME))
                .andWaitForStateChange(repository::findAll, records -> !records.isEmpty())
                .andVerify(records -> assertThat(records).singleElement().satisfies(record ->
                        assertThat(record.getAmount()).isCloseTo(20.0, within(0.001))));
    }

}
