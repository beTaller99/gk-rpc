package com.asher.gkrpc.client;

import com.asher.gkrpc.proto.*;
import com.asher.gkrpc.proto.codec.Decoder;
import com.asher.gkrpc.proto.codec.Encoder;

import com.asher.gkrpc.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @className: RemoteInvoker
 * @Description: 调用远程服务的代理类
 * @version: openjdk-17.0.2
 * @author: asher
 * @date: 2022/5/3 17:23
 */
@Slf4j
public class RemoteInvoker implements InvocationHandler {
    private Class clazz;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;

    //我们要调用远程代理服务，就要知道远程服务的Class信息，远程服务的序列化、反序列化，选择一个远程网络连接
    public RemoteInvoker(Class clazz, Encoder encoder, Decoder decoder, TransportSelector selector) {
        this.clazz = clazz;
        this.encoder = encoder;
        this.decoder = decoder;
        this.selector = selector;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /*要调用远程服务，首先构造一个请求，把这个请求通过网络发送给server，发送完之后等待server的响应，然后从响应
        里面拿到数据，一次调用就结束了*/

        //构造一个请求
        Request request = new Request();
        request.setService(ServiceDescriptor.from(clazz, method));
        request.setParameters(args);

        //发出请求获得响应
        Response resp = invokeRemote(request);

        //判断响应是否成功
        if (resp == null || resp.getCode() != 0) {
            throw new IllegalStateException("fail to invoke remote : " + resp);
        }

        //成功就返回数据，远程调用结束
        return resp.getData();
    }

    private Response invokeRemote(Request request) {
        Response resp = null;
        TransportClient client = null;

        try {
            //selector的select()方法确定使用  哪一个client  去连接server
            client = selector.select();

            byte[] outBytes = encoder.encode(request);

            //write方法执行实际上是执行HTTPTransportClientImpl的write方法，在这个方法中会开启和server的连接发送数据
            InputStream recive = client.write(new ByteArrayInputStream(outBytes));
            
            byte[] inBytes = IOUtils.readFully(recive, recive.available());
            //response赋值
            resp = decoder.decode(inBytes, Response.class);


        } catch (IOException e) {
            log.warn(e.getMessage(),e);
            //如果失败了，就返回错误的response
            resp = new Response();
            resp.setCode(1);
            resp.setMessage("RpcClient got error" + e.getClass() + " : " + e.getMessage());
        } finally {
            if (client != null) {
                selector.release(client);
            }
        }

        return resp;
    }
}
