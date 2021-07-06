package com.mytrial.app.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component("noRpcAuthenticationSuccessHandler")
public class NoRpcAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final NoRpcSecurityConfigs noRpcSecurityConfigs;

    @Autowired
    public NoRpcAuthenticationSuccessHandler(NoRpcSecurityConfigs noRpcSecurityConfigs) {
        this.noRpcSecurityConfigs = noRpcSecurityConfigs;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException {


    }
}
