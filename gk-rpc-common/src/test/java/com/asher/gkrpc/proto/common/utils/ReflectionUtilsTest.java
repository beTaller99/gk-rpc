package com.asher.gkrpc.proto.common.utils;

import junit.framework.TestCase;

import java.lang.reflect.Method;

public class ReflectionUtilsTest extends TestCase {

    //先看看能不能创建一个类
    public void testNewInstance() {
        TestClass instance = ReflectionUtils.newInstance(TestClass.class);

        assertNotNull(instance);

    }

    public void testGetPublicMethod() {
        Method[] publicMethod = ReflectionUtils.getPublicMethods(TestClass.class);
        assertEquals(1, publicMethod.length);

        String name = publicMethod[0].getName();
        assertEquals("b",name);
    }

    public void testInvoke() {
        TestClass t = new TestClass();
        Method[] publicMethod = ReflectionUtils.getPublicMethods(t.getClass());
        Method method = publicMethod[0];

        Object invoke = ReflectionUtils.invoke(t, method);

        assertEquals("b",invoke);
    }
}