package fr.funixgaming.api.twitch.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients(basePackages = {
        "com.funixproductions.api.twitch.reference.client",
        "com.funixproductions.api.user.client"
})
@SpringBootApplication(scanBasePackages = {
        "fr.funixgaming",
        "com.funixproductions"
})
public class FunixGamingTwitchApp {
    public static void main(String[] args) {
        SpringApplication.run(FunixGamingTwitchApp.class, args);
    }
}
