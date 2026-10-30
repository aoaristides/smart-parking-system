package br.com.makersweb.smartparkingsystem.billing;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author andersonorione
 */
public interface BillingRecordRepository extends JpaRepository<BillingRecord, Long> {
}
