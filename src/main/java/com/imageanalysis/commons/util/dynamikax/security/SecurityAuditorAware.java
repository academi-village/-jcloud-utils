package com.imageanalysis.commons.util.dynamikax.security;

import com.imageanalysis.commons.util.dynamikax.Profile;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.AuditorAware;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
public class SecurityAuditorAware implements AuditorAware<String> {

    private final AuthorizationService authService;

    private final Profile activeProfile;

    @Override
    public Optional<String> getCurrentAuditor() {
        var username = authService.getOptionalUser().map(UserDetails::getUsername).orElse(getUsernameFromContext());
        username = StringUtils.hasText(username) ? username : activeProfile.getDefaultUsername();

        return Optional.of(username);
    }

    @Nullable
    private String getUsernameFromContext() {
        val principal = ThreadLocalContextHolder.getContext().getPrincipal();

        return principal != null ? principal.getUsername() : null;
    }
}
