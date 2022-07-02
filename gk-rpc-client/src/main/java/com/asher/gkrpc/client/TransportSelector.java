package com.asher.gkrpc.client;

import com.asher.gkrpc.proto.Peer;
import com.asher.gkrpc.transport.TransportClient;


import java.util.List;

/**
 * @className: TransportSelector
 * @Description: 表示选择哪个server去连接
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/5/1 17:36
 */
public interface TransportSelector {

    /*
     * @Description 初始化server，需要三个参数
     * @Param peers: 可以连接的server的端点集合
     * @Param count: client与server要连接的节点数量
     * @Param clazz: client实现class
     * @return void
     **/
    void init(List<Peer> peers, int count, Class<? extends TransportClient> clazz);

    /*
     * @Description 选择一个transport去与server连接交互
     * @return 网络client
     **/
    TransportClient select();

    /*
     * @Description 释放用完的client
     * @Param client: 待关闭的client
     * @return void
     **/
    void release(TransportClient client);

    //关闭client
    void close();
}
