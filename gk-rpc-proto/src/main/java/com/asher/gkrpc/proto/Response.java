package com.asher.gkrpc.proto;

import lombok.Data;

/**
 * @className: Reseponse
 * @Description: 有了请求那还要有表示 响应 的类
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/4/8 17:50
 */
@Data
public class Response {
    /*一般在使用远程服务的时候会使用
    code来表示成功或失败，一般使用 0 表示成功， 非0 表示失败
    如果失败的话，还需要
    message来表示失败的原因
    data 来表示返回的具体数据
    */
    private int code = 0; //我们默认成功
    private String message = "ok";
    private Object data;
}
