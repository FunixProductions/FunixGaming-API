package fr.funixgaming.api.server.payment.paypal.services;

import fr.funixgaming.api.client.payment.paypal.clients.PayPalAuthClient;
import fr.funixgaming.api.client.payment.paypal.dtos.PayPalToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;

@Service
public class PayPalAuthService {
    private final PayPalAuthClient authClient;
    private PayPalToken payPalToken = null;

    public PayPalAuthService(PayPalAuthClient authClient) {
        this.authClient = authClient;
    }

    public PayPalToken getToken() {
        if (!isTokenAvailable()) {
            this.payPalToken = generateToken();
        }
        return payPalToken;
    }

    private boolean isTokenAvailable() {
        return this.payPalToken != null && Instant.now().isBefore(this.payPalToken.getGeneratedAt().toInstant().plusSeconds(this.payPalToken.getExpiresIn() - 30));
    }

    private PayPalToken generateToken() {
        return authClient.getToken("grant_type=client_credentials");
    }
}
