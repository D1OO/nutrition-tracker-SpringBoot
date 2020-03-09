/**
 * RoleType
 * <p>
 * version 1.0
 * <p>
 * 06.03.2020
 * <p>
 * Copyright(r) shvdy.net
 */

package net.shvdy.sbproject.entity;

import org.springframework.security.core.GrantedAuthority;

public enum RoleType implements GrantedAuthority {
    ROLE_SUPERADMIN,
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_USER_WEB;

    @Override
    public String getAuthority() {
        return name();
    }
}
