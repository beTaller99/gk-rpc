package com.asher.gkrpc.transport;

import com.asher.gkrpc.proto.Peer;

import java.io.InputStream;

/**
 * @className: TransportClient
 * @Description: TODO
 * @version: openjdk-17.0.2
 * @author: asher
 * @date: 2022/4/9 19:52
 */

/*
* 在RPC场景下client的使用场景
* 作为client，首先
* 1.创建链接
* 2.发送数据，并且等待响应
* 3.关闭连接
* */
public interface TransportClient {
    void connect(Peer peer);

    //第二步是写数据，因为写完数据之后需要等待响应数据，响应数据是数据，所以返回类型是InputStream
    InputStream write(InputStream data);

    void close();
}
