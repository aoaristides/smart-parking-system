package br.com.makersweb.smartparkingsystem.allocation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author andersonorione
 */
public interface SlotRepository extends JpaRepository<Slot, Long> {

    Optional<Slot> findFirstByAvailableTrue();

    Optional<Slot> findByVehicleNumber(final String vehicleNumber);

}
