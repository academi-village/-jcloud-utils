package com.imageanalysis.commons.util.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Duration;
import java.time.Instant;

@ToString
@RequiredArgsConstructor
public class ExpirableValue<T> implements Cache.Expirable {

    public final T value;

    @Getter
    public final Instant expiresAt;

    public ExpirableValue(T value, Duration expiration) {
        this.value     = value;
        this.expiresAt = Instant.now().plus(expiration);
    }
}
