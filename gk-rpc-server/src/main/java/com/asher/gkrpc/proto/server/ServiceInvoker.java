package com.asher.gkrpc.proto.server;

import com.asher.gkrpc.proto.Request;
import com.asher.gkrpc.proto.common.utils.ReflectionUtils;

/**
 * @className: ServiceInvoker
 * @Description: 调用Service实例的辅助类
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/4/29 17:11
 */
public class ServiceInvoker {
    //invoke方法：返回调用到的服务应返回的类型
    //调用具体服务
    public Object invoke(ServiceInstance service, Request request) {

        //利用我们之前的反射工具类
        return ReflectionUtils.invoke(service.getTarget(), service.getMethod(),
                request.getParameters());
    }
}
