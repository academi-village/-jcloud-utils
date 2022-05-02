package com.github.academivillage.jcloud.util.dynamikax.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@RequiredArgsConstructor
public class SecurityAuditorAware implements AuditorAware<String> {

    private final AuthorizationService authService;

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(authService.getUsername());
    }
}
