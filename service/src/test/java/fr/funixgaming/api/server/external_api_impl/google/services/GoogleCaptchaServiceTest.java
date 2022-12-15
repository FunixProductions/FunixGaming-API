package fr.funixgaming.api.server.external_api_impl.google.services;

import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.utils.network.IPUtils;
import fr.funixgaming.api.server.external_api_impl.google.clients.GoogleCaptchaClient;
import fr.funixgaming.api.server.external_api_impl.google.config.GoogleCaptchaConfig;
import fr.funixgaming.api.server.external_api_impl.google.dtos.GoogleCaptchaSiteVerifyResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Fail.fail;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class GoogleCaptchaServiceTest {

    @Mock
    private GoogleCaptchaClient googleCaptchaClient;

    private GoogleCaptchaService googleCaptchaService;

    @Autowired
    private GoogleCaptchaConfig googleCaptchaConfig;

    @Autowired
    private IPUtils ipUtils;

    @BeforeEach
    void setup() {
        googleCaptchaService = new GoogleCaptchaService(googleCaptchaConfig, googleCaptchaClient, ipUtils);
        googleCaptchaConfig.setDisabled(false);
        googleCaptchaConfig.setThreshold(0.5f);
        googleCaptchaConfig.setSite("key_site");
        googleCaptchaConfig.setSecret("key_secret");
    }

    @AfterEach
    void end() {
        googleCaptchaConfig.setDisabled(true);
    }

    @Test
    void testCheckCodeValid() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("google_reCaptcha", "codevalid");
        request.setRemoteAddr("127.0.0.2");
        request.setLocalAddr("127.0.0.1");

        final GoogleCaptchaSiteVerifyResponse responseMock = new GoogleCaptchaSiteVerifyResponse();
        responseMock.setSuccess(true);
        responseMock.setHostname("127.0.0.2");
        responseMock.setScore(0.8f);
        responseMock.setAction("register");

        Mockito.when(googleCaptchaClient.verify(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseMock);
        googleCaptchaService.checkCode(request);
    }

    @Test
    void testCheckCodeValid2() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("google_reCaptcha", "codevalid");
        request.setRemoteAddr("127.0.0.2");
        request.setLocalAddr("127.0.0.1");

        final GoogleCaptchaSiteVerifyResponse responseMock = new GoogleCaptchaSiteVerifyResponse();
        responseMock.setSuccess(true);
        responseMock.setHostname("127.0.0.2");
        responseMock.setScore(0.8f);
        responseMock.setAction("login");

        Mockito.when(googleCaptchaClient.verify(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseMock);
        googleCaptchaService.checkCode(request);
    }

    @Test
    void testCheckCodeInvalid() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("google_reCaptcha", "codevalid");
        request.setRemoteAddr("127.0.0.2");
        request.setLocalAddr("127.0.0.1");

        final GoogleCaptchaSiteVerifyResponse responseMock = new GoogleCaptchaSiteVerifyResponse();
        responseMock.setSuccess(false);
        responseMock.setHostname("127.0.0.2");
        responseMock.setScore(0.1f);
        responseMock.setAction("register");

        Mockito.when(googleCaptchaClient.verify(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseMock);

        try {
            googleCaptchaService.checkCode(request);
            fail("Should throw an exception");
        } catch (ApiBadRequestException ignored) {
        }
    }

    @Test
    void testCheckCodeInvalidScore() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("google_reCaptcha", "codevalid");
        request.setRemoteAddr("127.0.0.2");
        request.setLocalAddr("127.0.0.1");

        final GoogleCaptchaSiteVerifyResponse responseMock = new GoogleCaptchaSiteVerifyResponse();
        responseMock.setSuccess(true);
        responseMock.setHostname("127.0.0.2");
        responseMock.setScore(0.1f);
        responseMock.setAction("register");

        Mockito.when(googleCaptchaClient.verify(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseMock);

        try {
            googleCaptchaService.checkCode(request);
            fail("Should throw an exception");
        } catch (ApiBadRequestException ignored) {
        }
    }

    @Test
    void testCheckCodeNoCaptcha() {
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
