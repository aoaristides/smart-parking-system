package br.com.makersweb.smartparkingsystem.entry;

import br.com.makersweb.smartparkingsystem.event.VehicleEnteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author andersonorione
 */
@Service
@Slf4j
public class EntryService {

    private final ParkingEntryRepository repository;
    private final ApplicationEventPublisher publisher;

    public EntryService(final ParkingEntryRepository repository, final ApplicationEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional
    public void vehicleEntry(final String vehicleNumber) {
        var entry = new ParkingEntry(null, vehicleNumber, LocalDateTime.now(), null, true);
        repository.save(entry);
        publisher.publishEvent(new VehicleEnteredEvent(vehicleNumber, entry.getEntryTime()));
    }


}
