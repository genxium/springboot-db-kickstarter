package com.mytrial.app.preconf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertySource;

@Slf4j
@Configuration
public class ZkPropsConfigs extends PropertySource<ZkPropertySourceUnderlying> implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Autowired
    public ZkPropsConfigs(ZkPropertySourceUnderlying source) {
        super("ZkPropsConfigs", source);
    }

    @Override
    public Object getProperty(String s) {
        return source.getExportingProps().getProperty(s);
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        configurableApplicationContext.getEnvironment().getPropertySources().addFirst(this);
    }
}