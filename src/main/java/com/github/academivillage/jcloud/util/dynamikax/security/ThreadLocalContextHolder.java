package com.github.academivillage.jcloud.util.dynamikax.security;

import org.springframework.util.Assert;

public class ThreadLocalContextHolder {

    private static final ThreadLocal<SecurityContext<UserDetails>> contextHolder = new ThreadLocal<>();

    public static void clearContext() {
        contextHolder.remove();
    }

    public static SecurityContext<UserDetails> getContext() {
        SecurityContext<UserDetails> ctx = contextHolder.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }

        return ctx;
    }

    public static void setContext(SecurityContext<UserDetails> context) {
        Assert.notNull(context, "Only non-null Context instances are permitted");
        contextHolder.set(context);
    }

    public static SecurityContext<UserDetails> createEmptyContext() {
        return new SecurityContext<>();
    }
}
