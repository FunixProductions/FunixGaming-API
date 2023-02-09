package fr.funixgaming.api.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync(proxyTargetClass = true)
@EnableScheduling
@EnableFeignClients(basePackages = "fr.funixgaming.api")
@SpringBootApplication(scanBasePackages = "fr.funixgaming.api")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class FunixApiApp {

    public static void main(final String[] args) {
        SpringApplication.run(FunixApiApp.class, args);
    }

}
