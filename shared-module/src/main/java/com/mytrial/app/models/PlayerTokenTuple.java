package com.mytrial.app.models;

import com.mytrial.app.security.GrantedRolesMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Builder
@AllArgsConstructor
@Value
public class PlayerTokenTuple implements UserDetails, Serializable {
    private String principal;
    private Integer roles;
    private Long createdAt; // GMT+0 milliseconds

    @Override
    public String toString() {
        return principal + ";" + roles + ";" +  createdAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return GrantedRolesMapper.grantedAuthoritiesOfRoles(getRoles());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return principal;
    }

    @Override
    public boolean isAccountNonExpired() {
        /*
         * Expiry will be checked by "NoRpcRememberMeService" with a "DatabaseSecurityConfigs".
         * */
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        /*
         * Always deemed "non-locked" if accessed by this "PlayerTokenTuple" class.
         * */
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        /*
         * Always deemed "non-expired" if accessed by this "PlayerTokenTuple" class.
         * */
        return true;
    }

    @Override
    public boolean isEnabled() {
        /*
        * Always deemed "enabled" if accessed by this "PlayerTokenTuple" class.
        * */
        return true;
    }
}
