package fr.funixgaming.api.server.external_api_impl.twitch.services;

import fr.funixgaming.api.client.external_api_impl.twitch.enums.TwitchClientTokenType;
import fr.funixgaming.api.core.utils.string.PasswordGenerator;
import fr.funixgaming.api.server.external_api_impl.twitch.configs.TwitchApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * <a href="https://dev.twitch.tv/docs/authentication/getting-tokens-oauth#authorization-code-grant-flow">Documentation</a>
 */
@Service
@Slf4j(topic = "TwitchAppClientTokenService")
public class TwitchAppClientTokenService {

    private final TwitchApiConfig twitchApiConfig;
    private final PasswordGenerator passwordGenerator;

    private final Set<String> csrfTokens = new HashSet<>();

    public TwitchAppClientTokenService(TwitchApiConfig twitchApiConfig) {
        this.twitchApiConfig = twitchApiConfig;

        final PasswordGenerator passwordGenerator = new PasswordGenerator();
        passwordGenerator.setSpecialCharsAmount(0);
        passwordGenerator.setAlphaDown(20);
        passwordGenerator.setAlphaUpper(20);
        passwordGenerator.setNumbersAmount(20);
        this.passwordGenerator = passwordGenerator;
    }

    public String getAuthClientUrl(final TwitchClientTokenType tokenType) {
        return twitchApiConfig.getAppAuthDomainUrl() + "/oauth2/authorize" +
                "?response_type=code" +
                "&client_id=" + twitchApiConfig.getAppClientId() +
                "&redirect_uri=" + twitchApiConfig.getAppDomainCallback() +
                "&scope=" + generateScopesForTokenType(tokenType) +
                "&state=" + generateNewState();
    }

    /**
     * <a href="https://dev.twitch.tv/docs/authentication/scopes">Scopes list</a>
     * <p>Encoders: %20 is space and %3A is :</p>
     * @param tokenType token type generated
     * @return scope list http formatted
     */
    private String generateScopesForTokenType(final TwitchClientTokenType tokenType) {
        final Set<String> scopes;

        switch (tokenType) {
            case STREAMER -> scopes = Set.of(

            );
            case MODERATOR -> scopes = Set.of(

            );
            case BOT -> scopes = Set.of(

            );
            default -> scopes = Set.of(

            );
        }

        final String scopeList = String.join("%20", scopes);
        return scopeList.replaceAll(":", "%3A");
    }

    private String generateNewState() {
        final String state = passwordGenerator.generateRandomPassword();

        this.csrfTokens.add(state);
        return state;
    }
}
