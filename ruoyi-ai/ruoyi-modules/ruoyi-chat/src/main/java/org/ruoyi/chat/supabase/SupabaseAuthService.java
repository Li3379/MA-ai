package org.ruoyi.chat.supabase;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SupabaseAuthService {

    private final SupabaseProperties properties;
    private final RestClient restClient;
    private final Map<String, ConfigurableJWTProcessor<SecurityContext>> processorCache = new ConcurrentHashMap<>();

    public SupabaseAuthService(SupabaseProperties properties, RestClient supabaseRestClient) {
        this.properties = properties;
        this.restClient = supabaseRestClient;
    }

    public SupabaseUser verifyAccessToken(String token) throws ParseException, BadJOSEException, JOSEException, MalformedURLException {
        if (!StringUtils.hasText(properties.getApiUrl())) {
            throw new IllegalStateException("Supabase API 地址未配置");
        }
        SignedJWT signedJWT = SignedJWT.parse(token);
        String issuer = signedJWT.getJWTClaimsSet().getIssuer();

        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = processorCache.computeIfAbsent(issuer, iss -> {
            try {
                URL jwksUrl = new URL(properties.getApiUrl() + "/auth/v1/.well-known/jwks.json");
                JWKSource<SecurityContext> jwkSource = new RemoteJWKSet<>(jwksUrl);
                DefaultJWTProcessor<SecurityContext> processor = new DefaultJWTProcessor<>();
                JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);
                processor.setJWSKeySelector(keySelector);
                return processor;
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Supabase JWKS 地址错误", e);
            }
        });

        jwtProcessor.process(signedJWT, null);

        Instant expiresAt = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
        if (expiresAt.isBefore(Instant.now())) {
            throw new BadJOSEException("Supabase token 已过期");
        }

        String userId = signedJWT.getJWTClaimsSet().getSubject();
        String email = (String) signedJWT.getJWTClaimsSet().getClaim("email");

        return new SupabaseUser(userId, email, token);
    }

    public String createSignedStorageUrl(String bucket, String path, int expiresInSeconds) {
        String endpoint = "/storage/v1/object/sign/" + bucket + "/" + path;
        Map<String, Object> response = restClient.post()
            .uri(endpoint)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getServiceKey())
            .body(Map.of("expiresIn", expiresInSeconds))
            .retrieve()
            .body(Map.class);
        return response != null ? (String) response.getOrDefault("signedURL", "") : "";
    }

    public record SupabaseUser(String userId, String email, String rawToken) {
    }
}
