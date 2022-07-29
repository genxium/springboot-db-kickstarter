package com.mytrial.ha.controllers;

import io.grpc.Server;
import com.mytrial.ha.RpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class Console {
    @Autowired
    RpcServer rpcServer;

    @GetMapping(value = "/restart")
    void restart() throws InterruptedException, IOException {
        // TODO: Rollback if any of the grpc server restart fails.
        List<Server> haServers = rpcServer.getHaServers();
        for (Server haServer : haServers) {
            // TODO: Reload the patched folder (of classes and resources) only for this single "haServer" because it starts.
            haServer.shutdown();
            haServer.awaitTermination();
            haServer.start();
        }
    }
}

