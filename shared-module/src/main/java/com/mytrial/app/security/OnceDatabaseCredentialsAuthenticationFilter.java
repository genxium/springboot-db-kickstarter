package com.mytrial.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.stereotype.Component;

@Component("onceDatabaseCredentialsAuthenticationFilter")
public class OnceDatabaseCredentialsAuthenticationFilter extends DatabaseCredentialsAuthenticationFilter {

    @Autowired
    public OnceDatabaseCredentialsAuthenticationFilter(
            @Qualifier("databaseAuthenticationManager") DatabaseAuthenticationManager databaseAuthenticationManager,
            @Qualifier("databaseSecurityConfigs") DatabaseSecurityConfigs securityConfigs,
            @Qualifier("databaseAuthenticationSuccessHandler") DatabaseAuthenticationSuccessHandler databaseAuthenticationSuccessHandler,
            @Qualifier("databaseAuthenticationFailureHandler") DatabaseAuthenticationFailureHandler databaseAuthenticationFailureHandler
    ) {
        super(databaseAuthenticationManager, new NullRememberMeServices(), securityConfigs, databaseAuthenticationSuccessHandler, databaseAuthenticationFailureHandler);
        setPasswordParameter("confirmPassword");
    }
}
