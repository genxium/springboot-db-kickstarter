It's recommended to use [the scripts in this folder](https://github.com/genxium/Ubuntu14InitScripts/tree/master/database/mysql/docker-innodb-cluster) to initialize a "master-slave" MySQL InnoDB Cluster by Docker. This project in fact works for a single MySQL instance thus don't bother if you have difficulties setting up the cluster.  

Although it's not very difficult to switch to Postgres for this project, if one wants to use "Postgres with XA enabled", the instances should be started with "PreparedTransaction"s, e.g. as follows.
```
pg_ctl start -D /path/to/pg_data_1 -l /path/to/pg_log_1 -o "-p 5401 -c max_prepared_transactions=64"

pg_ctl start -D /path/to/pg_data_2 -l /path/to/pg_log_2 -o "-p 5402 -c max_prepared_transactions=64"
```

