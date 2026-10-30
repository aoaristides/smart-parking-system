package br.com.makersweb.smartparkingsystem.allocation;

import br.com.makersweb.smartparkingsystem.event.VehicleEnteredEvent;
import br.com.makersweb.smartparkingsystem.event.VehicleExitedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

/**
 * @author andersonorione
 */
@Service
@Slf4j
public class SlotAllocationService {

    private final SlotRepository slotRepository;

    public SlotAllocationService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @ApplicationModuleListener
    public void handleVehicleEntry(final VehicleEnteredEvent event) {
        var slot = slotRepository.findFirstByAvailableTrue()
                .orElseThrow(() -> new RuntimeException("No available slots"));

        slot.setAvailable(false);
        slot.setVehicleNumber(event.vehicleNumber());
        slotRepository.save(slot);

        log.info("Allocated Slot {} to vehicle {}", slot.getSlotCode(), event.vehicleNumber());
    }

    @ApplicationModuleListener
    public void handleVehicleExit(final VehicleExitedEvent event) {
        slotRepository.findByVehicleNumber(event.vehicleNumber())
                .ifPresentOrElse(slot -> {
                    slot.setAvailable(true);
                    slot.setVehicleNumber(null);
                    slotRepository.save(slot);

                    log.info("Freed Slot {} from vehicle {}", slot.getSlotCode(), event.vehicleNumber());
                }, () -> {
                    throw new RuntimeException("No slot found for vehicle " + event.vehicleNumber());
                });
    }

}
