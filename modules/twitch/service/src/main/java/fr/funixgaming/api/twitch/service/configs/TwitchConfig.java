package fr.funixgaming.api.twitch.service.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("funixgaming.twitch")
public class TwitchConfig {
    private String streamerUsername = "funixgaming";
}
