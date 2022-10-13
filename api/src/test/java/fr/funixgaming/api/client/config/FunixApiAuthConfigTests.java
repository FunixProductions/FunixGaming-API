package fr.funixgaming.api.client.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import feign.RequestTemplate;
import fr.funixgaming.api.client.TestApp;
import fr.funixgaming.api.client.beans.JsonHelper;
import fr.funixgaming.api.client.beans.WiremockTestServer;
import fr.funixgaming.api.client.user.clients.UserAuthClient;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.Date;

@SpringBootTest(classes = TestApp.class)
@ImportAutoConfiguration(WiremockTestServer.class)
@AutoConfigureMockMvc
public class FunixApiAuthConfigTests {

    @Autowired
    private FunixApiConfig funixApiConfig;

    @Autowired
    private UserAuthClient userAuthClient;

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private JsonHelper jsonHelper;

    @BeforeEach
    public void setup() {
        wireMockServer.resetAll();
    }

    @Test
    public void testAuthConfigValidToken() throws Exception {
        final UserTokenDTO userTokenDTO = new UserTokenDTO();
        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.ADMIN);
        userDTO.setUsername("funix");
        userDTO.setEmail("contact@funixgaming.fr");
        userTokenDTO.setUser(userDTO);
        userTokenDTO.setToken("token");
        userTokenDTO.setExpirationDate(Date.from(Instant.now().plusSeconds(1000)));

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/user/valid"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                )
        );

        wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/user/login"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonHelper.toJson(userTokenDTO))
                )
        );

        final FunixApiAuthConfig apiAuthConfig = new FunixApiAuthConfig(funixApiConfig, userAuthClient);
        apiAuthConfig.apply(new RequestTemplate());
        apiAuthConfig.apply(new RequestTemplate());
        apiAuthConfig.client();
    }

    @Test
    public void testAuthConfigExpiredDateToken() throws Exception {
        final UserTokenDTO userTokenDTO = new UserTokenDTO();
        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.ADMIN);
        userDTO.setUsername("funix");
        userDTO.setEmail("contact@funixgaming.fr");
        userTokenDTO.setUser(userDTO);
        userTokenDTO.setToken("token");
        userTokenDTO.setExpirationDate(Date.from(Instant.now().minusSeconds(1000)));

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/user/valid"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                )
        );

        wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/user/login"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonHelper.toJson(userTokenDTO))
                )
        );

        final FunixApiAuthConfig apiAuthConfig = new FunixApiAuthConfig(funixApiConfig, userAuthClient);
        apiAuthConfig.apply(new RequestTemplate());
    }

    @Test
    public void testAuthConfigInvalidTokenApi() throws Exception {
        final UserTokenDTO userTokenDTO = new UserTokenDTO();
        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.ADMIN);
        userDTO.setUsername("funix");
        userDTO.setEmail("contact@funixgaming.fr");
        userTokenDTO.setUser(userDTO);
        userTokenDTO.setToken("token");
        userTokenDTO.setExpirationDate(Date.from(Instant.now().plusSeconds(1000)));

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/user/valid"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                )
        );

        wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/user/login"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonHelper.toJson(userTokenDTO))
                )
        );

        final FunixApiAuthConfig apiAuthConfig = new FunixApiAuthConfig(funixApiConfig, userAuthClient);
        apiAuthConfig.apply(new RequestTemplate());
    }

}
