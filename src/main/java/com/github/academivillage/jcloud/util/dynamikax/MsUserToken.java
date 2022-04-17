package com.github.academivillage.jcloud.util.dynamikax;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.Instant;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@JsonAutoDetect(fieldVisibility = ANY)
public class MsUserToken {
    public String  userName;
    public String  jwtToken;
    public Instant expiresAt;
}
