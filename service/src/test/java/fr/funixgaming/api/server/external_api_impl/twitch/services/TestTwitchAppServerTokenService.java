package fr.funixgaming.api.server.external_api_impl.twitch.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class TestTwitchAppServerTokenService {

    @Autowired
    private TwitchAppServerTokenService twitchAppServerTokenService;

    @Test
    public void testFetchingToken() {
        assertNotNull(twitchAppServerTokenService.getAccessToken());
        assertNotNull(twitchAppServerTokenService.getExpiresAt());
    }
}
