package com.mytrial.ha.controllers;

import io.grpc.Server;
import com.mytrial.ha.RpcServerClusterManager;
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
    RpcServerClusterManager rpcServerClusterManager;

    @GetMapping(value = "/restartAll")
    public boolean restartAll() throws InterruptedException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        rpcServerClusterManager.restartAll();
        return true;
    }
}

