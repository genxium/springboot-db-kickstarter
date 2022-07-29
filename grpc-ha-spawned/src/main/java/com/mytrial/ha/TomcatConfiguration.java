package com.mytrial.ha;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfiguration implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        final TomcatConnectorCustomizer portReuseCustomizer = connector -> {
            // Reference https://tomcat.apache.org/tomcat-9.0-doc/config/http.html, kindly note that SpringBoot 2.3.4-RELEASE uses Tomcat-embedded-core 9.0.38
            connector.setProperty("socket.soReuseAddress", "true"); // Set but not actually used here, because this setting still couldn't circumvent the "port in use" error when the gRPC server bound to the same port is running.
        };
        factory.addConnectorCustomizers(portReuseCustomizer);
    }
}