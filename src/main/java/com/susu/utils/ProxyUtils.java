package com.susu.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p>Description: Proxy</p>
 * <p>动态代理</p>
 * @author sujay
 * @version 12:32 2022/5/31
 * @see java.util.Date
 * @since JDK1.8
 */
public class ProxyUtils {

    public static Object getProxyInstance(Object obj) {
        Class[] interfaces = { obj.getClass() };
        return Proxy.newProxyInstance(ProxyUtils.class.getClassLoader(), interfaces, new BeanProxyFactory(obj));
    }

}


class BeanProxyFactory implements InvocationHandler {

    private final Object obj;

    public BeanProxyFactory(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(obj,args);
    }
}
