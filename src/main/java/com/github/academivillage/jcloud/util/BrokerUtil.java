package com.github.academivillage.jcloud.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BrokerUtil {
    public static Runnable repeatForever(Runnable runnable) {
        return () -> {
            while (true) {
                try {
                    runnable.run();
                } catch (Exception ex) {
                    log.error("Fatal Error ", ex);
                }
            }
        };
    }
}
