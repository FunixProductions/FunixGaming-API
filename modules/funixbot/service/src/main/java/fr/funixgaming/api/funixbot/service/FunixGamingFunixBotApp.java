package fr.funixgaming.api.funixbot.service;

import com.funixproductions.api.user.client.clients.UserAuthClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = UserAuthClient.class)
@SpringBootApplication(scanBasePackages = {
        "com.funixproductions",
        "fr.funixgaming"
})
public class FunixGamingFunixBotApp {
    public static void main(String[] args) {
        SpringApplication.run(FunixGamingFunixBotApp.class, args);
    }
}