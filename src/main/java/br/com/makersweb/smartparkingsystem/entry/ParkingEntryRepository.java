package br.com.makersweb.smartparkingsystem.entry;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author andersonorione
 */
public interface ParkingEntryRepository extends JpaRepository<ParkingEntry, Long> {

    Optional<ParkingEntry> findByVehicleNumberAndActiveTrue(final String vehicleNumber);

}
