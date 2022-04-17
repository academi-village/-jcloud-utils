package com.github.academivillage.jcloud.util.cache;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Encapsulates the caching operations.
 *
 * @param <T> The type of cache values.
 * @author Younes Rahimi
 */
public interface Cache<T> {

    /**
     * Fetches the value associated with the given key from cache.
     * Before getting from cache, it checks if there were any keys which is needed to be evicted from cache or any
     * pending invalidates due to inconsistency.
     *
     * @param key The given key to be fetched from cache.
     * @return The value which is related to the key or null if it does not found or can not be inquired from cache.
     */
    @Nullable
    T get(String key);

    /**
     * Fetches the value associated with the given key from cache.
     * In case of the cache-miss, it fills the cache with the value provided by {@code newValueSupplier}.
     * If the provided value was null, it will not be cached.
     *
     * @param key              The given key to be fetched from cache.
     * @param newValueSupplier The supplier of the new value to be inserted in cache in case of the cache-miss.
     * @return The value which is fetched/added to cache.
     */
    @Nullable
    T get(String key, Supplier<T> newValueSupplier);

    /**
     * Evicts the given key from cache.
     * If the cache is down, and it could not put to cache, it will put that key into a local map to be evicted later
     * when the cache is ready.
     *
     * @param key Represents the key to evict from cache.
     */
    void evict(String key);

    /**
     * Tries to clear the cache. If the cache backend is down/disconnected,
     * it sets the pendingInvalidate to true to clear the cache as soon as the cache is available.
     */
    void invalidate();
}
