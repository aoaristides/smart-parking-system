package br.com.makersweb.smartparkingsystem.allocation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Semeia as vagas iniciais. Vive no módulo {@code allocation} para que a classe
 * principal não dependa dele (e testes de outros módulos subam isolados).
 *
 * @author andersonorione
 */
@Component
class SlotInitializer implements CommandLineRunner {

    private final SlotRepository repository;

    SlotInitializer(final SlotRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(final String... args) {
        if (repository.count() == 0) {
            repository.save(new Slot(null, "A1", true, null));
            repository.save(new Slot(null, "A2", true, null));
            repository.save(new Slot(null, "B1", true, null));
        }
    }

}
