package fr.funixgaming.api.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication(scanBasePackages = "fr.funixgaming.api")
@EnableFeignClients(basePackages = "fr.funixgaming.api")
@EnableSpringDataWebSupport
public class TestApp {

    public static void main(String[] args) {
        SpringApplication.run(TestApp.class);
    }

}
