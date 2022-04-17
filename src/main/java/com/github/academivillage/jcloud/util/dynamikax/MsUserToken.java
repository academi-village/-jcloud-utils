package com.github.academivillage.jcloud.util.dynamikax;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.time.Instant;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@JsonAutoDetect(fieldVisibility = ANY)
public class MsUserToken {

    /**
     * Subject of JWT token or merely the user email.
     */
    public String subject;

    public String jwtToken;

    public Instant expiresAt;

    public MsUserToken(String jws) {
        int    i                = jws.lastIndexOf('.');
        String withoutSignature = jws.substring(0, i + 1);
        Claims claims           = Jwts.parserBuilder().build().parseClaimsJwt(withoutSignature).getBody();

        this.jwtToken  = jws;
        this.subject   = claims.getSubject();
        this.expiresAt = claims.getExpiration().toInstant();
    }
}
