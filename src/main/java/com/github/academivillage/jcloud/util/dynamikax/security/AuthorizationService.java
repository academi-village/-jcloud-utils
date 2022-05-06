package com.github.academivillage.jcloud.util.dynamikax.security;

import com.github.academivillage.jcloud.errors.AppException;
import com.github.academivillage.jcloud.errors.ProjectError;

import java.util.Optional;

import static com.github.academivillage.jcloud.errors.ProjectError.USER_NOT_AUTHENTICATED;

public interface AuthorizationService {

    /**
     * Checks whether the user calling current API is authenticated?
     *
     * @throws AppException with {@link ProjectError#USER_NOT_AUTHENTICATED} if the user isn't authenticated.
     */
    void checkIsAuthenticated();

    /**
     * Checks the user access to at least one of the provided permissions.
     *
     * @throws AppException with {@link ProjectError#USER_NOT_AUTHENTICATED} if the user isn't authenticated.
     * @throws AppException with {@link ProjectError#ACCESS_DENIED} if the user doesn't have any of the required permissions.
     */
    void checkAccess(Permission... permissions);

    /**
     * Checks the user access to at least one of the provided permissions on the given study ID.
     *
     * @throws AppException with {@link ProjectError#USER_NOT_AUTHENTICATED} if the user isn't authenticated.
     * @throws AppException with {@link ProjectError#ACCESS_DENIED} if the user doesn't have any of the required permissions.
     */
    void checkAccess(long studyId, Permission... permissions);

    /**
     * @return The optional user details of the current API caller.
     */
    Optional<UserDetails> getOptionalUser();

    /**
     * @return The user details of the current API caller.
     * @throws AppException with {@link ProjectError#USER_NOT_AUTHENTICATED} if the user is not authenticated.
     */
    default UserDetails getUser() {
        return getOptionalUser().orElseThrow(USER_NOT_AUTHENTICATED::ex);
    }

    /**
     * @return The optional JWT token of the current API caller.
     */
    Optional<String> getOptionalJwtToken();

    /**
     * @return The JWT token of the current API caller.
     * @throws AppException with {@link ProjectError#USER_NOT_AUTHENTICATED} if the user is not authenticated.
     */
    default String getJwtToken() {
        return getOptionalJwtToken().orElseThrow(USER_NOT_AUTHENTICATED::ex);
    }

    /**
     * @return The user details of the given JWT token.
     * @throws AppException with {@link ProjectError#USER_NOT_AUTHENTICATED} if the JWT token is not valid.
     */
    UserDetails getUser(String jwtToken);
}
