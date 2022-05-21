package com.imageanalysis.commons.util.dynamikax.security;

import com.imageanalysis.commons.spring.Profile;
import com.imageanalysis.commons.util.jooq.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@RequiredArgsConstructor
public class SecurityAuditorAware implements AuditorAware<String> {

    private final AuthorizationService authService;

    private final Profile activeProfile;

    @Override
    public Optional<String> getCurrentAuditor() {
        String username = authService.getOptionalUser().map(UserDetails::getUsername).orElse(getUsernameFromContext());
        username = StringUtils.defaultIfBlank(username, activeProfile.getDefaultUsername());

        return Optional.of(username);
    }

    @Nullable
    private String getUsernameFromContext() {
        val principal = ThreadLocalContextHolder.getContext().getPrincipal();

        return principal != null ? principal.getUsername() : null;
    }
}
