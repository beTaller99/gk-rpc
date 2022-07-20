# RPC

### 项目环境

> jdk1.8  + fastjson + lombok + Commons IO + logback + jetty


实现简易的RPC框架

运行成功图

client：![在这里插入图片描述](https://img-blog.csdnimg.cn/aeb82cf768e74f27adaa71e63dde2f82.png)



server：![在这里插入图片描述](https://img-blog.csdnimg.cn/27125fa6520d4c5684be9ac15adc7258.png)

## rpc调用过程
client调用接口方法，存根中的代理对象会通过socket与server间建立连接，连接之后传输数据，传输数据之前将传输对象转成二进制数据（序列化），
server，拿到数据之后反序列化成对象，对象中包含客户端要调用的服务，参数，server找到接口具体实现类的对象。找到对象之后通过反射来调用方法，调用完成就可以拿到结果了，然后把数据序列化二进制数据，传回给客户端

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

### 7. example模块
用于测试整个框架

---

#### 一些小说明

---
当我们new一个RpcClient对象，就只是在RandomTransportSelector创建了一个节点，且指定了目标节点（也就是server）的host与port，但还没真正开始调用http的连接开启，只有到我们使用了动态代理的时候才开启

---
在client 的网络传输实现中，使用servlet包中的url对象，调用url对象的.openConnection()方法来开启对服务器（在本项目中是使用servlet容器的server，JSP的那个时代这个常用于连接数据库）的连接。连接之后，使用刚刚的openConnection()方法得到的HttpConnection对象强转成HttpUrlConnection类型，然后调用HttpUrlConnection的getOutputStream()方法像server发送数据。	要查看收到的数据，就调用HttpUrlConnection的getIntputStream()方法来获得

---
关于jdk动态代理：对于动态代理，首先要有一个类实现InvocationHandler接口，在本项目中，我们RemoteInvoker类实现了这个接口，在RemoteInvoker中我们存储了需要被代理的接口类clazz，序列化与反序列化对象，选择路由。

重写了invoke方法，该方法有三个参数invoke(Object proxy, Method method, Object[] args)，第一个参数是代理对象，第二个参数是被代理对象的方法，第三个参数是代理对象的方法中所需的参数集合，当invoke方法被调用，jdk在底层会根据调用的对象名与方法名通过反射得到代理对象proxy与方法method。

那么method名的获取是在我们调用代理对象的某个方法的时候才传入到底层的

选择路由本质上就是多创建了一个网络传输client对象

---
我们在RpcServerConfig 里面确定了port是3000，然后在传输模块中的HttpTransportServerImpl调用了jetty包下的server对象，给对象setPort，然后jetty的server开启之后就会在3000端口监听



---









