package com.github.academivillage.jcloud.util.cache;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * An in-memory implementation of {@link Cache} using {@link ConcurrentHashMap}.
 *
 * @param <T> The type of cache values.
 * @author Younes Rahimi
 */
@RequiredArgsConstructor
public class InMemoryCache<T> implements Cache<T> {

    private final ConcurrentHashMap<String, T> cacheMap = new ConcurrentHashMap<>();

    @Override
    public @Nullable T get(String key) {
        return cacheMap.get(key);
    }

    @Override
    public @Nullable T get(String key, Supplier<T> newValueSupplier) {
        val cachedValue = get(key);
        if (cachedValue != null)
            return cachedValue;

        val newValue = newValueSupplier.get();
        if (newValue != null)
            cacheMap.compute(key, (k, oldValue) -> oldValue == null ? newValue : oldValue);

        return newValue;
    }

    @Override
    public void evict(String key) {
        cacheMap.remove(key);
    }

    @Override
    public void invalidate() {
        cacheMap.clear();
    }
}
