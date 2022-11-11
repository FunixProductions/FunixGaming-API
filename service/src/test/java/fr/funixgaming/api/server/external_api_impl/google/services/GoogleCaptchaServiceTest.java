package fr.funixgaming.api.server.external_api_impl.google.services;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.server.beans.JsonHelper;
import fr.funixgaming.api.server.beans.WiremockTestServer;
import fr.funixgaming.api.server.external_api_impl.google.config.GoogleCaptchaConfig;
import fr.funixgaming.api.server.external_api_impl.google.dtos.GoogleCaptchaSiteVerifyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Fail.fail;

@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(WiremockTestServer.class)
public class GoogleCaptchaServiceTest {

    @Autowired
    private GoogleCaptchaService googleCaptchaService;

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private GoogleCaptchaConfig googleCaptchaConfig;

    @Autowired
    private JsonHelper jsonHelper;

    @BeforeEach
    public void setup() {
        googleCaptchaConfig.setDisabled(false);
        googleCaptchaConfig.setThreshold(0.5f);
        googleCaptchaConfig.setSite("key_site");
        googleCaptchaConfig.setSecret("key_secret");
        wireMockServer.resetAll();
    }

    @Test
    public void testCheckCodeValid() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("google_reCaptcha", "codevalid");
        request.setRemoteAddr("127.0.0.2");
        request.setLocalAddr("127.0.0.1");

        final GoogleCaptchaSiteVerifyResponse responseMock = new GoogleCaptchaSiteVerifyResponse();
        responseMock.setSuccess(true);
        responseMock.setHostname("127.0.0.2");
        responseMock.setScore(0.8f);
        responseMock.setAction("register");

        wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo(String.format("/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s", googleCaptchaConfig.getSecret(), "codevalid", request.getRemoteAddr())))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonHelper.toJson(responseMock))));

        googleCaptchaService.checkCode(request);
    }

    @Test
    public void testCheckCodeValid2() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("google_reCaptcha", "codevalid");
        request.setRemoteAddr("127.0.0.2");
        request.setLocalAddr("127.0.0.1");

        final GoogleCaptchaSiteVerifyResponse responseMock = new GoogleCaptchaSiteVerifyResponse();
        responseMock.setSuccess(true);
        responseMock.setHostname("127.0.0.2");
        responseMock.setScore(0.8f);
        responseMock.setAction("login");

        wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo(String.format("/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s", googleCaptchaConfig.getSecret(), "codevalid", request.getRemoteAddr())))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonHelper.toJson(responseMock))));

        googleCaptchaService.checkCode(request);
    }

    @Test
    public void testCheckCodeInvalid() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("google_reCaptcha", "codevalid");
        request.setRemoteAddr("127.0.0.2");
        request.setLocalAddr("127.0.0.1");

        final GoogleCaptchaSiteVerifyResponse responseMock = new GoogleCaptchaSiteVerifyResponse();
        responseMock.setSuccess(false);
        responseMock.setHostname("127.0.0.2");
        responseMock.setScore(0.1f);
        responseMock.setAction("register");

        wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo(String.format("/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s", googleCaptchaConfig.getSecret(), "codevalid", request.getRemoteAddr())))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonHelper.toJson(responseMock))));

        try {
            googleCaptchaService.checkCode(request);
            fail("Should throw an exception");
        } catch (ApiBadRequestException ignored) {
        }
    }

    @Test
    public void testCheckCodeInvalidScore() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("google_reCaptcha", "codevalid");
        request.setRemoteAddr("127.0.0.2");
        request.setLocalAddr("127.0.0.1");

        final GoogleCaptchaSiteVerifyResponse responseMock = new GoogleCaptchaSiteVerifyResponse();
        responseMock.setSuccess(true);
        responseMock.setHostname("127.0.0.2");
        responseMock.setScore(0.1f);
        responseMock.setAction("register");

        wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo(String.format("/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s", googleCaptchaConfig.getSecret(), "codevalid", request.getRemoteAddr())))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonHelper.toJson(responseMock))));

        try {
            googleCaptchaService.checkCode(request);
            fail("Should throw an exception");
        } catch (ApiBadRequestException ignored) {
        }
    }

    @Test
    public void testCheckCodeNoCaptcha() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.2");
        request.setLocalAddr("127.0.0.1");

        try {
            googleCaptchaService.checkCode(request);
            fail("Should throw an exception");
        } catch (ApiBadRequestException ignored) {
        }
    }

}
