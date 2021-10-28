package com.mytrial.app.preconf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Slf4j
@Component
@ConfigurationProperties
public class ZkPropsSourceUnderlying implements Watcher {

    private final Properties exportingProps = new Properties();

    // [WARNING] Specify "@Autowired" here to ensure that constructor is called AFTER "@Value"s are injected
    @Autowired
    public ZkPropsSourceUnderlying(@Value("${zk.endpoint:\"localhost:2181\"}") String endpoint, @Value("${zk.confpath:/}") String confpath, @Value("${zk.sessionTimeout:1000}") int sessionTimeout) {
        try {
            exportingProps.put("foo", "bar"); // For testing init order.
            final ZooKeeper zookeeperClient = new ZooKeeper(endpoint, sessionTimeout, this);
            final ObjectMapper objectMapper = new ObjectMapper();
            final JsonNode detailsNode = objectMapper.readTree(zookeeperClient.getData(confpath, false, null));
            /*
            The jsonString should be like

            {
                "name": "ExamConfigs",
                "details": "{"spring.rwds.master.username":"root", "spring.rwds.master.driverClassName":"com.mysql.cj.jdbc.Driver", ... }"
            }
             */

            detailsNode.fields().forEachRemaining((r) -> {
                String key = r.getKey();
                String val = r.getValue().textValue(); // Type is guaranteed a string
                exportingProps.setProperty(key, val);
            });

            zookeeperClient.close();
        } catch (IOException | KeeperException | InterruptedException e) {
            log.error("Error when connecting or reading zookeeper at {}", endpoint, e);
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }

    public Properties getExportingProps() {
        return exportingProps;
    }
}
