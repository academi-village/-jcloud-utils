package com.imageanalysis.commons.util.dynamikax.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class User implements UserDetails {
    private final long   Id;
    private final String username;
}
