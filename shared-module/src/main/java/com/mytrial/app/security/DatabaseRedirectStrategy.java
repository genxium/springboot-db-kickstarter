package com.mytrial.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component("databaseRedirectStrategy")
public class DatabaseRedirectStrategy implements RedirectStrategy {
    private DatabaseSecurityConfigs databaseSecurityConfigs;

    @Autowired
    public DatabaseRedirectStrategy(@Qualifier("databaseSecurityConfigs") DatabaseSecurityConfigs databaseSecurityConfigs) {
        this.databaseSecurityConfigs = databaseSecurityConfigs;
    }

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        final String reqUrl = request.getRequestURL().toString();
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam(databaseSecurityConfigs.getTargetUrlParameter(), URLEncoder.encode(reqUrl, "UTF-8").replaceAll("\\+", "%20"));
        final String effectiveUrl = builder.build().toString();
        final String encodedEffectiveUrl = response.encodeURL(effectiveUrl);
        response.sendRedirect(encodedEffectiveUrl);
    }
}
