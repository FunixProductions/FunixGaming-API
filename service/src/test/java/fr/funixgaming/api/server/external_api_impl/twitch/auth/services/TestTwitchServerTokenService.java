package fr.funixgaming.api.server.external_api_impl.twitch.auth.services;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import fr.funixgaming.api.server.beans.JsonHelper;
import fr.funixgaming.api.server.beans.WiremockTestServer;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.dtos.TwitchTokenResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(classes = WiremockTestServer.class)
class TestTwitchServerTokenService {

    @Autowired
    private TwitchServerTokenService twitchServerTokenService;

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private JsonHelper jsonHelper;

    @Test
    protected void testFetchingToken() throws Exception {
        final TwitchTokenResponseDTO mockToken = new TwitchTokenResponseDTO();
        mockToken.setAccessToken("access");
        mockToken.setRefreshToken("refersh");
        mockToken.setTokenType("bearer");
        mockToken.setExpiresIn(3000);

        wireMockServer.stubFor(WireMock.post(WireMock.urlPathEqualTo("/oauth2/token"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonHelper.toJson(mockToken))
                )
        );

        twitchServerTokenService.refreshToken();

        assertNotNull(twitchServerTokenService.getAccessToken());
        assertNotNull(twitchServerTokenService.getExpiresAt());
    }
}
