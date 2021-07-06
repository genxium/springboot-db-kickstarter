package com.mytrial.app.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component
public class GrantedRolesMapper {
    // Roles for players
    public static final int ROLE_NONE = 0;
    public static final int ROLE_ROOT = (1 << 0);
    public static final int ROLE_ROOT_DUAL = (1 << 1);
    public static final int ROLE_COOPADMIN = (1 << 2);
    public static final int ROLE_COOPADMIN_DUAL = (1 << 3);
    public static final int ROLE_LEARNER = (1 << 4);
    public static final int ROLE_LEARNER_DUAL = (1 << 5);

    public static final String ROLE_ROOT_NAME = "ROLE_ROOT";
    public static final String ROLE_ROOT_DUAL_NAME = "ROLE_ROOT_DUAL";
    public static final String ROLE_COOPADMIN_NAME = "ROLE_COOPADMIN";
    public static final String ROLE_COOPADMIN_DUAL_NAME = "ROLE_COOPADMIN_DUAL";
    public static final String ROLE_LEARNER_NAME = "ROLE_LEARNER";
    public static final String ROLE_LEARNER_DUAL_NAME = "ROLE_LEARNER_DUAL";

    public static final List<Integer> ROLE_LIST = new ArrayList<>();
    static {
        ROLE_LIST.add(ROLE_ROOT);
        ROLE_LIST.add(ROLE_COOPADMIN);
        ROLE_LIST.add(ROLE_LEARNER);
    }

    public static final HashMap<Integer, GrantedAuthority> ROLE_NAME_MAPPING = new HashMap<>();
    static {
        ROLE_NAME_MAPPING.put(ROLE_ROOT, (GrantedAuthority) () -> ROLE_ROOT_NAME);
        ROLE_NAME_MAPPING.put(ROLE_ROOT_DUAL, (GrantedAuthority) () -> ROLE_ROOT_DUAL_NAME);
        ROLE_NAME_MAPPING.put(ROLE_COOPADMIN, (GrantedAuthority) () -> ROLE_COOPADMIN_NAME);
        ROLE_NAME_MAPPING.put(ROLE_COOPADMIN_DUAL, (GrantedAuthority) () -> ROLE_COOPADMIN_DUAL_NAME);
        ROLE_NAME_MAPPING.put(ROLE_LEARNER, (GrantedAuthority) () -> ROLE_LEARNER_NAME);
        ROLE_NAME_MAPPING.put(ROLE_LEARNER_DUAL, (GrantedAuthority) () -> ROLE_LEARNER_DUAL_NAME);
    }

    public static final HashMap<String, Integer> ROLE_NAME_REV_MAPPING = new HashMap<>();
    static {
        ROLE_NAME_REV_MAPPING.put(ROLE_ROOT_NAME, ROLE_ROOT);
        ROLE_NAME_REV_MAPPING.put(ROLE_ROOT_DUAL_NAME, ROLE_ROOT_DUAL);
        ROLE_NAME_REV_MAPPING.put(ROLE_COOPADMIN_NAME, ROLE_COOPADMIN);
        ROLE_NAME_REV_MAPPING.put(ROLE_COOPADMIN_DUAL_NAME, ROLE_COOPADMIN_DUAL);
        ROLE_NAME_REV_MAPPING.put(ROLE_LEARNER_NAME, ROLE_LEARNER);
        ROLE_NAME_REV_MAPPING.put(ROLE_LEARNER_DUAL_NAME, ROLE_LEARNER_DUAL);
    }

    public static List<GrantedAuthority> grantedAuthoritiesOfRoles(final int roles) {
        List<GrantedAuthority> toRet = new ArrayList<>();
        for (final int role : ROLE_LIST) {
            if ((roles & role) > 0) {
                toRet.add(ROLE_NAME_MAPPING.get(role));
            }
        }
        return toRet;
    }

    public static GrantedAuthority dualGrantedAuthorityOf(final GrantedAuthority grantedAuthority) {
        final int role = ROLE_NAME_REV_MAPPING.get(grantedAuthority.getAuthority());
        final int dualRole = (1 << role);
        return ROLE_NAME_MAPPING.get(dualRole);
    }

    public static int grantedAuthoritiesToRoles(final Collection<? extends GrantedAuthority> grantedAuthorities) {
        int roles = 0;
        for (final GrantedAuthority grantedAuthority : grantedAuthorities) {
            roles += ROLE_NAME_REV_MAPPING.get(grantedAuthority.getAuthority());
        }
        return roles;
    }
}
