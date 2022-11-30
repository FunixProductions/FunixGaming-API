package fr.funixgaming.api.server.external_api_impl.twitch.auth.resources;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import fr.funixgaming.api.client.external_api_impl.twitch.auth.enums.TwitchClientTokenType;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.core.utils.string.PasswordGenerator;
import fr.funixgaming.api.server.beans.JsonHelper;
import fr.funixgaming.api.server.beans.WiremockTestServer;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.dtos.TwitchTokenResponseDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.dtos.TwitchValidationTokenResponseDTO;
import fr.funixgaming.api.server.user.components.UserTestComponent;
import fr.funixgaming.api.server.user.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(classes = WiremockTestServer.class)
class TwitchAuthResourceTest {

    @Autowired
    private UserTestComponent userTestComponent;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private JsonHelper jsonHelper;

    @BeforeEach
    void beforeEach() {
        this.wireMockServer.resetAll();
    }

    @Test
    void testGetAuthUrlSuccess() throws Exception {
        final User user = userTestComponent.createBasicUser();
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(user);

        mockMvc.perform(get("/twitch/auth/clientAuthUrl"))
                .andExpect(status().isUnauthorized());

        MvcResult result = mockMvc.perform(get("/twitch/auth/clientAuthUrl")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                ).andExpect(status().isOk())
                .andReturn();
        String res = result.getResponse().getContentAsString();

        result = mockMvc.perform(get("/twitch/auth/clientAuthUrl?tokenType=" + TwitchClientTokenType.STREAMER.name())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                ).andExpect(status().isOk())
                .andReturn();
        String res2 = result.getResponse().getContentAsString();

        assertTrue(res2.length() > res.length());
        assertTrue(res.contains("client_id"));
        assertTrue(res.contains("redirect_uri"));
        assertTrue(res.contains("response_type"));
        assertTrue(res.contains("scope"));
        assertTrue(res.contains("state"));
    }

    @Test
    void testTwitchCallbackRoute() throws Exception {
        final User user = userTestComponent.createBasicUser();
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(user);

        MvcResult result = mockMvc.perform(get("/twitch/auth/clientAuthUrl")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                ).andExpect(status().isOk())
                .andReturn();
        final String url = result.getResponse().getContentAsString();
        final String csrfCode = getCsrfCodeFromUrl(url);

        setupTwitchMockServerValidResponses();

        result = mockMvc.perform(get("/twitch/auth/cb" +
                        String.format("?code=sdfsdfsdfsdfsdqfthfyh&state=%s", csrfCode))
                ).andExpect(status().isOk())
                .andReturn();
        String responseCallback = result.getResponse().getContentAsString();
        assertTrue(responseCallback.contains("connecté"));

        mockMvc.perform(get("/twitch/auth/cb")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/twitch/auth/cb?code=dfggopaizepoaznqsdlb&state=codeAuPifNonValide")).andExpect(status().isBadRequest());

        result = mockMvc.perform(get("/twitch/auth/cb?error=error_custom&error_description=Une erreur custom faite exprès."))
                .andExpect(status().isOk())
                .andReturn();
        responseCallback = result.getResponse().getContentAsString();
        assertTrue(responseCallback.contains("erreur"));
    }

    @Test
    void testGetAccessTokenSuccess() throws Exception {
        final User user = userTestComponent.createBasicUser();
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(user);

        setupTwitchMockServerValidResponses();

        mockMvc.perform(get("/twitch/auth/accessToken"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/twitch/auth/accessToken")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                ).andExpect(status().isNotFound());

        MvcResult result = mockMvc.perform(get("/twitch/auth/clientAuthUrl")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                ).andExpect(status().isOk())
                .andReturn();
        String url = result.getResponse().getContentAsString();
        String csrfCode = getCsrfCodeFromUrl(url);
        mockMvc.perform(get("/twitch/auth/cb" +
                String.format("?code=ioqapeiuycxnbveryjtgmlykhpodf&state=%s", csrfCode))
        ).andExpect(status().isOk());
        mockMvc.perform(get("/twitch/auth/accessToken")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
        ).andExpect(status().isOk());
    }

    @Test
    void testGetAccessTokenStreamerSuccess() throws Exception {
        final User user = userTestComponent.createBasicUser();
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(user);

        setupTwitchMockServerValidResponses();

        mockMvc.perform(get("/twitch/auth/accessToken?tokenType=" + TwitchClientTokenType.STREAMER.name())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
        ).andExpect(status().isNotFound());
        MvcResult result = mockMvc.perform(get("/twitch/auth/clientAuthUrl?tokenType=" + TwitchClientTokenType.STREAMER.name())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                ).andExpect(status().isOk())
                .andReturn();
        String url = result.getResponse().getContentAsString();
        String csrfCode = getCsrfCodeFromUrl(url);
        mockMvc.perform(get("/twitch/auth/cb" +
                String.format("?code=piiuuiezuoioueizryuierzaovvjkdshhfjqdshgjkfdiu&state=%s", csrfCode))
        ).andExpect(status().isOk());
        mockMvc.perform(get("/twitch/auth/accessToken?tokenType=" + TwitchClientTokenType.STREAMER.name())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
        ).andExpect(status().isOk());
    }

    private String getCsrfCodeFromUrl(final String url) {
        final String toScan = "&state=";
        return url.substring(url.lastIndexOf(toScan) + toScan.length());
    }

    private void setupTwitchMockServerValidResponses() throws Exception {
        final PasswordGenerator passwordGenerator = new PasswordGenerator();
        passwordGenerator.setSpecialCharsAmount(0);

        final TwitchTokenResponseDTO mockToken = new TwitchTokenResponseDTO();
        mockToken.setAccessToken("access" + passwordGenerator.generateRandomPassword());
        mockToken.setRefreshToken("refersh" + passwordGenerator.generateRandomPassword());
        mockToken.setTokenType("bearer");
        mockToken.setExpiresIn(3000);

        final TwitchValidationTokenResponseDTO validResponseMock = new TwitchValidationTokenResponseDTO();
        validResponseMock.setTwitchUserId("lqksjdsldgh");
        validResponseMock.setTwitchUsername("funix");

        wireMockServer.stubFor(WireMock.any(WireMock.urlPathEqualTo("/oauth2/token"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonHelper.toJson(mockToken))
                )
        );

        wireMockServer.stubFor(WireMock.any(WireMock.urlPathEqualTo("/oauth2/validate"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonHelper.toJson(validResponseMock))
                )
        );
    }
}