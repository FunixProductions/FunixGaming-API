package fr.funixgaming.api.server.payment.paypal;

import fr.funixgaming.api.client.payment.paypal.dtos.PayPalToken;
import fr.funixgaming.api.server.payment.paypal.services.PayPalAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class TestPayPalAuthService {

    private final PayPalAuthService authService;

    @Autowired
    public TestPayPalAuthService(PayPalAuthService authService) {
        this.authService = authService;
    }

    @Test
    public void testGetToken() {
        final PayPalToken token = authService.getToken();

        assertNotNull(token.getAccessToken());
        assertNotNull(token.getTokenType());
        assertNotNull(token.getAppId());
        assertNotNull(token.getNonce());
        assertNotNull(token.getExpiresIn());
        assertNotNull(token.getGeneratedAt());
        assertNotNull(token.getScope());
    }

}
