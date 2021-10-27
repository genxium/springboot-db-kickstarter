package com.mytrial.app.shardingsphereds;

import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component("shardingSphereMsConfigs")
@ConfigurationProperties(prefix = "spring.rwds")
@AutoConfigureOrder(value = 1)
public class RwPoolConfigs {
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
