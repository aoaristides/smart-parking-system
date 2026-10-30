package br.com.makersweb.smartparkingsystem.billing;

import br.com.makersweb.smartparkingsystem.event.VehicleExitedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author andersonorione
 */
@Service
@Slf4j
public class BillingService {

    private final BillingRecordRepository billingRecordRepository;

    public BillingService(BillingRecordRepository billingRecordRepository) {
        this.billingRecordRepository = billingRecordRepository;
    }

    @ApplicationModuleListener
    public void handleVehicleExit(final VehicleExitedEvent event) {
        Duration duration = Duration.between(event.entryTime(), event.exitTime());
        double amount = Math.max(20, (duration.toMinutes() / 60.0) * 50);
        BillingRecord record = new BillingRecord(null, event.vehicleNumber(), amount, event.exitTime());
        billingRecordRepository.save(record);

        log.info("Billed {} for vehicle {} from {} to {}", amount, event.vehicleNumber(), event.entryTime(), event.exitTime());
    }

}
