package br.com.makersweb.smartparkingsystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@SpringBootTest
class SmartParkingSystemApplicationTests {

    @Test
    void contextLoads() {
        ApplicationModules modules = ApplicationModules.of(SmartParkingSystemApplication.class)
                .verify();
        new Documenter(modules);
    }

}
