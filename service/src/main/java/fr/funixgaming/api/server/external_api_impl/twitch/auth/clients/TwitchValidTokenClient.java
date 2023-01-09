package fr.funixgaming.api.server.external_api_impl.twitch.auth.clients;

import com.google.gson.Gson;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.dtos.TwitchValidationTokenResponseDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.configs.TwitchApiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
public class TwitchValidTokenClient {

    private final TwitchApiConfig twitchApiConfig;
    private final Gson gson = new Gson();

    @Nullable
    public TwitchValidationTokenResponseDTO makeHttpRequestValidation(final String accessToken) throws ApiException {
        final HttpRequest.Builder httpRequest = HttpRequest.newBuilder();
        httpRequest.uri(URI.create(twitchApiConfig.getAppAuthDomainUrl() + "/oauth2/validate"));
        httpRequest.GET();
        httpRequest.setHeader("Client-Id", twitchApiConfig.getAppClientId());
        httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpRequest.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        try {
            final HttpRequest request = httpRequest.build();
            final HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            final int status = response.statusCode();

            if (Integer.toString(status).startsWith("2")) {
                return gson.fromJson(response.body(), TwitchValidationTokenResponseDTO.class);
            } else if (status == HttpStatus.UNAUTHORIZED.value()) {
                return null;
            } else {
                throw new ApiException("Une erreur est survenue lors du refresh access token via Twitch.");
            }
        } catch (IOException | InterruptedException e) {
            throw new ApiException("Une erreur interne est survenue lors de la vérification du token twitch.", e);
        }
    }

}
