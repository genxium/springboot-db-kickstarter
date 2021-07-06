package com.mytrial.app.rwds;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class RwPoolRoutingDataSource extends AbstractRoutingDataSource {
    private final Map<Object, Object> dataSources = new HashMap<>();

    public RwPoolRoutingDataSource(RwPoolConfigs rwPoolDataSourceConfigs) {
        RwPoolConfigs.DataSourceProperties masterProperties = rwPoolDataSourceConfigs.getMaster();
        final DataSource master = DataSourceBuilder.create()
                .driverClassName(masterProperties.getDriverClassName())
                .url(masterProperties.getJdbcUrl())
                .username(masterProperties.getUsername())
                .password(masterProperties.getPassword())
                .type(HikariDataSource.class)
                .build();

        List<DataSource> slaves = new ArrayList<>();
        List<RwPoolConfigs.DataSourceProperties> slavePropertiesList = rwPoolDataSourceConfigs.getSlaveList();
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

        init(master, slaves);
    }

    private void init(final DataSource master, final List<DataSource> slaves) {
        dataSources.put(0, master);
        int slaveKey = 1;
        for (DataSource slave : slaves) {
            dataSources.put(slaveKey++, slave);
        }
        super.setTargetDataSources(dataSources);
        super.setDefaultTargetDataSource(master);
    }

    private Object randomReadOnlyKey() {
        final int slavesCount = dataSources.size() - 1;
        return (int)(Math.random()*(slavesCount - 1) + 1);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
                ?
                randomReadOnlyKey()
                :
                0;
    }
}
