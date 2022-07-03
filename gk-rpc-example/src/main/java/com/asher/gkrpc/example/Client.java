package com.asher.gkrpc.example;

import com.asher.gkrpc.client.RpcClient;

/**
 * @className: Client
 * @Description: TODO
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/5/3 18:13
 */
public class Client {
    public static void main(String[] args) {
        RpcClient client = new RpcClient();
        CalService service = client.getProxy(CalService.class);

        //当我调用了代理类的add方法的时候，invoke里面的
        int add = service.add(1, 2);
        int minus = service.minus(1, 2);
        System.out.println("add : " + add);
        System.out.println("minus" + minus);
    }
}

