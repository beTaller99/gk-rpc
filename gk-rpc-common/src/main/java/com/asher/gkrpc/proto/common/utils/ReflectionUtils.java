package com.asher.gkrpc.proto.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @className: ReflectionUtils
 * @Description: TODO
 * @version: openjdk-17.0.2
 * @author: asher
 * @date: 2022/4/8 22:09
 */
@Slf4j
public class ReflectionUtils {
    //根据class创建对象
    /*
     * @param clazz 待创建对象的类
     * @return T
    */
    /*  第一个 表示这个方法是一个泛型的方法
        第二个 T 表示是这个方法返回的类型
        第三个Class表示获取T类型的类*/
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        }  catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /*
     * @Description 获取类的公共方法
     * @Param [clazz]
     * @return java.lang.reflect.Method[]
     **/
    public static Method[] getPublicMethods(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> pMethods = new ArrayList<Method>();
        //过滤出公共方法
        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                pMethods.add(method);
            }
        }
        return pMethods.toArray(new Method[0]);
    }

   
    
    /*
     * @Description 获取指定对象的指定方法
     * @Param obj: 被调用方法的对象
     * @Param method: 被调用的方法
     * @Param args: 方法需要的参数
     * @return 返回结果
     **/
    public static Object invoke(Object obj, Method method, Object... args) {
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
