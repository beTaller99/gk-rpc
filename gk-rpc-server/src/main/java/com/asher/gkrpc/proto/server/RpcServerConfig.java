package com.asher.gkrpc.proto.server;

import com.asher.gkrpc.transport.HttpTransportServerImpl;
import com.asher.gkrpc.transport.TransportServer;
import com.asher.gkrpc.proto.codec.Decoder;
import com.asher.gkrpc.proto.codec.Encoder;
import com.asher.gkrpc.proto.codec.JSONDecoder;
import com.asher.gkrpc.proto.codec.JSONEncoder;
import lombok.Data;

/**
 * @className: RpcServerConfig
 * @Description: server配置
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/4/21 10:41
 */
@Data
public class RpcServerConfig {
   /*
   server配置需要配置以下三点
   代表使用哪个网络模块
          哪个序列化实现
    启动之后监听什么端口
    */

    private Class<? extends TransportServer> transportClass = HttpTransportServerImpl.class;

    //配置序列化 : 序列化和反序列化都要配置
    private Class<? extends Encoder> encoderClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;

    //监听端口，赋值3000
    private int port = 3000;


}
