package com.mytrial.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component("databaseAuthenticationFailureHandler")
public class DatabaseAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private DatabaseSecurityConfigs databaseSecurityConfigs;

    @Autowired
    public DatabaseAuthenticationFailureHandler(
            @Qualifier("databaseSecurityConfigs") DatabaseSecurityConfigs databaseSecurityConfigs,
            @Qualifier("databaseRedirectStrategy") DatabaseRedirectStrategy databaseRedirectStrategy
    ) {
        super(databaseSecurityConfigs.getDefaultAuthEndpoint());
        this.setRedirectStrategy(databaseRedirectStrategy);
        this.databaseSecurityConfigs = databaseSecurityConfigs;
    }
}
