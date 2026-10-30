package br.com.makersweb.smartparkingsystem.notification;

import br.com.makersweb.smartparkingsystem.event.VehicleEnteredEvent;
import br.com.makersweb.smartparkingsystem.event.VehicleExitedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @author andersonorione
 */
@Service
@Slf4j
public class NotificationService {

    @EventListener
    public void notifyOnVehicleEntry(final VehicleEnteredEvent event) {
        log.info("Notification: Vehicle {} entered at {}. Welcome!", event.vehicleNumber(), event.entryTime());
    }

    @EventListener
    public void notifyOnVehicleExit(final VehicleExitedEvent event) {
        log.info("Notification: Vehicle {} exited at {}. Bye!", event.vehicleNumber(), event.exitTime());
    }

}
