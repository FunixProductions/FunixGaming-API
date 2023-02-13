package fr.funixgaming.api.server.external_api_impl.paypal.orders.services;

import feign.FeignException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.server.external_api_impl.paypal.orders.clients.PaypalOrderClient;
import fr.funixgaming.api.server.external_api_impl.paypal.orders.dtos.requests.PaypalOrderCreationDTO;
import fr.funixgaming.api.server.external_api_impl.paypal.orders.dtos.responses.PaypalOrderResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaypalOrderService implements PaypalOrderClient {

    private final PaypalOrderClient orderClient;

    @Override
    public PaypalOrderResponseDTO createOrder(String metadataId,
                                              String requestId,
                                              PaypalOrderCreationDTO request) {
        try {
            return this.orderClient.createOrder(metadataId, requestId, request);
        } catch (FeignException e) {
            throw handleFeignException(e, "de créer");
        }
    }

    @Override
    public PaypalOrderResponseDTO getOrder(String orderId) {
        try {
            return this.orderClient.getOrder(orderId);
        } catch (FeignException e) {
            throw handleFeignException(e, "de récupérer");
        }
    }

    @Override
    public PaypalOrderResponseDTO authorizeOrder(String paypalAuthSession,
                                                 String paypalClientMetadataId,
                                                 String requestId,
                                                 String orderId) {
        try {
            return this.orderClient.authorizeOrder(paypalAuthSession, paypalClientMetadataId, requestId, orderId);
        } catch (FeignException e) {
            throw handleFeignException(e, "d'autoriser");
        }
    }

    @Override
    public PaypalOrderResponseDTO captureOrder(String paypalAuthSession,
                                               String paypalClientMetadataId,
                                               String requestId,
                                               String orderId) {
        try {
            return this.orderClient.captureOrder(paypalAuthSession, paypalClientMetadataId, requestId, orderId);
        } catch (FeignException e) {
            throw handleFeignException(e, "de capturer");
        }
    }

    private ApiException handleFeignException(final FeignException e, final String action) {
        throw new ApiException(String.format("Impossible %s un ordre d'achat avec PayPal. Veuillez recommencer ou envoyer un mail à contact@funixgaming.fr", action), e);
    }
}
