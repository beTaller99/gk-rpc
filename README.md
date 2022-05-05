1.删去request类下的有参与无参注解





![image-20220503220313727](C:\Users\hh\AppData\Roaming\Typora\typora-user-images\image-20220503220313727.png)







![image-20220503232338146](C:\Users\hh\AppData\Roaming\Typora\typora-user-images\image-20220503232338146.png)



server中设定net之后要初始化



在transport模块下的httpTransportServer的实现中，没有调用serverlet，导致client启动通过http调用post方法时，会报错“HTTP method POST is not supported by this URL”

用这个报错去搜网上，大部分都是说没有重写doGet方法导致，绝对不是！！

而是因为在初始化时没有将servlet和处理路径添加到我们的servletContextHandler中





# RPC

实现简易的RPC框架

运行成功图

client：![在这里插入图片描述](https://img-blog.csdnimg.cn/aeb82cf768e74f27adaa71e63dde2f82.png)



server：![在这里插入图片描述](https://img-blog.csdnimg.cn/27125fa6520d4c5684be9ac15adc7258.png)



# 项目架构：

### 1. common模块：通用工具模块。

主要提供了反射工具类

### 2. codec模块：序列化模块

借助fastjson实现序列化与反序列

### 3. proto模块：协议模块

该模块用于确定client与server通信的协议

- Peer类用于表示端点；

- Request 类、Response类表示请求与响应
- ServiceDescriptor类表示服务

### 4. transport模块：网络传输模块

基于http实现，使用jetty作为容器。

- ```java
  //处理网络请求的handle
  public interface RequestHandler {
      void onRequest(InputStream recive, OutputStream toRespon);
  }
  ```

- ``` java
  //RPC server服务定义
  public interface TransportServer {
      //初始化Server服务
      void init(int port, RequestHandler handler);
      //开启Server服务
      void start();
      //关闭Server服务
      void stop();
  }
  ```

- ```java
  //客户端 也是服务消费者
  public interface TransportClient {
      //连接Server服务
      void connect(Peer peer);
      //订阅Server服务  并返回response
      InputStream write(InputStream data);
      //关闭
      void close();
  }
  ```



### 5. server模块

服务注册、服务管理、服务调用

确定端口

使用网络传输模块中`HttpTransportServer`，将请求在Handle中实现，并封装在Response中。

### 6. client模块

确定路由、端口。

借助动态代理完成方法调用



