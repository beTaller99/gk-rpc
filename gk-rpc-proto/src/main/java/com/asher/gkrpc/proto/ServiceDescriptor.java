package com.asher.gkrpc.proto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @className: ServiceDecriptor
 * @Description: 表示服务
 * @version: openjdk-17.0.2
 * @author: asher
 * @date: 2022/4/8 17:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDescriptor {
    /*用来表示服务的话
    首先需要一个类名
    这个类的每一个方法
    这个方法的返回类型是什么，参数是什么
    * */

    private String clazz;
    private String method;
    private String returnType;
    private String[] parameterTypes; //因为参数有多个，所以用数组来表示

    public static ServiceDescriptor from(Class clazz, Method method) {
        ServiceDescriptor serviceDescriptor = new ServiceDescriptor();
        serviceDescriptor.setClazz(clazz.getName());
        serviceDescriptor.setMethod(method.getName());
        serviceDescriptor.setReturnType(method.getReturnType().getTypeName());

        Class<?>[] parameterClass = method.getParameterTypes();
        String[] parameterTypes = new String[parameterClass.length];
        for (int i = 0; i < parameterClass.length; i++) {
            parameterTypes[i] = parameterClass[i].getName();

        }
        serviceDescriptor.setParameterTypes(parameterTypes);

        return serviceDescriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceDescriptor that = (ServiceDescriptor) o;
        return this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return "clazz=" + clazz
                + ",method=" + method
                + ",returnType=" + returnType
                + ",parameterTypes=" + Arrays.toString(parameterTypes);
    }
}
