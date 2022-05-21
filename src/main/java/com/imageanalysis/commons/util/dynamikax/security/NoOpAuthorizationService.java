package com.imageanalysis.commons.util.dynamikax.security;

import io.jsonwebtoken.JwtParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@ConditionalOnProperty(value = "authorization.enable", havingValue = "false")
public class NoOpAuthorizationService extends SimpleAuthorizationService {

    private final User fakeUser = new User(0L, "__fake_user__");

    public NoOpAuthorizationService(JwtParser jwtParser) {
        super(jwtParser);
    }

    @Override
    public void checkIsAuthenticated() {
    }

    @Override
    public void checkAccess(Permission... permissions) {
    }

    @Override
    public void checkAccess(long studyId, Permission... permissions) {
    }

    @Override
    public Optional<UserDetails> getOptionalUser() {
        return Optional.of(super.getOptionalUser().orElse(fakeUser));
    }
}
