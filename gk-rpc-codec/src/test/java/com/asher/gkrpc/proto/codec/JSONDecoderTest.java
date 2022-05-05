package com.asher.gkrpc.proto.codec;

import junit.framework.TestCase;

public class JSONDecoderTest extends TestCase {

    public void testDecode() {
        //把序列化好的代码拿过来
        Encoder encoder = new JSONEncoder();
        TestBean bean = new TestBean("xiaoming",20);
        bean.add(12,12);
        byte[] encode = encoder.encode(bean);

        Decoder decoder = new JSONDecoder();
        TestBean decode = decoder.decode(encode, TestBean.class);

        assertEquals(bean.getAge(),decode.getAge());
        assertEquals(bean.getName(), decode.getName());
    }
}