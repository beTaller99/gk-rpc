package com.asher.gkrpc.example;

import com.asher.gkrpc.proto.server.RpcServer;

/**
 * @className: Server
 * @Description: TODO
 * @version: openjdk-17.0.2
 * @author: asher
 * @date: 2022/5/3 18:13
 */
public class Server {
    public static void main(String[] args) {
        RpcServer server = new RpcServer();
        server.register(CalService.class, new CalServiceImpl());
        server.start();
    }
}
