package com.mytrial.ha;

import io.grpc.Server;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.inprocess.InProcessSocketAddress;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Component
public class RpcServerClusterManager {
    protected List<Server> haServers;

    public RpcServerClusterManager(@Value("${server.ha.count}") int serverHaCnt) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        haServers = new ArrayList<>();
        for (int i = 0; i < serverHaCnt; i += 1) {
            final Server server = InProcessServerBuilder
                    .forAddress(new InProcessSocketAddress(String.format("grpc-s-%d", i)))
                    .addService(new RpcServerImpl(i))
                    .build()
                    .start();
            haServers.add(server);
        }
    }

    public boolean restartSpecifiedServer(int i) throws InterruptedException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        final Server oldHaServer = haServers.get(i);
        oldHaServer.shutdown().awaitTermination();
        final Server newHaServer = InProcessServerBuilder
                .forAddress(new InProcessSocketAddress(String.format("grpc-s-%d", i)))
                .addService(new RpcServerImpl(i))
                .build()
                .start(); // There's no way to call "start" again on "oldHaServer"
        haServers.set(i, newHaServer);
        return true;
    }

    public boolean restartAll() throws IOException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // TODO: Rollback if any of the grpc server restart fails.
        for (int i = 0; i < haServers.size(); i += 1) {
            restartSpecifiedServer(i);
        }
        return true;
    }
}
