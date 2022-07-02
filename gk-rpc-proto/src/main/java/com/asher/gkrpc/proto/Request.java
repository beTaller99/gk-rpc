package com.asher.gkrpc.proto;

import lombok.Data;

/**
 * @className: Request
 * @Description: 表示RPC的一个请求
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/4/8 17:47
 */
@Data
public class Request {
    /*一个请求，肯定要表示他想
    * 请求什么服务
    * 然后传递参数
    * */
    private ServiceDescriptor service;
    private Object[] parameters;

}
