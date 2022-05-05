package com.asher.gkrpc.client;

import com.asher.gkrpc.proto.codec.Decoder;
import com.asher.gkrpc.proto.codec.Encoder;
import com.asher.gkrpc.proto.common.utils.ReflectionUtils;

import java.lang.reflect.Proxy;

/**
 * @className: RpcClient
 * @Description: TODO
 * @version: openjdk-17.0.2
 * @author: asher
 * @date: 2022/5/3 16:50
 */
public class RpcClient {
    private RpcClientConfig config;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;

    public RpcClient() {
        this(new RpcClientConfig());
    }

    public RpcClient(RpcClientConfig config) {
        this.config = config;
        this.decoder = ReflectionUtils.newInstance(this.config.getDecoderClass());
        this.encoder = ReflectionUtils.newInstance(this.config.getEncodeClass());
        this.selector = ReflectionUtils.newInstance(this.config.getSelectClass());

        this.selector.init(
                this.config.getServers(),
                this.config.getConnectCount(),
                this.config.getTransportClient()
        );
    }

    //获取接口的代理对象，我们希望该方法返回传入Class的子类对象，因此使用泛型
    public <T> T getProxy(Class<T> clazz) {
        //基于jdk 动态代理
        return (T) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{clazz},
                new RemoteInvoker(clazz, encoder, decoder, selector)
        );
    }

}
