package com.github.academivillage.jcloud.util.cache;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.function.Supplier;

/**
 * Encapsulates the caching operations.
 *
 * @param <T> The type of cache values. For the cache to be expirable the {@link T} must implement the {@link Expirable}.
 * @author Younes Rahimi
 */
public interface Cache<T> {

    /**
     * Fetches the value associated with the given key from cache.
     *
     * @param key The given key to be fetched from cache.
     * @return The value which is related to the key or null if it does not found or can not be inquired from cache.
     */
    @Nullable
    T get(String key);

    /**
     * Fetches the value associated with the given key from cache.
     *
     * @param key              The given key to be fetched from cache.
     * @param newValueSupplier The supplier of the new value to be inserted in cache in case of the cache-miss.
     * @return The cached value which is fetched from cache.
     */
    @Nullable
    T get(String key, Supplier<T> newValueSupplier);

    /**
     * Evicts the given key from cache.
     *
     * @param key Represents the key to evict from cache.
     */
    void evict(String key);

    /**
     * Tries to clear the cache.
     */
    void invalidate();

    /**
     * Encapsulates the expiration date for a cacheable entity.
     */
    interface Expirable {

        /**
         * Represents the expiration date in the future.
         */
        Instant getExpiresAt();
    }
}
