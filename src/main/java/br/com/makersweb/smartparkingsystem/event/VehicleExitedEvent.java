package br.com.makersweb.smartparkingsystem.event;

import java.time.LocalDateTime;

/**
 * @author andersonorione
 * @param vehicleNumber
 * @param entryTime
 * @param exitTime
 */
public record VehicleExitedEvent(
        String vehicleNumber,
        LocalDateTime entryTime,
        LocalDateTime exitTime
) {
}
