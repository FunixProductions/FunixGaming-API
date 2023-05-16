package fr.funixgaming.api.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@EnableFeignClients(basePackages = {"fr.funixgaming.api", "com.funixproductions"})
@SpringBootApplication(scanBasePackages = {"fr.funixgaming.api", "com.funixproductions"})
public class FunixGamingApiApp {

    public static void main(final String[] args) {
        SpringApplication.run(FunixGamingApiApp.class, args);
    }

}
