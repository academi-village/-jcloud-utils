package com.imageanalysis.commons.util.dynamikax.security;

import com.imageanalysis.commons.errors.AppException;
import com.imageanalysis.commons.errors.ProjectError;

import java.util.Optional;

import static com.imageanalysis.commons.errors.ProjectError.USER_NOT_AUTHENTICATED;

public interface AuthorizationService {

    /**
     * Checks whether the user calling current API is authenticated?
     *
     * @throws AppException with {@link ProjectError#USER_NOT_AUTHENTICATED} if the user isn't authenticated.
     */
    void checkIsAuthenticated();

    /**
     * Checks the user access to at least one of the provided global permissions.
     *
     * @throws AppException with {@link ProjectError#USER_NOT_AUTHENTICATED} if the user isn't authenticated.
     * @throws AppException with {@link ProjectError#ACCESS_DENIED} if the user doesn't have any of the required global permissions.
     */
    void checkAccess(Permission... globalPermissions);

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
     * Could be used in presence of a Servlet request context, for example in controllers.
     *
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
     /** Could be used in presence of a Servlet request context, for example in controllers.
     * @return The JWT token of the current API caller.
     * @throws AppException with {@link ProjectError#USER_NOT_AUTHENTICATED} if the user is not authenticated.
     */
    default String getJwtToken() {
        return getOptionalJwtToken().orElseThrow(USER_NOT_AUTHENTICATED::ex);
    }

    /**
     /** Could be used in absence of a Servlet request context, for example in background threads and Kafka consumers.
     * @return The user details of the given JWT token.
     * @throws AppException with {@link ProjectError#USER_NOT_AUTHENTICATED} if the JWT token is not valid.
     */
    UserDetails getUser(String jwtToken);
}
