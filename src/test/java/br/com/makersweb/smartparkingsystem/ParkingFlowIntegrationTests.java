package br.com.makersweb.smartparkingsystem;

import br.com.makersweb.smartparkingsystem.allocation.SlotRepository;
import br.com.makersweb.smartparkingsystem.billing.BillingRecordRepository;
import br.com.makersweb.smartparkingsystem.entry.EntryService;
import br.com.makersweb.smartparkingsystem.entry.ExitService;
import br.com.makersweb.smartparkingsystem.entry.ParkingEntryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Fluxo completo entre módulos, com todos os listeners reais e assíncronos.
 * Cobre o que os testes isolados não cobrem: a cadeia entry -> allocation/billing.
 */
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ParkingFlowIntegrationTests {

    private static final String PLATE = "TST-0001";

    private final EntryService entryService;
    private final ExitService exitService;
    private final SlotRepository slotRepository;
    private final BillingRecordRepository billingRepository;
    private final ParkingEntryRepository entryRepository;

    ParkingFlowIntegrationTests(final EntryService entryService,
                                final ExitService exitService,
                                final SlotRepository slotRepository,
                                final BillingRecordRepository billingRepository,
                                final ParkingEntryRepository entryRepository) {
        this.entryService = entryService;
        this.exitService = exitService;
        this.slotRepository = slotRepository;
        this.billingRepository = billingRepository;
        this.entryRepository = entryRepository;
    }

    @AfterEach
    void cleanUp() {
        billingRepository.deleteAll();
        entryRepository.deleteAll();
    }

    @Test
    void entryAllocatesSlotAndExitFreesItAndBills() {
        entryService.vehicleEntry(PLATE);

        await().untilAsserted(() -> assertThat(slotRepository.findByVehicleNumber(PLATE))
                .hasValueSatisfying(slot -> assertThat(slot.isAvailable()).isFalse()));

        exitService.vehicleExit(PLATE);

        await().untilAsserted(() -> {
            assertThat(slotRepository.findByVehicleNumber(PLATE)).isEmpty();
            assertThat(billingRepository.findAll())
                    .anySatisfy(record -> assertThat(record.getVehicleNumber()).isEqualTo(PLATE));
        });
    }

}
