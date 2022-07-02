package com.asher.gkrpc.proto.codec;

import com.alibaba.fastjson.JSON;

/**
 * @className: JSONDecoder
 * @Description: TODO
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/4/9 17:42
 */
public class JSONDecoder implements Decoder {
    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }
}
