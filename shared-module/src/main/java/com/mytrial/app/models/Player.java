package com.mytrial.app.models;

import com.mytrial.app.security.GrantedRolesMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player implements UserDetails {
    @Id
    private int id;
    private String principal; // The unique identifier of a "Player" a.k.a. "username", often serves as the "Authentication.principal" in the "Spring Security Pipeline".
    @Column(name = "salted_password")
    private String saltedPassword;
    @Column(name = "display_name")
    private String displayName;
    private int roles;
    @Column(name = "created_at")
    private Long createdAt;
    @Column(name = "updated_at")
    private Long updatedAt;
    @Column(name = "deleted_at")
    private Long deletedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return GrantedRolesMapper.grantedAuthoritiesOfRoles(getRoles());
    }

    @Override
    public String getPassword() {
        return getSaltedPassword();
    }

    @Override
    public String getUsername() {
        return getPrincipal();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return (null == getDeletedAt());
    }
}
