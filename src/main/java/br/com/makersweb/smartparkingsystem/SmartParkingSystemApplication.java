package br.com.makersweb.smartparkingsystem;

import br.com.makersweb.smartparkingsystem.allocation.Slot;
import br.com.makersweb.smartparkingsystem.allocation.SlotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SmartParkingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartParkingSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner initSlots(final SlotRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Slot(null, "A1", true, null));
                repository.save(new Slot(null, "A2", true, null));
                repository.save(new Slot(null, "B1", true, null));
            }
        };
    }

}
