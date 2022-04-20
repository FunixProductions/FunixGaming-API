package fr.funixgaming.api.core.payment.paypal;

import fr.funixgaming.api.core.TestApp;
import fr.funixgaming.api.core.payment.paypal.configs.PayPalConfig;
import fr.funixgaming.api.core.payment.paypal.dtos.PayPalToken;
import fr.funixgaming.api.core.payment.paypal.services.PayPalAuthService;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TestApp.class)
@AutoConfigureMockMvc
public class TestPayPalAuthService {

    private final PayPalConfig payPalConfig;
    private final PayPalAuthService authService;

    @Autowired
    public TestPayPalAuthService(PayPalAuthService authService,
                                 PayPalConfig payPalConfig) {
        this.authService = authService;
        this.payPalConfig = payPalConfig;
    }

    @Test
    public void testGetToken() {
        if (doSkip()) {
            return;
        }

        final PayPalToken token = authService.getToken();

        assertNotNull(token.getAccessToken());
        assertNotNull(token.getTokenType());
        assertNotNull(token.getAppId());
        assertNotNull(token.getNonce());
        assertNotNull(token.getExpiresIn());
        assertNotNull(token.getGeneratedAt());
        assertNotNull(token.getScope());
    }

    public boolean doSkip() {
        return Strings.isEmpty(payPalConfig.getClientId()) || Strings.isEmpty(payPalConfig.getClientSecret());
    }

}
