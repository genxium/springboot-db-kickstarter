package com.mytrial.app.preconf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration("zkPropsSourceInjector")
public class ZkPropsSourceInjector {
    @Autowired
    public ZkPropsSourceInjector(@Qualifier("zkPropsSource") ZkPropsSource zkPropsSource, ConfigurableEnvironment env) {
        env.getPropertySources().addFirst(zkPropsSource);
    }
}
