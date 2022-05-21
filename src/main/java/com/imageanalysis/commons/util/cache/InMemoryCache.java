package com.imageanalysis.commons.util.cache;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * An in-memory implementation of {@link Cache} using a {@link ConcurrentHashMap}.
 *
 * @param <T> The type of cache values. For the cache to be expirable the {@link T} must implement the {@link Expirable}.
 * @author Younes Rahimi
 */
@RequiredArgsConstructor
public class InMemoryCache<T> implements Cache<T> {
    private final static Duration CLEAN_UP_PERIOD = Duration.ofMinutes(10);

    /**
     * Represents the cache backend. Contains the actual cache key/values.
     */
    private final ConcurrentHashMap<String, T> cacheMap = new ConcurrentHashMap<>();

    private Instant nextCleanUp = Instant.now().plus(CLEAN_UP_PERIOD);

    /**
     * @see Cache#get(String)
     */
    @Override
    public @Nullable T get(String key) {
        cleanUp();
        return cacheMap.get(key);
    }

    /**
     * @see Cache#get(String, Supplier)
     */
    @Override
    public T get(String key, Supplier<T> newValueSupplier) {
        cleanUp();
        val cachedValue = get(key);
        if (cachedValue != null && isValid(cachedValue))
            return cachedValue;

        val newValue = Objects.requireNonNull(newValueSupplier.get());
        cacheMap.compute(key, (k, oldValue) -> oldValue == null ? newValue : oldValue);

        return newValue;
    }

    /**
     * @see Cache#evict(String)
     */
    @Override
    public void evict(String key) {
        cacheMap.remove(key);
    }

    /**
     * @see Cache#invalidate()
     */
    @Override
    public void invalidate() {
        cacheMap.clear();
    }

    /**
     * Checks the expiration state of cached value.
     *
     * @return Whether the expiration past is not past (cache is valid)?
     */
    private boolean isValid(T cachedValue) {
        if (cachedValue instanceof Expirable)
            return !((Expirable) cachedValue).isExpired();

        return true;
    }

    private void cleanUp() {
        boolean mustCleanUp = nextCleanUp.isBefore(Instant.now());
        if (mustCleanUp)
            synchronized (cacheMap) {
                mustCleanUp = nextCleanUp.isBefore(Instant.now());
                if (!mustCleanUp)
                    return;

                nextCleanUp = Instant.now().plus(CLEAN_UP_PERIOD);
                for (val entry : cacheMap.entrySet()) {
                    if (!(entry.getValue() instanceof Expirable))
                        continue;

                    val expirable = ((Expirable) entry.getValue());
                    if (expirable.isExpired())
                        cacheMap.remove(entry.getKey(), entry.getValue());
                }
            }
    }
}
