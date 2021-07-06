package com.mytrial.app.security;

import com.mytrial.app.models.PlayerLoginCache;
import com.mytrial.app.shardingsphereds.BeanExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Slf4j
@Component("databaseTokenRepository")
public class DatabaseTokenRepository extends JdbcTokenRepositoryImpl {
    /**
     * [WARNING]
     *
     * Though by the time of writing I don't find it necessary for "createNewToken/updateToken/removeUserTokens/getTokenForSeries" to support a "Transaction reference" as input, it's possible that we add such support in the future.
     **/
    private final BeanExporter beanExporter;

    @Autowired
    public DatabaseTokenRepository(BeanExporter beanExporter) {
        this.beanExporter = beanExporter;
        this.setCreateTableOnStartup(false);
        this.setJdbcTemplate(beanExporter.getJdbcTemplate());
    }

    @Override
    public void createNewToken(PersistentRememberMeToken persistentRememberMeToken) {
        long refGmtMillis = persistentRememberMeToken.getDate().getTime();
        final String insertQ = "INSERT INTO player_login_cache(series, player_principal, int_auth_token, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        final int rowsInserted = getJdbcTemplate().update(insertQ, persistentRememberMeToken.getSeries(), persistentRememberMeToken.getUsername(), persistentRememberMeToken.getTokenValue(), refGmtMillis, refGmtMillis);
        if (1 != rowsInserted) {
            log.info("Login record not inserted for " + persistentRememberMeToken);
        }
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        final String updateQ = "UPDATE player_login_cache set int_auth_token = ?, updated_at = ? where series = ?";
        getJdbcTemplate().update(updateQ, tokenValue, lastUsed.getTime(), series);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {
        final String q = "SELECT * FROM player_login_cache WHERE series=? LIMIT 1";
        final PlayerLoginCache login = getJdbcTemplate().queryForObject(q, new Object[]{series}, PlayerLoginCache.class);
        if (null == login) {
            return null;
        }
        return new PersistentRememberMeToken(login.getPlayerPrincipal(), login.getSeries(), login.getIntAuthToken(), Date.from(Instant.ofEpochMilli(login.getUpdatedAt())));
    }

    @Override
    public void removeUserTokens(String principal) {
        final String deleteQ = "DELETE FROM player_login_cache WHERE player_principal=? LIMIT 1";
        getJdbcTemplate().update(deleteQ, principal);
    }
}
