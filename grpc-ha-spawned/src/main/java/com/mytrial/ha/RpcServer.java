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
@Component
@Data
public class RpcServer {
    protected List<Server> haServers;

    public RpcServer(@Value("${server.ha.count}") int serverHaCnt) throws IOException {
        haServers = new ArrayList<>();
        for (int i = 0; i < serverHaCnt; i += 1) {
            final Server server = InProcessServerBuilder
                    .forAddress(new InProcessSocketAddress(String.format("grpc-s-%d", i)))
                    .build()
                    .start();
            haServers.add(server);
        }
    }
}
