package com.imageanalysis.commons.util.dynamikax.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class User implements UserDetails {
    private final Long   id;
    private final String username;
}
