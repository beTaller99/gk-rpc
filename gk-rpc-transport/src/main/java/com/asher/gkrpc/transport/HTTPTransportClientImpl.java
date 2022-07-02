package com.asher.gkrpc.transport;

import com.asher.gkrpc.proto.Peer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * @className: TransportClientImpl
 * @Description: client的实现是基于HTTP
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/4/9 19:58
 */
public class HTTPTransportClientImpl implements TransportClient {
    private String url;

    @Override
    public void connect(Peer peer) {
        // HTTP是基于短链接的，不需要链接server，但是需要一个URL，所以我们要在这里把URL拼出来
        this.url = "http://" + peer.getHost() + ":" + peer.getPort();
    }

    @Override
    public InputStream write(InputStream data) {
        //我们在写数据的时候，第一步是要把connection给打开的，
        // 我们直接使用jdk自带的HTTPConnection

        try {
            HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();

            //因为需要输出，所以setDoOutput设置为true
            httpConn.setDoOutput(true);

            //同时也需要读数据所以setDoInput设置为true
            httpConn.setDoInput(true);

            //我们不需要使用cache
            httpConn.setUseCaches(false);

            //我们使用post方法
            httpConn.setRequestMethod("POST");

            //连接
            httpConn.connect();

            //连接完之后我们需要把data数据发送给server
            //在这里的做法是选择使用commons.io包的IOUtils中copy方法
            // 把data拷贝到outputStream里面去
            IOUtils.copy(data, httpConn.getOutputStream());

            //获取http的code
            int resultCode = httpConn.getResponseCode();
            if (resultCode == HttpURLConnection.HTTP_OK) {
                return httpConn.getInputStream();
            } else {
                return httpConn.getErrorStream();
            }
        } catch (IOException ioException) {
            throw new IllegalStateException(ioException);
        }


    }

    @Override
    public void close() {

    }
}
