package fr.funixgaming.api.server.user.services;

import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.utils.time.TimeUtils;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.entities.UserToken;
import fr.funixgaming.api.server.user.mappers.UserTokenMapper;
import fr.funixgaming.api.server.user.repositories.UserRepository;
import fr.funixgaming.api.server.user.repositories.UserTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserTokenService {
    private final static String ISSUER = "FunixApi - api.funixgaming.fr";

    private final UserTokenRepository tokenRepository;
    private final UserTokenMapper tokenMapper;
    private final UserRepository userRepository;
    private final Key jwtSecretKey;

    public UserTokenService(UserTokenRepository tokenRepository,
                            UserTokenMapper tokenMapper,
                            UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.tokenMapper = tokenMapper;
        this.userRepository = userRepository;
        this.jwtSecretKey = getJwtCryptKey();
    }

    private static Key getJwtCryptKey() {
        final File keyFile = new File("secretJwt.key");
        Key key = getKeyFromFile(keyFile);

        if (key == null) {
            key = generateNewKey();
            saveKey(key, keyFile);
            return key;
        } else {
            return key;
        }
    }

    @Nullable
    private static Key getKeyFromFile(final File keyFile) throws ApiException {
        try {
            if (keyFile.exists()) {
                final String content = Files.readString(keyFile.toPath(), StandardCharsets.UTF_8);

                if (!Strings.isEmpty(content)) {
                    final byte[] decodedKey = Decoders.BASE64.decode(content);
                    return Keys.hmacShaKeyFor(decodedKey);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new ApiException("Une erreur est survenue lors du chargement de la clé secrete pour les tokens JWT.", e);
        }
    }

    private static Key generateNewKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    private static void saveKey(final Key key, final File keyFile) {
        try {
            if (!keyFile.exists() && !keyFile.createNewFile()) {
                throw new IOException("Creation file failed.");
            }

            final String encodedKey = Encoders.BASE64.encode(key.getEncoded());
            Files.writeString(keyFile.toPath(), encodedKey, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new ApiException("Impossible de sauvegarder la clé de cryptage pour les tokens JWT.");
        }
    }

    @Transactional
    public UserTokenDTO generateAccessToken(final User user) {
        final Instant now = Instant.now();
        final Instant expiresAt = now.plusSeconds(604800); //one week
        final UserToken userToken = new UserToken();

        userToken.setUser(user);
        userToken.setUuid(UUID.randomUUID());
        userToken.setExpirationDate(Date.from(expiresAt));
        userToken.setToken(Jwts.builder()
                .setSubject(userToken.getUuid().toString())
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiresAt))
                .signWith(jwtSecretKey)
                .compact());

        return tokenMapper.toDto(tokenRepository.save(userToken));
    }

    @Transactional
    public void invalidTokens(@Nullable final UUID userUUID) {
        if (userUUID == null) return;

        final Optional<User> search = userRepository.findByUuid(userUUID.toString());
        if (search.isPresent()) {
            final User user = search.get();
            final Set<UserToken> tokens = user.getTokens();

            this.tokenRepository.deleteAll(tokens);
        }
    }

    public boolean isTokenValid(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token);

            final UserToken userToken = getToken(token);

            if (userToken == null) {
                throw new ApiBadRequestException("Votre token d'accès est invalide.");
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public UsernamePasswordAuthenticationToken authenticateToken(final String token) {
        final UserToken userToken = getToken(token);
        final User user;

        if (userToken == null) {
            user = null;
        } else {
            user = userToken.getUser();
        }

        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                user == null ? List.of() : user.getAuthorities()
        );
    }

    @Nullable
    private UserToken getToken(final String token) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        final String tokenUuid = claims.getSubject();
        final Optional<UserToken> search = tokenRepository.findByUuid(tokenUuid);

        if (search.isPresent()) {
            return search.get();
        } else {
            return null;
        }
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void removeInvalidTokens() {
        final Iterable<UserToken> tokens = this.tokenRepository.findAll();
        final Instant start = Instant.now();
        int invalidedTokens = 0;

        for (final UserToken token : tokens) {
            final Instant expirationDate = token.getExpirationDate();

            if (expirationDate.isBefore(start)) {
                invalidedTokens++;
                this.tokenRepository.delete(token);
            }
        }

        if (invalidedTokens > 0) {
            final long seconds = TimeUtils.diffInMillisBetweenInstants(start, Instant.now());
            log.info("{} tokens user invalides supprimés. ({} ms)", invalidedTokens, seconds);
        }
    }
}
