package fr.funixgaming.api.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "fr.funixgaming.api")
@EnableFeignClients
public class FunixApiApp {

    public static void main(final String[] args) {
        SpringApplication.run(FunixApiApp.class, args);
    }

}
