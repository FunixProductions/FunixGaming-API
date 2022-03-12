package fr.funixgaming.api.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "fr.funixgaming.api")
public class FunixApiApp {

    public static void main(final String[] args) {
        SpringApplication.run(FunixApiApp.class, args);
    }

}
