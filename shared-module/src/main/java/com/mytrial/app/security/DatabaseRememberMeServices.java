package com.mytrial.app.security;

import com.mytrial.app.shardingsphereds.BeanExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;

@Component("databaseRememberMeServices")
public class DatabaseRememberMeServices extends PersistentTokenBasedRememberMeServices {
    /**
     * Deliberately using a customized bean "databaseRememberMeServices", instead of leaving the "RememberMeConfigurer" instance to build an instance of "RememberMeServices" by "RememberMeConfigurer.getRememberMeServices()". The key reason is that we need "databaseRememberMeServices.onLoginSuccess(...)" to create a new "PersistentRememberMeToken" with a ".series" against all the other ones in the database.
     *
     * The "RememberMeServices.autoLogin()" method is called by "RememberMeAuthenticationFilter" whenever the "SecurityContextHolder" does not contain an "Authentication" after several "filterChainProxy.additionalFilters". Breakpoint at the constructor of "FilterChainProxy#VirtualFilterChain(...)" to see a list of "originalFilters & additionalFilters", mind the order.
     *
     * See https://docs.spring.io/spring-security/site/docs/3.0.x/reference/remember-me.html#remember-me-persistent-token for details.
     **/

    private final BeanExporter beanExporter;
    private final DatabaseTokenRepository databaseAuthTokenRepository;
    private final DatabaseSecurityConfigs databaseSecurityConfigs;
    private final NoRpcSecurityConfigs noRpcSecurityConfigs;

    @Autowired
    public DatabaseRememberMeServices(
            BeanExporter beanExporter,
            DatabaseUserDetailsManager userDetailsService,
            DatabaseTokenRepository tokenRepository,
            @Qualifier("databaseSecurityConfigs") DatabaseSecurityConfigs dbSecurityConfigs,
            @Qualifier("noRpcSecurityConfigs") NoRpcSecurityConfigs noRpcSecurityConfigs
    ) {
        super(dbSecurityConfigs.getRememberMeKey(), userDetailsService, tokenRepository);
        this.beanExporter = beanExporter;
        this.databaseAuthTokenRepository = tokenRepository;
        this.databaseSecurityConfigs = dbSecurityConfigs;
        this.noRpcSecurityConfigs = noRpcSecurityConfigs;
        setCookieName(databaseSecurityConfigs.getCookieName());
        setTokenValiditySeconds(databaseSecurityConfigs.getRememberMeLifeSeconds());
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request,
                                  HttpServletResponse response, Authentication successfulAuthentication) {
        final String principal = (String) successfulAuthentication.getPrincipal();

        logger.debug("Creating new persistent login for player " + principal);

        final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        final PlatformTransactionManager txManager = beanExporter.getTransactionManager();
        final TransactionStatus status = txManager.getTransaction(def);
        try {
            final String q = "SELECT UUID()";
            final String series = beanExporter.getJdbcTemplate().queryForObject(q, null, String.class);
            final PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(principal, series, generateTokenData(), new Date());
            databaseAuthTokenRepository.createNewToken(persistentToken);
            txManager.commit(status);

            final Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, getTokenValiditySeconds());
            final String[] tokens = new String[] { persistentToken.getSeries(), persistentToken.getTokenValue() };

            /**
            * [WARNING] A play might not choose "remember-me" when trying to login, don't generate "extAuthToken" in "XxxRememberMeService"!
            * */
            setCookie(tokens, getTokenValiditySeconds(), request, response);
        } catch (Exception e) {
            txManager.rollback(status);
            logger.error("Failed to save persistent token ", e);
        }
    }
}
