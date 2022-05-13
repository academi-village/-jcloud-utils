package com.imageanalysis.commons.util.dynamikax.security;

import com.imageanalysis.commons.errors.AppException;
import com.imageanalysis.commons.util.java.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.imageanalysis.commons.errors.ProjectError.ACCESS_DENIED;
import static com.imageanalysis.commons.errors.ProjectError.USER_NOT_AUTHENTICATED;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "authorization.enable", havingValue = "true", matchIfMissing = true)
public class SimpleAuthorizationService implements AuthorizationService {

    private static final String BEARER      = "Bearer ";
    private static final String PRM         = "prm";
    private static final String PERMISSIONS = "permissions";

    private final JwtParser jwtParser;

    @Override
    public void checkIsAuthenticated() {
        getClaims();
    }

    @Override
    public void checkAccess(Permission... permissions) {
        val globals     = (List<String>) extractPermissions(getClaims()).get(VeryCompactJwtPermissions.GLOBALS);
        val hasNoAccess = Arrays.stream(permissions).map(Permission::getName).noneMatch(globals::contains);
        if (!hasNoAccess)
            throw new AppException(ACCESS_DENIED);
    }

    @Override
    public void checkAccess(long studyId, Permission... permissions) {
        val activitiesMap = (List<Map>) extractPermissions(getClaims()).get(VeryCompactJwtPermissions.ACTIVITIES);
        activitiesMap.stream()
                .filter(jwtAcc -> ((Integer) jwtAcc.get(VeryCompactJwtPermissions.PROJECT_ID)).longValue() == studyId)
                .map(jwtAcc -> (List<String>) jwtAcc.get(VeryCompactJwtPermissions.ACTIVITIES))
                .filter(act -> Arrays.stream(permissions).map(Permission::getName).anyMatch(act::contains))
                .findAny()
                .orElseThrow(ACCESS_DENIED::ex);
    }

    @Override
    public Optional<UserDetails> getOptionalUser() {
        try {
            return getOptionalJwtToken().map(this::getUser);
        } catch (AppException e) {
            if (e.getError() == USER_NOT_AUTHENTICATED)
                return Optional.empty();

            throw e;
        }
    }

    @Override
    public Optional<String> getOptionalJwtToken() {
        val attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("ServletRequestAttributes is null. It seems you called this from a non-request bounded thread");

            return Optional.empty();
        }

        val request       = attributes.getRequest();
        val authorization = request.getHeader("Authorization");
        if (!StringUtils.hasText(authorization))
            return Optional.empty();

        val jwtToken = authorization.replace(BEARER, "");

        if (!StringUtils.hasText(jwtToken))
            return Optional.empty();

        return Optional.of(jwtToken);
    }

    @Override
    public UserDetails getUser(String jwtToken) {
        Claims claims = getClaims(jwtToken);
        long   userId;
        try {
            userId = Long.parseLong(claims.getId());
        } catch (NumberFormatException e) {
            log.error("Can't convert user ID to a long number", e);

            throw new AppException(USER_NOT_AUTHENTICATED);
        }

        return new User(userId, claims.getSubject());
    }

    private Claims getClaims() {
        val token = getOptionalJwtToken().orElseThrow(USER_NOT_AUTHENTICATED::ex);

        return getClaims(token);
    }

    private Claims getClaims(String token) {
        try {
            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            if (claims == null)
                throw new AppException(USER_NOT_AUTHENTICATED);

            return claims;
        } catch (Exception ex) {
            if (ex instanceof AppException)
                throw ex;

            log.warn("Can't parse JWT token: {}", ex.getMessage());
            throw new AppException(USER_NOT_AUTHENTICATED);
        }
    }

    /**
     * @param claims Encapsulates the claims of a JWT token including either {@code permissions} or {@code prm}.
     * @return A map containing keys {@code globals} and {@code activities}.
     */
    private Map<String, Object> extractPermissions(Claims claims) {
        if (claims.containsKey(PRM) && !claims.containsKey(PERMISSIONS))
            return new VeryCompactJwtPermissions(((Map<String, String>) claims.get(PRM))).decodeToMap();

        List<String> globals       = (List<String>) ((Map) claims.get(PERMISSIONS)).get(VeryCompactJwtPermissions.GLOBALS);
        List<Map>    activitiesMap = (List<Map>) ((Map) claims.get(PERMISSIONS)).get(VeryCompactJwtPermissions.ACTIVITIES);

        return Maps.of(VeryCompactJwtPermissions.GLOBALS, globals, VeryCompactJwtPermissions.ACTIVITIES, activitiesMap);
    }
}
