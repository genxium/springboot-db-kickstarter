package com.mytrial.auth.security;

import com.mytrial.app.security.DatabaseRememberMeServices;
import com.mytrial.app.security.DatabaseSecurityConfigs;
import com.mytrial.app.security.DualAuthenticationFilterChainProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@EnableWebSecurity /* Includes "@EnableGlobalAuthentication" & "@Configuration" */
@EnableGlobalMethodSecurity(securedEnabled = true) /* Enables the use of "@Secured" annotation. */
public class ApiRoleBindingImpl extends WebSecurityConfigurerAdapter {

    private final DatabaseSecurityConfigs databaseSecurityConfigs;
    private final DatabaseRememberMeServices dbRememberMeServices;
    private final DualAuthenticationFilterChainProxy dualAuthenticationFilterChainProxy;

    @Autowired
    public ApiRoleBindingImpl(DatabaseSecurityConfigs databaseSecurityConfigs, DatabaseRememberMeServices databaseRememberMeServices, DualAuthenticationFilterChainProxy dualAuthenticationFilterChainProxy) {
        this.databaseSecurityConfigs = databaseSecurityConfigs;
        this.dbRememberMeServices = databaseRememberMeServices;
        this.dualAuthenticationFilterChainProxy = dualAuthenticationFilterChainProxy;
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        /**
         * [WARNING]
         *
         * We'll be mainly using method level auth constraints by "@Secured" annotation, thus NOT specifying many "antMatcher"s here.
         * */

        /**
         * [WARNING]
         *
         * Don't forget to set the "rememberMeServices" for each individual "Filter", they can be different from "the foremost `rememberMeServices` used for `autoLogin`"!
         * */
        http
                .csrf().disable()
                .rememberMe().rememberMeServices(dbRememberMeServices)
                .and()
                .addFilterAt(dualAuthenticationFilterChainProxy, UsernamePasswordAuthenticationFilter.class);
        /** Replace the original "bean `UsernamePasswordAuthenticationFilter`".
         *
         * I've tuned "databaseUsernamePasswordAuthenticationFilter + databaseRememberMeServices" to implement
         * "token validation & saving into persistent storage", WITHOUT the use of an additional
         * ```
         * addFilterBefore(databaseAuthTokenFilter: OncePerRequestFilter, UsernamePasswordAuthenticationFilter.class)
         * ```
         */
    }
}
