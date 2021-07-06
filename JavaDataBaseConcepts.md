# Buzzword to package-prefixes
When talking about "of JDBC", it's referring to these two packages alone
- java.sql
- javax.sql

When talking about "of JTA", it's referring to these two packages alone
- javax.transaction
- javax.transaction.xa

# No assumption on ACID compliance of the chosen DBMS
It's also noticeable that from the very beginning "JTA" is designed for "transaction management solely within JVM", i.e. even when using the non-xa part "javax.transaction.*" there's no assumption of "upstream ACID transaction support", thus the user of "javax.transaction.*" is supposed to implement his/her own commit/rollback methods, either ACID compliant or not (or partial). The xa part "javax.transaction.xa.*" is only providing protocols for proxying the was-two-party transactions to a distributed context.

# Layering
- java.sql.Connection of JDBC 
  - where "SqlSession" is a Mybatis/SpringFramework concept as a counterpart or wrapper of "java.sql.Connection"
- java.sql.DataSource of JDBC 
- javax.transaction.TransactionManager of JTA 
- org.springframework.transaction.support.PlatformTransactionManager+DataSourceTransactionManager of Spring 
  - needs either a reference to "java.sql.DataSource", or one to "SqlSessionFactory"), in case where "DataSourceTransactionManager+AbstractRoutingDataSource(w.r.t. Spring)" is used, there should be a reference to the DataSource

# What for
A "DynamicDataSource" is mostly a data structure that supports
- requesting "TransactionDefinition" and
- returning a corresponding "Connection" of the chosen "DataSource"
