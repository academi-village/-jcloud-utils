package com.github.academivillage.jcloud.util.dynamikax.security;

import com.github.academivillage.jcloud.errors.AppException;
import com.github.academivillage.jcloud.errors.JCloudError;
import com.github.academivillage.jcloud.util.java.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Map;

import static com.github.academivillage.jcloud.errors.JCloudError.ACCESS_DENIED;
import static com.github.academivillage.jcloud.errors.JCloudError.USER_NOT_AUTHENTICATED;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private static final String BEARER = "Bearer ";

    private final JwtParser jwtParser;

    /**
     * Checks whether the user calling current API is authenticated?
     *
     * @throws AppException with {@link JCloudError#USER_NOT_AUTHENTICATED} if the user isn't authenticated.
     */
    public void checkIsAuthenticated() {
        getClaims();
    }

    /**
     * @return The user ID of the API caller.
     * @throws AppException with {@link JCloudError#USER_NOT_AUTHENTICATED} if the user isn't authenticated.
     */
    public long getUserId() {
        Claims claims = getClaims();
        String userId = claims.getId();
        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            log.error("Can't convert userID {} to a long number", userId, e);
            throw new AppException(USER_NOT_AUTHENTICATED);
        }
    }

    /**
     * @return The username of the API caller.
     * @throws AppException with {@link JCloudError#USER_NOT_AUTHENTICATED} if the user isn't authenticated.
     */
    public String getUsername() {
        return getClaims().getSubject();
    }

    /**
     * Checks the user access to the provided permission.
     *
     * @throws AppException with {@link JCloudError#USER_NOT_AUTHENTICATED} if the user isn't authenticated.
     * @throws AppException with {@link JCloudError#ACCESS_DENIED} if the user doesn't have required permission.
     */
    public void checkAccess(Permission permission) {
        checkAccess(permission.getName(), null);
    }

    /**
     * Checks the user access to the provided permission.
     *
     * @throws AppException with {@link JCloudError#USER_NOT_AUTHENTICATED} if the user isn't authenticated.
     * @throws AppException with {@link JCloudError#ACCESS_DENIED} if the user doesn't have required permission.
     */
    public void checkAccess(String permission) {
        checkAccess(permission, null);
    }

    /**
     * Checks the user access to the provided permission on the given study ID.
     *
     * @throws AppException with {@link JCloudError#USER_NOT_AUTHENTICATED} if the user isn't authenticated.
     * @throws AppException with {@link JCloudError#ACCESS_DENIED} if the user doesn't have required permission.
     */
    public void checkAccess(Permission permission, @Nullable Long studyId) {
        checkAccess(permission.getName(), studyId);
    }

    /**
     * Checks the user access to the provided permission on the given study ID.
     *
     * @throws AppException with {@link JCloudError#USER_NOT_AUTHENTICATED} if the user isn't authenticated.
     * @throws AppException with {@link JCloudError#ACCESS_DENIED} if the user doesn't have required permission.
     */
    public void checkAccess(String permission, @Nullable Long studyId) {
        val permissions = extractPermissions(getClaims());
        if (studyId == null) {
            val globals = (List<String>) permissions.get("globals");
            if (!globals.contains(permission))
                throw new AppException(ACCESS_DENIED);

            return;
        }

        val activitiesMap = (List<Map>) permissions.get("activities");
        for (Map jwtActivity : activitiesMap) {
            val projectId  = (Integer) jwtActivity.get("projectId");
            val activities = (List<String>) jwtActivity.get("activities");
            if (projectId.longValue() == studyId && !activities.contains(permission))
                throw new AppException(ACCESS_DENIED);
        }
    }

    public String getJwtToken() {
        val attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null)
            throw new IllegalStateException("ServletRequestAttributes is null. It seems you called this from a non-request bounded thread");

        val request       = attributes.getRequest();
        val authorization = request.getHeader("Authorization");
        if (!StringUtils.hasText(authorization))
            throw new AppException(USER_NOT_AUTHENTICATED);

        val jwtToken = authorization.replace(BEARER, "");

        if (!StringUtils.hasText(jwtToken))
            throw new AppException(USER_NOT_AUTHENTICATED);

        return jwtToken;
    }

    private Claims getClaims() {
        try {
            Claims claims = jwtParser.parseClaimsJws(getJwtToken()).getBody();
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
        if (claims.containsKey("prm") && !claims.containsKey("permissions"))
            return new VeryCompactJwtPermissions(((Map<String, String>) claims.get("prm"))).decodeToMap();

        List<String> globals       = (List<String>) ((Map) claims.get("permissions")).get("globals");
        List<Map>    activitiesMap = (List<Map>) ((Map) claims.get("permissions")).get("activities");

        return Maps.of("globals", globals, "activities", activitiesMap);
    }
}
