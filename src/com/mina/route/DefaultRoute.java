package com.mina.route;

import com.mina.core.HttpHandler;
import com.mina.core.HttpRequestMessage;
import com.mina.core.HttpResponseMessage;
import com.mina.core.util.InfUtil;
import com.mina.route.annotation.Mapping;
import com.mina.route.exception.InitRouteMappingException;
import com.mina.route.exception.RouteNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ASUS on 2016/11/18.
 */
public class DefaultRoute implements HttpHandler {
    private static final Log log = LogFactory.getLog(DefaultRoute.class);

    private ApplicationContext context;

    private static Map<String, Invoke> routes = new ConcurrentHashMap<String, Invoke>();

    public DefaultRoute() {
    }

    public DefaultRoute(ApplicationContext context) throws Exception {
        this.context = context;
        initRoute();
    }

    private void initRoute() throws Exception {
        Map<String, Object> beans = context.getBeansWithAnnotation(Mapping.class);
        System.out.println(beans);
        for (String key : beans.keySet()) {
            try {
                Object o = beans.get(key);
                Method[] methods = o.getClass().getDeclaredMethods(); // 获取实体类的所有属性，返回Field数组
                for (int j = 0; j < methods.length; j++) { // 遍历所有属性
                    Mapping m = methods[j].getAnnotation(Mapping.class);
                    if (m != null) {//手动注入
                        Invoke invoke = new Invoke();
                        invoke.setO(o);
                        invoke.setM(methods[j]);
                        routes.put(m.value(), invoke);
                    }
                }
            } catch (Exception e) {
                throw new InitRouteMappingException("init " + key + " route mapping fail.");
            }
        }
    }

    private Invoke getInvoke(String cxt) throws RouteNotFoundException {
        // TODO 分析cxt得到key
        Invoke invoke = routes.get("/" + cxt);
        if (invoke == null) {
            throw new RouteNotFoundException();
        }
        return invoke;
    }

    @SuppressWarnings("unchecked")
    public HttpResponseMessage handle(HttpRequestMessage request) throws Exception {
        String context = InfUtil.chkInterface(request); //检查接口请求是否合法
        Invoke invoke = getInvoke(context);
        invoke.setR(request);
        HttpResponseMessage response = new HttpResponseMessage();
        response.setContentType("text/html");
        response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
        response.appendBody(invoke.invoke());
        return response;
    }

}
