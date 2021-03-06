package com.mina.route;

import com.mina.core.HttpRequestMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ASUS on 2016/11/18.
 */
public class Invoke {
    private Object o;
    private Method m;

    private HttpRequestMessage r;

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }

    public Method getM() {
        return m;
    }

    public void setM(Method m) {
        this.m = m;
    }

    public HttpRequestMessage getR() {
        return r;
    }

    public void setR(HttpRequestMessage r) {
        this.r = r;
    }

    public String invoke() throws InvocationTargetException, IllegalAccessException {
        return (String) m.invoke(o, r);
    }
}
