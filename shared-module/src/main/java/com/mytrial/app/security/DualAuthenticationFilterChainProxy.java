package com.mytrial.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DualAuthenticationFilterChainProxy extends FilterChainProxy {
    /**
     * Not all methods should undergo all filters,
     * - some methods might require a "GrantedAuthority(ROLE_XXX_DUAL)" from a "OncePerRequestFilter" while
     * - others only require a "GrantedAuthority(ROLE_XXX)" from "UsernamePasswordAuthenticationFilter".
     * <p>
     * See how "FilterChainProxy" fits the requirement.
     * - https://spring.io/guides/topicals/spring-security-architecture.
     * - https://docs.spring.io/spring-security/site/docs/3.0.x/reference/security-filter-chain.html
     */

    @Autowired
    public DualAuthenticationFilterChainProxy(
            @Qualifier("databaseCredentialsAuthenticationFilter") DatabaseCredentialsAuthenticationFilter dbCredentialsAuthenticationFilter,
            @Qualifier("onceDatabaseCredentialsAuthenticationFilter") OnceDatabaseCredentialsAuthenticationFilter onceDatabaseCredentialsAuthenticationFilter) {
        super(Arrays.asList(
                new DefaultSecurityFilterChain(
                        new AntPathRequestMatcher("/Dual/**", HttpMethod.POST.name()),
                        dbCredentialsAuthenticationFilter, /** [WARNING] The request will often pass this first filter by "rememberMeServices.autoLogin" */
                        onceDatabaseCredentialsAuthenticationFilter
                ),
                new DefaultSecurityFilterChain(
                        new AntPathRequestMatcher("/**", HttpMethod.POST.name()),
                        dbCredentialsAuthenticationFilter
                )
            )
        );
    }
}
