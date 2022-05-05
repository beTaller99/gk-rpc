package com.asher.gkrpc.proto.codec;

/*
 * @Description 序列化接口
 * @Param null:
 * @return
 **/
public interface Encoder {
    //把对象转成byte数组, 所以返回值肯定是byte[]
    byte[] encode(Object obj);
}
