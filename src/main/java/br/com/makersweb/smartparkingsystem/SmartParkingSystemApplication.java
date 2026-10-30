package br.com.makersweb.smartparkingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SmartParkingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartParkingSystemApplication.class, args);
    }

}
