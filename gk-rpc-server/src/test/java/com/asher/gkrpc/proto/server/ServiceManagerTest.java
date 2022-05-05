package com.asher.gkrpc.proto.server;

import com.asher.gkrpc.proto.Request;
import com.asher.gkrpc.proto.ServiceDescriptor;
import com.asher.gkrpc.proto.common.utils.ReflectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

public class ServiceManagerTest{
    ServiceManager sm;

    @Before
    public void init() {
        sm = new ServiceManager();
        //下面两行是为了测试lookup方法的，测试register方法时注释掉
        TestInterface bean = new TestClass();
        sm.register(TestInterface.class, bean);
    }

    @Test
    public void testRegister() {
        TestInterface bean = new TestClass();
        sm.register(TestInterface.class, bean);
    }

    @Test
    public void testLookup() {
        Method[] methods = ReflectionUtils.getPublicMethods(TestInterface.class);
        ServiceDescriptor sdp = ServiceDescriptor.from(TestInterface.class, methods[0]);

        Request request = new Request();
        request.setService(sdp);

        ServiceInstance lookup = sm.lookup(request);
        Assert.assertNotNull(lookup);
    }
}