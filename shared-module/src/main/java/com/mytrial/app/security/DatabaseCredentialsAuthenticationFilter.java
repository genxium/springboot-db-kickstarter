package com.mytrial.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCredentialsAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    public DatabaseCredentialsAuthenticationFilter(
            DatabaseAuthenticationManager databaseAuthenticationManager,
            @Qualifier("databaseRememberMeServices") DatabaseRememberMeServices databaseRememberMeServices,
            @Qualifier("databaseSecurityConfigs") DatabaseSecurityConfigs securityConfigs,
            @Qualifier("databaseAuthenticationSuccessHandler") DatabaseAuthenticationSuccessHandler databaseAuthenticationSuccessHandler,
            @Qualifier("databaseAuthenticationFailureHandler") DatabaseAuthenticationFailureHandler databaseAuthenticationFailureHandler
    ) {
        this(databaseAuthenticationManager, (RememberMeServices) databaseRememberMeServices, securityConfigs, databaseAuthenticationSuccessHandler, databaseAuthenticationFailureHandler);
    }

    public DatabaseCredentialsAuthenticationFilter(
            DatabaseAuthenticationManager databaseAuthenticationManager,
            RememberMeServices rememberMeServices,
            DatabaseSecurityConfigs securityConfigs,
            SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler,
            SimpleUrlAuthenticationFailureHandler authenticationFailureHandler
    ) {
        super();
        authenticationSuccessHandler.setTargetUrlParameter(securityConfigs.getTargetUrlParameter());
        this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
        this.setRememberMeServices(rememberMeServices);
        this.setAuthenticationManager(databaseAuthenticationManager);
        this.setUsernameParameter(securityConfigs.getUsernameParameter());
        /**
         * [WARNING]
         *
         * For authentication failures, we shall just let the "`Authentication` typed object being null or with no-GrantedAuthority" to pass on and trigger "ExceptionTranslationFilter.handleSpringSecurityException(...)", often at the end of "FilterChainProxy#VirtualFilterChain.additionalFilters", to finally mark the "savedRequest(i.e. intercepted request)" with a "JSESSIONID" for later recovery on the server-side.
         *
         * As a counterpart of the "JSESSIONID keyed savedRequest", the "AuthenticationSuccessHandler" is by default "SavedRequestAwareAuthenticationSuccessHandler", which would try to find a "savedRequest w.r.t. the JSESSIONID" and extract a "targetUrl(to redirect)" from it (yet WON'T re-execute the "savedRequest").
         *
         * The traditional web-specific version of "SavedRequestAwareAuthenticationSuccessHandler" is a simple "302 /path/to/intercepted/page", which is also implemented when the current "request" explicitly specifies the "targetUrlParameter" (i.e. even without a "JSESSIONID").
        */
    }
}
