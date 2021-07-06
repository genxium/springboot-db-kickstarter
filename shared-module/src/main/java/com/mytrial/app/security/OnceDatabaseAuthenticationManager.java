package com.mytrial.app.security;

import com.mytrial.app.models.Player;
import com.mytrial.app.shardingsphereds.BeanExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component("onceDatabaseAuthenticationManager")
public class OnceDatabaseAuthenticationManager extends DatabaseAuthenticationManager {

    private BeanExporter beanExporter;
    private GrantedRolesMapper grantedRolesMapper;
    private DatabasePasswordEncoder passwordEncoder;

    @Autowired
    public OnceDatabaseAuthenticationManager(BeanExporter beanExporter, GrantedRolesMapper grantedRolesMapper, DatabasePasswordEncoder passwordEncoder) {
        super(beanExporter, grantedRolesMapper, passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final Authentication reAuthentication = super.authenticate(authentication);
        if (null == reAuthentication || !reAuthentication.isAuthenticated()) {
            return null;
        }

        final Player player = (Player) reAuthentication.getDetails();
        final List<GrantedAuthority> authorities = new LinkedList<>();
        for (final GrantedAuthority grantedAuthority : reAuthentication.getAuthorities()) {
            authorities.add(grantedAuthority);
            authorities.add(GrantedRolesMapper.dualGrantedAuthorityOf(grantedAuthority));
        }

        final UsernamePasswordAuthenticationToken newAuthenticationToken = new UsernamePasswordAuthenticationToken(player.getPrincipal(), player.getSaltedPassword(), authorities);
        newAuthenticationToken.setDetails(player);
        return newAuthenticationToken;
    }
}
