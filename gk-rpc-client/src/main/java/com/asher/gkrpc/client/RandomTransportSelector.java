package com.asher.gkrpc.client;

import com.asher.gkrpc.proto.Peer;
import com.asher.gkrpc.transport.TransportClient;
import com.asher.gkrpc.proto.common.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @className: RandomTransportSelector
 * @Description: TODO
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/5/1 17:55
 */
@Slf4j
public class RandomTransportSelector implements TransportSelector {
    //新建一个list，存储所有已经连好的TransportClient
    private List<TransportClient> clients;

    //初始化client的池子
    public RandomTransportSelector() {
        clients = new ArrayList<>();
    }

    @Override
    public synchronized void  init(List<Peer> peers, int count, Class<? extends TransportClient> clazz) {

        //做一个判断：count必须大于等于1
        count = Math.max(count,1);

        //通过循环创建client的连接
        for (Peer peer : peers) {
            for (int i = 0; i < count; i++) {
                TransportClient client = ReflectionUtils.newInstance(clazz);
                client.connect(peer);
                clients.add(client);
            }
            log.info("connect server peer : {}", peer);
        }

    }

    @Override
    public synchronized TransportClient select() {
        //通过jdk自带的随机数来选取，并且保证选出来的数不会大过可用的client数量
        int i = new Random().nextInt(clients.size());

        //将选中的client从可用集合中去掉，并返回
        return clients.remove(i);
    }

    @Override
    public synchronized void release(TransportClient client) {

        //client被释放之后要加到可用client集合中
        clients.add(client);
    }

    @Override
    public synchronized void close() {
        //将所有的client都关闭
        for (TransportClient client : clients) {
            client.close();
        }
        //关闭之后，清除掉所有client
        clients.clear();
    }

    //因为ArrayList是线程不安全的，我们每一个方法都用到了clients这个集合，为了适应高并发
    // 所以我们每一个方法要有synchronize去修饰
}
