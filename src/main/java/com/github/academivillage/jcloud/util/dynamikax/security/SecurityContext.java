package com.github.academivillage.jcloud.util.dynamikax.security;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class SecurityContext<T extends UserDetails> {

    @Nullable
    private T principal;
}
