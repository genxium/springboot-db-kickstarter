package com.mytrial.app.shardingsphereds;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

public class RwPoolTransactionManager extends DataSourceTransactionManager {
    RwPoolTransactionManager(DataSource ds) {
        super(ds);
    }
}
