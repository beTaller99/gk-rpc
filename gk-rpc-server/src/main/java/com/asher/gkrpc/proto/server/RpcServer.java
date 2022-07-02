package com.asher.gkrpc.proto.server;

import com.asher.gkrpc.proto.Request;
import com.asher.gkrpc.transport.RequestHandler;
import com.asher.gkrpc.proto.Response;
import com.asher.gkrpc.transport.TransportServer;
import com.asher.gkrpc.proto.codec.Decoder;
import com.asher.gkrpc.proto.codec.Encoder;
import com.asher.gkrpc.proto.common.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * @className: RpcServer
 * @Description: TODO
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/4/29 17:32
 */
@Slf4j
public class RpcServer {
    //server有自己的配置信息、网络模块、序列化模块、反序列化、服务管理ServiceManager、服务调用ServiceInvoker

    private RpcServerConfig config;
    private TransportServer net;
    private ServiceInvoker serviceInvoker;
    private ServiceManager serviceManager;
    private Encoder encoder;
    private Decoder decoder;


    public RpcServer(RpcServerConfig config) {
        this.config = config;
        this.net = ReflectionUtils.newInstance(config.getTransportClass());
        this.net.init(config.getPort(), this.handler);
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());

        this.serviceInvoker = new ServiceInvoker();
        this.serviceManager = new ServiceManager();


    }

    private RequestHandler handler = new RequestHandler() {
        @Override
        public void onRequest(InputStream recive, OutputStream toResp) {
            //handler第一步：从recive中读取二进制数据。
            // 读完之后通过ServiceInvoker调用服务，然后通过toResp把数据写回去

            Response resp = new Response();

            //读数据
            //利用commonio读取数据
            try {
                byte[] inBytes = IOUtils.readFully(recive, recive.available());

                //将二进制数据反序列化为request
                Request request = decoder.decode(inBytes, Request.class);
                log.info("get request : {}", request);

                //有了request之后就可以找到服务了
                ServiceInstance sis = serviceManager.lookup(request);

                //有了serviceInstance之后就可以通过serviceInvoker调用
                //ret是返回值
                Object ret = serviceInvoker.invoke(sis, request);

                resp.setData(ret);

            } catch (Exception e) {
                log.warn(e.getMessage(), e);

                //返回状态码置为失败1
                resp.setCode(1);
                //失败消息返回错误类型
                resp.setMessage("RpcServer got a error："
                        + e.getClass().getName()
                        + " ：" + e.getMessage());
            } finally {
                //在finally把数据彻底返回到客户端
                //做法：将response序列化为二进制数据

                try {
                    byte[] outBytes = encoder.encode(resp);
                    toResp.write(outBytes);

                    log.info("respone client");
                } catch (IOException ioException) {
                    log.warn(ioException.getMessage(), ioException);
                }
            }

        }
    };

    public RpcServer() {
        this(new RpcServerConfig());
    }

    //添加注册方法，注册过程和servicemanager一样
    public <T> void  register(Class<T> interfaceClass, T bean) {
        serviceManager.register(interfaceClass, bean);
    }

    //添加start（） 方法，让server启动————实际上就是让网络模块启动
    public void start(){
        this.net.start();
    }
    //添加stop（）方法，让server关闭————实际就是让网络模块关闭
    public void stop() {
        this.net.stop();
    }
}
