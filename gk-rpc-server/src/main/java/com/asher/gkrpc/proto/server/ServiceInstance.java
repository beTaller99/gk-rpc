package com.asher.gkrpc.proto.server;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @className: ServiceInstance
 * @Description: 表示一个具体服务
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/4/21 10:54
 */
@Data
@AllArgsConstructor
public class ServiceInstance {
    /*这个服务由哪个对象target提供的，这个对象提供了什么方法*/

    //提供服务的对象
    private Object target;
   //对象暴露的方法
    private Method method;
}
