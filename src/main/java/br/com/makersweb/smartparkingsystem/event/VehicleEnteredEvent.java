package br.com.makersweb.smartparkingsystem.event;

import java.time.LocalDateTime;

/**
 * @author andersonorione
 * @param vehicleNumber
 * @param entryTime
 */
public record VehicleEnteredEvent(
        String vehicleNumber,
        LocalDateTime entryTime
) {
}
