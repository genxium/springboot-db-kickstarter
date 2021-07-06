package com.mytrial.app.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("databaseSecurityConfigs")
@ConfigurationProperties(prefix = "spring.security")
public class DatabaseSecurityConfigs {
    private String cookieName;
    private String usernameParameter;
    private String rememberMeKey;
    private int rememberMeLifeSeconds;
    private String targetUrlParameter;
    private String defaultAuthEndpoint;
}
