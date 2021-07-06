package com.mytrial.app.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class NoRpcRememberMeService extends AbstractRememberMeServices {
    private NoRpcSecurityConfigs noRpcSecurityConfigs;

    @Autowired
    public NoRpcRememberMeService(
            @Qualifier("noRpcSecurityConfigs") NoRpcSecurityConfigs securityConfigs,
            NoRpcUserDetailsManager userDetailsService) {
        super(securityConfigs.getRememberMeKey(), userDetailsService);
        this.noRpcSecurityConfigs = securityConfigs;
        setCookieName(noRpcSecurityConfigs.getCookieName());
        setTokenValiditySeconds(noRpcSecurityConfigs.getRememberMeLifeSeconds());
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        boolean isType2Autologin = (boolean) request.getAttribute("TYPE_2_AUTOLOGIN");

        if (!isType2Autologin) {
            /**
             * Do nothing if it's NOT "TYPE_2_AUTOLOGIN"!
             * */
            return;
        }

        /**
         * The correct cookie has been set till now, just peel off the queryParam "ExtAuthToken".
         * */
        final String currentUrl = request.getRequestURL().toString();
        try {
            final URI currentUri = new URI(currentUrl);
            final UriComponentsBuilder targetUriComponentsBuilder = UriComponentsBuilder
                    .newInstance()
                    .scheme(currentUri.getScheme())
                    .schemeSpecificPart(currentUri.getSchemeSpecificPart())
                    .userInfo(currentUri.getUserInfo())
                    .host(currentUri.getHost())
                    .port(currentUri.getPort())
                    .path(currentUri.getPath())
                    .fragment(currentUri.getFragment());

            final UriComponents currentUriComponents = UriComponentsBuilder
                    .fromUri(currentUri).build();

            final MultiValueMap<String, String> args = new LinkedMultiValueMap<>(currentUriComponents.getQueryParams());
            args.remove(noRpcSecurityConfigs.getCookieName());

            final UriComponents targetUri = targetUriComponentsBuilder.queryParams(args).build();
            final String targetUrl = targetUri.toString();
            log.debug("Redirecting to targetUrl after TYPE_2_AUTOLOGIN: {}", targetUrl);
            response.sendRedirect(targetUrl);
        } catch (URISyntaxException | IOException e) {
            log.warn("Cannot parse the currentUrl into current Uri", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException ioException) {
                log.error("Not even able to send `500 SC_INTERNAL_SERVER_ERROR`", ioException);
            }
        }
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) throws RememberMeAuthenticationException, UsernameNotFoundException {
        return null;
    }

    @Override
    protected String extractRememberMeCookie(HttpServletRequest request) {
        /**
         * This method is used by "AbstractRememberMeServices.autoLogin".
         * */
        final String rememberMeKeyInCookie = super.extractRememberMeCookie(request);
        if (null != rememberMeKeyInCookie) return rememberMeKeyInCookie;

        // Will be respected by "DatabaseAuthenticationSuccessHandler.onAuthenticationSuccess".
        request.setAttribute("TYPE_2_AUTOLOGIN", true);
        return request.getParameter(getCookieName());
    }
}
