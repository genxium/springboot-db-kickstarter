package com.mytrial.app.security;

import com.mytrial.app.models.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j /* See https://projectlombok.org/api/lombok/extern/slf4j/Slf4j.html. */
@Component("databaseAuthenticationSuccessHandler")
public class DatabaseAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();

    private final NoRpcSecurityConfigs noRpcSecurityConfigs;
    private final ExtAuthTokenCodecManager extAuthTokenCodecManager;

    @Autowired
    public DatabaseAuthenticationSuccessHandler(NoRpcSecurityConfigs noRpcSecurityConfigs, ExtAuthTokenCodecManager extAuthTokenCodecManager) {
        this.noRpcSecurityConfigs = noRpcSecurityConfigs;
        this.extAuthTokenCodecManager = extAuthTokenCodecManager;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException {

        String targetUrl = null;
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (null != savedRequest) {
            targetUrl = savedRequest.getRedirectUrl();
            requestCache.removeRequest(request, response);
        } else {
            final String targetUrlParameter = getTargetUrlParameter();
            targetUrl = request.getParameter(targetUrlParameter);
        }

        clearAuthenticationAttributes(request);

        if (null == targetUrl) return;
        final Player loggedInPlayer = (Player) authentication.getDetails();
        final String extAuthToken = extAuthTokenCodecManager.encrypt(loggedInPlayer);
        request.setAttribute(noRpcSecurityConfigs.getCookieName(), extAuthToken);

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam(noRpcSecurityConfigs.getCookieName(), extAuthToken)
                .build()
                .toString();

        log.debug("Redirecting to targetUrl: {}", targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
