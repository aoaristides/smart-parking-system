package br.com.makersweb.smartparkingsystem.entry;

import br.com.makersweb.smartparkingsystem.event.VehicleExitedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author andersonorione
 */
@Service
@Slf4j
public class ExitService {

    private final ParkingEntryRepository repository;
    private final ApplicationEventPublisher publisher;

    public ExitService(final ParkingEntryRepository repository, final ApplicationEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    public void vehicleExit(final String vehicleNumber) {
        var entry = repository.findByVehicleNumberAndActiveTrue(vehicleNumber)
                .orElseThrow(() -> new RuntimeException("No active entry found for vehicle " + vehicleNumber));
        entry.setExitTime(LocalDateTime.now());
        entry.setActive(false);
        repository.save(entry);
        publisher.publishEvent(new VehicleExitedEvent(vehicleNumber, entry.getEntryTime(), entry.getExitTime()));
    }

}
