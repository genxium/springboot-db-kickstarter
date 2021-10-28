package com.mytrial.app.preconf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

@Slf4j
@Component("zkPropsSource")
public class ZkPropsSource extends PropertySource<ZkPropsSourceUnderlying> {

    @Autowired
    public ZkPropsSource(ZkPropsSourceUnderlying source) {
        super("ZkPropsConfigs", source);
    }

    @Override
    public Object getProperty(String s) {
        return source.getExportingProps().getProperty(s);
    }
}