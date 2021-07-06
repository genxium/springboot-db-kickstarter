package com.mytrial.app.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("noRpcSecurityConfigs")
@ConfigurationProperties(prefix = "spring.norpc-security")
public class NoRpcSecurityConfigs {
    private String cookieName;
    private String rememberMeKey;
    private int rememberMeLifeSeconds;
    private String jasyptSymmetricSecret;
}
