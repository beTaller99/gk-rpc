package com.asher.gkrpc.proto.codec;

/*
 * @Description 反序列化
 * @Param null:
 * @return
 **/
public interface Decoder {
//    Object decode(byte[] bytes);  //这么写不太方便，所以选择用泛型来处理
    <T> T decode(byte[] bytes, Class<T> clazz);
}
