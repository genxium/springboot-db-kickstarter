package com.mytrial.exam.security;

import com.mytrial.app.security.NoRpcRememberMeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Slf4j
public class ApiRoleBindingImpl extends WebSecurityConfigurerAdapter {

    private final NoRpcRememberMeService noRpcRememberMeService;

    @Autowired
    public ApiRoleBindingImpl(NoRpcRememberMeService noRpcRememberMeService) {
        this.noRpcRememberMeService = noRpcRememberMeService;
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .rememberMe().rememberMeServices(noRpcRememberMeService);
    }
}
