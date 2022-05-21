package com.imageanalysis.commons.util.dynamikax.msuser;

import com.imageanalysis.commons.util.dynamikax.msuser.dto.UserDto;

import java.util.Optional;

public interface MsUserClient {
    /**
     * Generates the JWT token for the default user.
     *
     * @return The JWT token of the default user.
     */
    String getJwtToken();

    /**
     * Generates the JWT token for the provided user.
     *
     * @return The JWT token of the provided user.
     */
    String getJwtToken(String email, String password);

    Optional<UserDto> fetchUser(Long userId);
}
