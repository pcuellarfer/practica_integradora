package org.pcuellar.administracionapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class AdministracionAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdministracionAppApplication.class, args);
    }

}
