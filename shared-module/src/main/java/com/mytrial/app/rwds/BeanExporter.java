package com.mytrial.app.rwds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Deprecated
@Component("rwPoolBeanExporter")
@EnableConfigurationProperties(RwPoolConfigs.class)
public class BeanExporter {
    private static final Logger logger = LoggerFactory.getLogger(BeanExporter.class);

    private RwPoolConfigs rwPoolConfigs = null;
    private DataSource rwPoolDataSource = null;
    private PlatformTransactionManager rwPoolTransactionManager = null;
    private JdbcTemplate jdbcTemplate = null;

    @Autowired
    public BeanExporter(@Qualifier("rwPoolConfigs") RwPoolConfigs rwPoolConfigs) {
        this.rwPoolConfigs = rwPoolConfigs;
    }

    @Bean("rwPoolDataSource")
    public DataSource getDataSource() {
        if (null == rwPoolDataSource) {
            rwPoolDataSource = new RwPoolRoutingDataSource(rwPoolConfigs);
        }
        return rwPoolDataSource;
    }

    /**
     * To be used by "HttpServerControllers".
     */
    @Bean("rwPoolTransactionManager")
    public PlatformTransactionManager getTransactionManager() {
        if (null == rwPoolTransactionManager) {
            rwPoolTransactionManager = new RwPoolTransactionManager(getDataSource());
        }
        return rwPoolTransactionManager;
    }

    /**
     * To be used by "HttpServerControllers".
     */
    @Bean("rwPoolJdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        if (null == jdbcTemplate) {
            jdbcTemplate = new JdbcTemplate(getDataSource());
        }
        return jdbcTemplate;
    }
}
