package com.asher.gkrpc.proto.codec;

import com.alibaba.fastjson.JSON;

/**
 * @className: JSONEncoder
 * @Description: TODO
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/4/9 17:41
 */
public class JSONEncoder implements Encoder {
    @Override
    public byte[] encode(Object obj) {
        return JSON.toJSONBytes(obj);
    }
}
