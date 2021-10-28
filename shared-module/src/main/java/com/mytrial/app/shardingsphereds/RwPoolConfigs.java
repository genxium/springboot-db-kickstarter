package com.mytrial.app.shardingsphereds;

import com.mytrial.app.preconf.ZkPropsSource;
import com.mytrial.app.preconf.ZkPropsSourceInjector;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component("shardingSphereMsConfigs")
@ConfigurationProperties(prefix = "spring.rwds")
public class RwPoolConfigs {

    @Autowired
    public RwPoolConfigs(ZkPropsSourceInjector zkPropsSourceInjector) {

    }

    @NestedConfigurationProperty // MUST USE when the field requires another layer of @Data to deserialize
    private DataSourceProperties master;

    @NestedConfigurationProperty // MUST USE when the field requires another layer of @Data to deserialize
    private List<DataSourceProperties> slaveList;

    // The static inner class must be "public" here for Lombok to construct.
    @Data
    public static class DataSourceProperties {
        private String username;
        private String password;
        private String jdbcUrl;
        private String driverClassName;
    }
}
