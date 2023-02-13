package fr.funixgaming.api.server.external_api_impl.paypal.orders.services;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.server.external_api_impl.paypal.auth.services.PaypalAccessTokenService;
import fr.funixgaming.api.server.external_api_impl.paypal.orders.clients.PaypalOrderClient;
import fr.funixgaming.api.server.external_api_impl.paypal.orders.dtos.requests.PaypalOrderCreationDTO;
import fr.funixgaming.api.server.external_api_impl.paypal.orders.dtos.responses.PaypalOrderResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class PaypalOrderServiceTest {

    @MockBean
    private PaypalOrderClient orderClient;

    @MockBean
    private PaypalAccessTokenService tokenService;

    @Autowired
    private PaypalOrderService service;

    @BeforeEach
    void setupMocks() {
        reset(orderClient);
        when(tokenService.getAccessToken()).thenReturn("token");
        doNothing().when(tokenService).refreshPaypalToken();
    }

    @Test
    void testValidRoutes() throws ApiException {
        final PaypalOrderResponseDTO responseDTO = new PaypalOrderResponseDTO();

        when(orderClient.createOrder(anyString(), anyString(), any(PaypalOrderCreationDTO.class))).thenReturn(responseDTO);
        when(orderClient.getOrder(anyString())).thenReturn(responseDTO);
        when(orderClient.captureOrder(anyString(), anyString(), anyString(), anyString())).thenReturn(responseDTO);
        when(orderClient.authorizeOrder(anyString(), anyString(), anyString(), anyString())).thenReturn(responseDTO);

        service.createOrder("meta", "id", new PaypalOrderCreationDTO());
        service.getOrder("orderId");
        service.authorizeOrder("authSession", "metaId", "requestId", "orderId");
        service.captureOrder("authSession", "metaId", "requestid", "orderId");
    }

    @Test
    void testInvalidRoutes() {
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

        doThrow(exception).when(orderClient).createOrder(anyString(), anyString(), any(PaypalOrderCreationDTO.class));
        doThrow(exception).when(orderClient).getOrder(anyString());
        doThrow(exception).when(orderClient).captureOrder(anyString(), anyString(), anyString(), anyString());
        doThrow(exception).when(orderClient).authorizeOrder(anyString(), anyString(), anyString(), anyString());

        try {
            service.createOrder("meta", "id", new PaypalOrderCreationDTO());
            fail("should fail here");
        } catch (ApiException ignored) {
        }

        try {
            service.getOrder("orderId");
            fail("should fail here");
        } catch (ApiException ignored) {
        }

        try {
            service.authorizeOrder("authSession", "metaId", "requestId", "orderId");
            fail("should fail here");
        } catch (ApiException ignored) {
        }

        try {
            service.captureOrder("authSession", "metaId", "requestid", "orderId");
            fail("should fail here");
        } catch (ApiException ignored) {
        }
    }

}
