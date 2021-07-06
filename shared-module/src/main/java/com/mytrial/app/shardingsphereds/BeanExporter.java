package com.mytrial.app.shardingsphereds;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

@Component("shardingSphereBeanExporter")
@EnableConfigurationProperties(RwPoolConfigs.class)
public class BeanExporter {
    private static final Logger logger = LoggerFactory.getLogger(BeanExporter.class);

    private RwPoolConfigs rwPoolConfigs = null;
    private DataSource rwPoolDataSource = null;
    private PlatformTransactionManager rwPoolTransactionManager = null;
    private JdbcTemplate jdbcTemplate = null;

    @Autowired
    public BeanExporter(@Qualifier("shardingSphereMsConfigs") RwPoolConfigs rwPoolConfigs) {
        this.rwPoolConfigs = rwPoolConfigs;
    }

    @Bean("shardingsphereMsDataSource")
    public DataSource getDataSource() {
        if (null == rwPoolDataSource) {
            try {
                final RwPoolConfigs.DataSourceProperties masterProperties = rwPoolConfigs.getMaster();
                final DataSource master = DataSourceBuilder.create()
                        .driverClassName(masterProperties.getDriverClassName())
                        .url(masterProperties.getJdbcUrl())
                        .username(masterProperties.getUsername())
                        .password(masterProperties.getPassword())
                        .type(HikariDataSource.class)
                        .build();

                final List<DataSource> slaves = new ArrayList<>();
                final List<RwPoolConfigs.DataSourceProperties> slavePropertiesList = rwPoolConfigs.getSlaveList();
                if (null != slavePropertiesList && !slavePropertiesList.isEmpty()) {
                    for (final RwPoolConfigs.DataSourceProperties slaveProperties : slavePropertiesList) {
                        final DataSource slave = DataSourceBuilder.create()
                                .driverClassName(slaveProperties.getDriverClassName())
                                .url(slaveProperties.getJdbcUrl())
                                .username(slaveProperties.getUsername())
                                .password(slaveProperties.getPassword())
                                .type(HikariDataSource.class)
                                .build();
                        slaves.add(slave);
                    }
                }

                final Map<String, DataSource> dataSourceMap = new HashMap<>();
                final List<String> slaveNames = new ArrayList<>();
                final String masterName = "ds_master";
                dataSourceMap.put(masterName, master);
                int slaveKey = 1;
                for (DataSource slave : slaves) {
                    final String slaveName = "ds_slave_" + slaveKey;
                    dataSourceMap.put(slaveName, slave);
                    slaveNames.add(slaveName);
                }

                // Configure read-write split rule
                final MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration("ds_master_slave", masterName, slaveNames);

                rwPoolDataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, masterSlaveRuleConfig, new Properties());
            } catch (SQLException ex) {
                logger.error("DataSource is not constructed!", ex);
            }
        }
        return rwPoolDataSource;
    }

    /**
     * To be used by "HttpServerControllers".
     */
    @Bean("shardingsphereTransactionManager")
    public PlatformTransactionManager getTransactionManager() {
        if (null == rwPoolTransactionManager) {
            rwPoolTransactionManager = new RwPoolTransactionManager(getDataSource());
        }
        return rwPoolTransactionManager;
    }

    /**
     * To be used by "HttpServerControllers".
     */
    @Bean("shardingsphereJdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        if (null == jdbcTemplate) {
            jdbcTemplate = new JdbcTemplate(getDataSource());
        }
        return jdbcTemplate;
    }
}
