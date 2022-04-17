package com.github.academivillage.jcloud.util.dynamikax;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class ProfileConfiguration {

    private final Environment env;

    @Bean
    public Profile profile() {
        return Arrays.stream(env.getActiveProfiles()).filter(BRANCHES::contains).findFirst().orElse("develop");
    }
}
