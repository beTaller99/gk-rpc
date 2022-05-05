package com.asher.gkrpc.proto.codec;

import junit.framework.TestCase;

public class JSONEncoderTest extends TestCase {

    public void testEncode() {
        /*
        * 先创建Encoder对象，然后再把Bean给new出来
        * */

        Encoder encoder = new JSONEncoder();
        TestBean bean = new TestBean("xiaoming",20);
        bean.add(12,12);

        byte[] encode = encoder.encode(bean);

        assertNotNull(encode);
    }
}