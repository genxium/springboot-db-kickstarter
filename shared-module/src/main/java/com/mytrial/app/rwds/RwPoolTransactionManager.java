package com.mytrial.app.rwds;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Deprecated
public class RwPoolTransactionManager extends DataSourceTransactionManager {
    RwPoolTransactionManager(DataSource ds) {
        super(ds);
    }
}
