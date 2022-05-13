package com.imageanalysis.commons.gcp.appengine8;

import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityService.GetAccessTokenResult;
import com.google.appengine.repackaged.com.google.common.collect.Iterables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenPool {

    private final AppIdentityService appIdentityService;

    private final Map<String, AtomicReference<GetAccessTokenResult>> cache = new ConcurrentHashMap<>();

    public GetAccessTokenResult getAccessToken(Iterable<String> scopes) {
        val    start   = System.nanoTime();
        String key     = generateKey(scopes);
        val    arToken = cache.computeIfAbsent(key, k -> new AtomicReference<>(getToken(scopes, key)));
        val tk = arToken.updateAndGet(token -> {
            if (token.getExpirationTime().after(new Date())) return token;
            else {
                log.info("Token of key {} expired. generate a new one. Old Token Expiration: {}, Access Token: {}", key, token.getExpirationTime().toInstant(), token.getAccessToken());
                return getToken(scopes, key);
            }
        });

        val elapsed = (System.nanoTime() - start) / 1_000_000;
        log.info("GetAccessToken fetch for key {} in {} millis ", key, elapsed);

        return tk;
    }

    private GetAccessTokenResult getToken(Iterable<String> scopes, String key) {
        val token = appIdentityService.getAccessToken(scopes);
        log.info("Generating a token for key {}: Expiration Date: {}", key, token.getExpirationTime());
        return new GetAccessTokenResult(token.getAccessToken(), Date.from(Instant.now().plusSeconds(1800)));
    }

    private String generateKey(Iterable<String> scopes) {
        StringBuilder builder = new StringBuilder();
        builder.append("_ah_app_identity_");
        builder.append('[');
        if (!Iterables.isEmpty(scopes)) {
            for (String scope : scopes) {
                builder.append('\'');
                builder.append(scope);
                builder.append("',");
            }

            builder.setLength(builder.length() - 1);
        }

        builder.append(']');
        return builder.toString();
    }
}
