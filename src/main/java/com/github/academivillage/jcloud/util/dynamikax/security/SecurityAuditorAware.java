package com.github.academivillage.jcloud.util.dynamikax.security;

import com.github.academivillage.jcloud.util.dynamikax.Profile;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@RequiredArgsConstructor
public class SecurityAuditorAware implements AuditorAware<String> {

    private final AuthorizationService authService;

    private final Profile activeProfile;

    @Override
    public Optional<String> getCurrentAuditor() {
        val username = authService.getUsername().orElse(activeProfile.getDefaultUsername());

        return Optional.of(username);
    }
}
