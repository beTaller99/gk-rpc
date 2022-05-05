package com.asher.gkrpc.transport;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @className: HttpTransportServerImpl
 * @Description: TODO
 * @version: openjdk-17.0.2
 * @author: asher
 * @date: 2022/4/17 16:04
 */
@Slf4j
public class HttpTransportServerImpl implements TransportServer {

    //先把handler保存起来
    private RequestHandler handler;

    //因为server是基于jetty的，所以我们在init的时候要把它new出来
    private Server server;


    @Override
    public void init(int port, RequestHandler handler) {
        this.handler = handler;
        this.server = new Server(port);

        //为了监听方便，我们在实现请求的时候是基于servlet的，所以我们在这里需要一个servletContextHandler
        //servlet接收请求
        ServletContextHandler ctx = new ServletContextHandler();
        //设置server的handler
        server.setHandler(ctx);

        //设置一个servlet的一个holder，这个holder是在处理网络请求时的一个抽象
        ServletHolder holder = new ServletHolder(new RequestServlet());
        ctx.addServlet(holder, "/*");
    }

    @Override
    public void start() {

        try {
            //启动的是启动jetty的server
            server.start();
            //因为start之后会开启一个线程，这个线程有一个方法会立马返回，为了不让他立马返回，所以我们使用join方法让他等待,
            //等待到server执行stop方法，停止关闭
            server.join();
        } catch (Exception e) {
            //让logback来处理打印错误信息
            log.error(e.getMessage(),e);
        }

    }

    @Override
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    //写一个servlet内部类来处理网络请求
    class RequestServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            doPost(req,resp);
        }

        //因为要处理post请求，所以我们把doPost方法重写一下
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            //加一些日志信息
            log.info("client connect");

            //clien与server连接之后会发送数据过来，我们需要拿到client发送的数据，怎么做呢
            //使用request里面的getInput方法
            InputStream in = req.getInputStream();

            //我们还需要返回Output
            OutputStream out = resp.getOutputStream();

            if (handler != null) {
                handler.onRequest(in, out);
            }

            //把out给flush一下
            out.flush();
/*
            flush的作用：
            flush是要求将该OutputStream的临时缓存清空，强制写到底层的OutputStream中.flush方法的意思是强制将缓冲区所有的数据输出！
            例如你外面套了一层BufferedOutputStream，那么你写入的内容其实有一部分还保存在这个Stream中，而没有写入底层的。所以在某些时候必须调用flush要求写入.
*/
        }
    }
}
