package com.github.academivillage.jcloud.util.dynamikax.msuser;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.github.academivillage.jcloud.util.cache.Cache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.Getter;

import java.time.Instant;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@JsonAutoDetect(fieldVisibility = ANY)
public class MsUserToken implements Cache.Expirable {

    private static final JwtParser JWT_PARSER = Jwts.parserBuilder().build();

    /**
     * Subject of JWT token or merely the user email. Example: {@code iag.microservice@ia-grp.com}
     */
    public String subject;

    /**
     * Represents the user ID.
     */
    public Long userId;

    /**
     * Represents the JWT token itself.
     */
    public String jwtToken;

    /**
     * Represents the expiration date of JWT token.
     */
    @Getter
    public Instant expiresAt;

    /**
     * @param jws Represents the JWT token.
     */
    public MsUserToken(String jws) {
        int    i                = jws.lastIndexOf('.');
        String withoutSignature = jws.substring(0, i + 1);
        Claims claims           = JWT_PARSER.parseClaimsJwt(withoutSignature).getBody();

        this.jwtToken  = jws;
        this.subject   = claims.getSubject();
        this.userId    = Long.parseLong((String) claims.get("jti"));
        this.expiresAt = claims.getExpiration().toInstant().minusSeconds(1800);
    }
}
