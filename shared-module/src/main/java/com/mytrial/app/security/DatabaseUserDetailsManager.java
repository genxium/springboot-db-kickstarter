package com.mytrial.app.security;

import com.mytrial.app.models.Player;
import com.mytrial.app.shardingsphereds.BeanExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DatabaseUserDetailsManager implements UserDetailsManager {
    private BeanExporter beanExporter = null;

    @Autowired
    public DatabaseUserDetailsManager(BeanExporter beanExporter) {
        this.beanExporter = beanExporter;
    }

    @Override
    public void createUser(UserDetails userDetails) {
        long nowGmtMillis = System.currentTimeMillis();
        final String insertQ = "INSERT INTO player(principal, salted_password, roles, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        final int rowsInserted = beanExporter.getJdbcTemplate().update(insertQ, userDetails.getUsername(), userDetails.getPassword(), GrantedRolesMapper.grantedAuthoritiesToRoles(userDetails.getAuthorities()), nowGmtMillis, nowGmtMillis);
        if (1 != rowsInserted) {
            log.warn("`createUser` failed");
        }
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        /**
         * It's intended that this method only updates "player.display_name" when applicable, i.e. called by the logged-in player himself/herself.
         * */
        if (!(userDetails instanceof Player)) return;
        final Player player = (Player) userDetails;
        long nowGmtMillis = System.currentTimeMillis();
        final String updateQ = "UPDATE player SET display_name=?, updated_at=? WHERE principal=?";
        final int rowsUpdated = beanExporter.getJdbcTemplate().update(updateQ, player.getDisplayName(), nowGmtMillis, player.getUsername());
        if (1 != rowsUpdated) {
            log.warn("`updateUser` failed");
        }
    }

    @Override
    public void deleteUser(String principal) {
        /**
         * It's intended that only "soft-delete" is implemented here, the "hard-delete" will be taken care of elsewhere, e.g. a standalone scheduler.
         * */
        long nowGmtMillis = System.currentTimeMillis();
        final String updateQ = "UPDATE player SET deleted_at=?, updated_at=? WHERE principal=? AND deleted_at IS NULL";
        final int rowsUpdated = beanExporter.getJdbcTemplate().update(updateQ, nowGmtMillis, nowGmtMillis, principal);
        if (1 != rowsUpdated) {
            log.warn("`updateUser` failed");
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        /**
         * [WARNING]
         *
         * The default strategy for "SecurityContextHolder" is "MODE_THREADLOCAL".
         * */
        final Authentication currentSessionAuth = SecurityContextHolder.getContext().getAuthentication();
        if (null == currentSessionAuth || false == currentSessionAuth.isAuthenticated()) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current player.");
        }

        final String principal = (String) currentSessionAuth.getPrincipal();

        /**
         * [WARNING]
         *
         * No need to check "oldPassword" in the where-clause, because the player never has his/her "oldPassword" re-encrypted/re-salted!
         * */
        final String updateQ = "UPDATE player SET salted_password=?, updated_at=? WHERE principal=? AND deleted_at IS NULL";

        log.debug("Changing password for player '" + principal + "'");

        final long nowGmtMillis = System.currentTimeMillis();
        final int rowsUpdated = beanExporter.getJdbcTemplate().update(updateQ, newPassword, nowGmtMillis, principal);
        if (1 == rowsUpdated) {
            final Player player = (Player)loadUserByUsername(principal);
            final UsernamePasswordAuthenticationToken newSessionAuth = new UsernamePasswordAuthenticationToken(player, newPassword, player.getAuthorities());
            newSessionAuth.setDetails(currentSessionAuth.getDetails());
            SecurityContextHolder.getContext().setAuthentication(newSessionAuth);
        }
    }

    @Override
    public boolean userExists(String principal) {
        final String q = "SELECT COUNT(id) FROM player WHERE principal=? AND deleted_at IS NULL";
        final int count = beanExporter.getJdbcTemplate().queryForObject(q, new Object[]{principal}, Integer.class);
        return (1 == count);
    }

    @Override
    public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
        final String q = "SELECT * FROM player WHERE principal=? AND deleted_at IS NULL LIMIT 1";
        return beanExporter.getJdbcTemplate().queryForObject(q, new Object[]{principal}, Player.class);
    }
}
