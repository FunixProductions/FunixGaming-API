package fr.funixgaming.api.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "fr.funixgaming.api")
@EnableFeignClients
public class TestApp {

    public static void main(String[] args) {
        SpringApplication.run(TestApp.class);
    }

}
