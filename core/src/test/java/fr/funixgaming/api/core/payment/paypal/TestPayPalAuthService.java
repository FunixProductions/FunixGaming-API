package fr.funixgaming.api.core.payment.paypal;

import fr.funixgaming.api.core.TestApp;
import fr.funixgaming.api.core.payment.paypal.dtos.PayPalToken;
import fr.funixgaming.api.core.payment.paypal.services.PayPalAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TestApp.class)
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
