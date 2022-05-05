package com.asher.gkrpc.transport;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description 处理网络请求的handler
 *
 **/
public interface RequestHandler {
    /*
     * @Description 
     * @Param recive: 接收的数据
     * @Param toResp: 输出数据的通道
     * @return void
     **/
   void onRequest(InputStream recive, OutputStream toResp);
}
