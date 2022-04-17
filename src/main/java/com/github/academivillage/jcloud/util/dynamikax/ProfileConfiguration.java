package com.github.academivillage.jcloud.util.dynamikax;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class ProfileConfiguration {

    private final Environment env;

    @Bean
    public Profile profile() {
        return Arrays.stream(env.getActiveProfiles())
                .map(Profile::ofBranch)
                .findFirst()
                .flatMap(Function.identity())
                .orElse(Profile.DEVELOP);
    }
}
