package fr.funixgaming.api.core.websocket.services.client;

import jakarta.websocket.ClientEndpointConfig;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
class ApiWebSocketClientConfigurator extends ClientEndpointConfig.Configurator {

    private final Map<String, String> headers;

    @Override
    public void beforeRequest(final Map<String, List<String>> headers) {
        for (final Map.Entry<String, String> entry : this.headers.entrySet()) {
            headers.put(entry.getKey(), List.of(entry.getValue()));
        }
    }

}
