package ru.project.jbd.domain.model;

import java.util.Set;

public enum ERole {
    ROLE_USER,
    ROLE_ADMIN;

    public static Set<ERole> admin() {
        return Set.of(ERole.ROLE_USER, ERole.ROLE_ADMIN);
    }

    public static Set<ERole> user() {
        return Set.of(ERole.ROLE_USER);
    }
}
