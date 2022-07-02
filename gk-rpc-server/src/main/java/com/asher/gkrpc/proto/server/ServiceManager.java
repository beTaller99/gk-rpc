package com.asher.gkrpc.proto.server;

import com.asher.gkrpc.proto.Request;
import com.asher.gkrpc.proto.ServiceDescriptor;
import com.asher.gkrpc.proto.common.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @className: ServiceManager
 * @Description: 管理rpc暴露的服务
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/4/21 10:56
 */
@Slf4j
public class ServiceManager {
/*
    这个类应该需要一个注册服务的方法和一个查找服务的方法
*/

    //注册的服务放到map里面， key使用服务的描述，value使用服务的实现
    private Map<ServiceDescriptor, ServiceInstance> services;

    public ServiceManager() {
        this.services = new ConcurrentHashMap<>();
    }

    public <T> void register(Class<T> interfaceClass, T bean) {
        /*把接口里的所有方法扫描出来绑定到bean，形成一个ServiceInstance*/

        //这里使用到common模块下的反射方法来获取
        Method[] methods = ReflectionUtils.getPublicMethods(interfaceClass);
        for (Method method : methods) {
            ServiceInstance sis = new ServiceInstance(bean ,method);

            //为了往map里面放，我们还得生成一个key。所以我们回到descriptor去写一个from方法,然后再使用from方法
            ServiceDescriptor serviceDescriptor = ServiceDescriptor.from(interfaceClass, method);

            services.put(serviceDescriptor, sis);
            //这样我们就把所有服务注册进来了

            //现在我们加一个日志输出看一看
            log.info("register service: {} {}", serviceDescriptor.getClazz(), serviceDescriptor.getMethod());
        }
    }

    //查找服务， 返回的应该是一个Serviceinstance,  应该传入rpc的一个request
    public ServiceInstance lookup(Request request) {
        //需要通过key来查找，所以我们先拿到ServiceDescriptor
        ServiceDescriptor serviceDescriptor = request.getService();
        log.info(" serviceDescriptor : {}", request.getService());
        return services.get(serviceDescriptor);

        //map的key——ServiceDescriptor是我们自己的一个类，而使用map的get方法的时候是用ServiceDescriptor的
        // equals方法去判断的，所以现在我们需要对这个类的equals方法进行重写，但是在重写eqauls方法之前我们还要重写
        // hashCode()方法
    }

}
