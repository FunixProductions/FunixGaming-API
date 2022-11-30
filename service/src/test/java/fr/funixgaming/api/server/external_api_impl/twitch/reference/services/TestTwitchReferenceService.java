package fr.funixgaming.api.server.external_api_impl.twitch.reference.services;

import com.github.tomakehurst.wiremock.WireMockServer;
import fr.funixgaming.api.server.beans.JsonHelper;
import fr.funixgaming.api.server.beans.WiremockTestServer;
import fr.funixgaming.api.server.external_api_impl.twitch.configs.TwitchApiConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(classes = WiremockTestServer.class)
class TestTwitchReferenceService {

    private final String accessToken = "fakeAccessToken";

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private TwitchApiConfig twitchApiConfig;

    @BeforeEach
    void beforeEach() {
        this.wireMockServer.resetAll();
    }


}
