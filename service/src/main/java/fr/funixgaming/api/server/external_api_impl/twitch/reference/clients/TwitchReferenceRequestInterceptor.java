package fr.funixgaming.api.server.external_api_impl.twitch.reference.clients;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import fr.funixgaming.api.server.external_api_impl.twitch.configs.TwitchApiConfig;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TwitchReferenceRequestInterceptor implements RequestInterceptor {

    private final TwitchApiConfig apiConfig;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Client-Id", apiConfig.getAppClientId());
    }
}
