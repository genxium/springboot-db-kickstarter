package com.mytrial.app.preconf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;

@Slf4j
@Data
@Configuration
@AutoConfigureOrder(value = 0)
public class ZkPropsConfigs implements Watcher {
    @Value("${zk.confpath:/}")
    private String confpath; // The path of the JSONString you want to parse into properties

    @Value("${zk.endpoint:\"localhost:2181\"}")
    private String endpoint;

    @Value("${zk.sessionTimeout:3000}")
    private int sessionTimeout;

    private Properties exportingProps;

    public ZkPropsConfigs() {
        try {
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
        // Deliberately left empty.
    }
}