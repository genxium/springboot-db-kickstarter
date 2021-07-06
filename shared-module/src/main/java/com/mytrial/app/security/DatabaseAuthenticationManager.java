package com.mytrial.app.security;

import com.mytrial.app.models.Player;
import com.mytrial.app.shardingsphereds.BeanExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import static com.mytrial.app.security.GrantedRolesMapper.ROLE_NONE;

@Slf4j
@Primary
@Component("databaseAuthenticationManager")
public class DatabaseAuthenticationManager implements AuthenticationManager {
    /**
     * [WARNING]
     *
     * Though by the time of writing I don't find it necessary for "createNewToken/updateToken/removeUserTokens/getTokenForSeries" to support a "Transaction reference" as input, it's possible that we add such support in the future.
     **/

    private final BeanExporter beanExporter;
    private final GrantedRolesMapper grantedRolesMapper;
    private final DatabasePasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseAuthenticationManager(BeanExporter beanExporter, GrantedRolesMapper grantedRolesMapper, DatabasePasswordEncoder passwordEncoder) {
        this.beanExporter = beanExporter;
        this.grantedRolesMapper = grantedRolesMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        /**
         * The method "AuthenticationManager.authenticate(...)" is used by "`AbstractAuthenticationProcessingFilter.doFilter(...)` of bean `DatabaseCredentialsAuthenticationFilter`" to execute ACTUAL COMPARISON between "inputPrincipal & inputPassword" and "storedPrincipal & storedPassword".
         *
         * Should return "null" upon failed comparisons.
         **/
        final String inputPrincipal = (String) authentication.getPrincipal();
        final String inputPassword = (String) authentication.getCredentials();

        final String q = "SELECT * FROM player WHERE principal=? AND deleted_at IS NULL LIMIT 1";
        final Player player = new Player();
        beanExporter.getJdbcTemplate().query(q, new Object[]{inputPrincipal}, (rs) -> {
            player.setId(rs.getInt("id"));
            player.setSaltedPassword(rs.getString("salted_password"));
            player.setPrincipal(rs.getString("principal"));
            player.setDisplayName(rs.getString("display_name"));
            player.setRoles(rs.getInt("roles"));
        });
        if (ROLE_NONE == player.getRoles()) {
            return null;
        }

        if (!isInputPasswordValid(inputPassword, player.getSaltedPassword())) {
            return null;
        }

        /**
         * The returning "`Authentication` instance" will be in turn processed by "rememberMeServices". See "AbstractAuthenticationProcessingFilter.doFilter(...)" then "AbstractAuthenticationProcessingFilter.successfulAuthentication(...)" for details.
         * */
        final UsernamePasswordAuthenticationToken toRet = new UsernamePasswordAuthenticationToken(inputPrincipal, player.getSaltedPassword(), GrantedRolesMapper.grantedAuthoritiesOfRoles(player.getRoles()));
        toRet.setDetails(player);
        return toRet;
    }

    private boolean isInputPasswordValid(final String inputPassword, final String storedPassword) {
        return passwordEncoder.matches(inputPassword, storedPassword);
    }
}
