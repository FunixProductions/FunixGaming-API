package fr.funixgaming.api.server.external_api_impl.paypal.auth.services;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.server.external_api_impl.paypal.auth.clients.PaypalAuthClient;
import fr.funixgaming.api.server.external_api_impl.paypal.auth.dtos.PaypalTokenAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class PaypalAccessTokenServiceTest {

    @MockBean
    private PaypalAuthClient paypalAuthClient;

    @Autowired
    private PaypalAccessTokenService service;

    @BeforeEach
    void resetMocks() throws Exception {
        Mockito.reset(paypalAuthClient);

        final Field field = service.getClass().getDeclaredField("tokenAuth");
        field.setAccessible(true);
        field.set(service, null);
        field.setAccessible(false);
    }

    @Test
    void testGetAuthValid() throws InterruptedException {
        final PaypalTokenAuth tokenAuth = new PaypalTokenAuth();
        tokenAuth.setAccessToken(UUID.randomUUID().toString());
        tokenAuth.setAppId("appId");
        tokenAuth.setExpiresIn(1000);

        when(paypalAuthClient.getToken(anyString())).thenReturn(tokenAuth);
        service.refreshPaypalToken();
        Thread.sleep(1000);

        assertEquals(tokenAuth.getAccessToken(), service.getAccessToken());
    }

    @Test
    void testGetAuthExpiredToken() throws InterruptedException {
        final PaypalTokenAuth tokenAuth = new PaypalTokenAuth();
        tokenAuth.setAccessToken(UUID.randomUUID().toString());
        tokenAuth.setAppId("appId");
        tokenAuth.setExpiresIn(1);

        when(paypalAuthClient.getToken(anyString())).thenReturn(tokenAuth);
        service.refreshPaypalToken();
        Thread.sleep(2000);

        try {
            service.getAccessToken();
            fail("should throw here");
        } catch (ApiException ignored) {
        }
    }

    @Test
    void testGetAuthWithFeignException() {
        final Request request = Request.create(
                Request.HttpMethod.GET,
                "url",
                new HashMap<>(),
                "test".getBytes(),
                StandardCharsets.UTF_8,
                new RequestTemplate()
        );

        final FeignException exception = new FeignException.InternalServerError(
                "Mock error",
                request,
                "Mock error body".getBytes(),
                new HashMap<>()
        );

        when(paypalAuthClient.getToken(anyString())).thenThrow(exception);
        service.refreshPaypalToken();

        try {
            service.getAccessToken();
            fail("should fail here");
        } catch (ApiException ignored) {
        }
    }

}
