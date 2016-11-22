package com.doloko.api.route;

import com.doloko.api.core.HttpHandler;
import com.doloko.api.core.HttpRequestMessage;
import com.doloko.api.core.HttpResponseMessage;
import com.doloko.api.core.util.InfUtil;
import com.doloko.api.route.annotation.Mapping;
import com.doloko.api.route.exception.InitRouteMappingException;
import com.doloko.api.route.exception.RouteNotFoundException;
import com.doloko.api.route.util.ClassFindUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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

    public static void main(String[] args) {
        String s = "AsssSS.class";
        System.out.println(s.substring(s.lastIndexOf(".") + 1));
    }
    private void initRoute() throws Exception {
        //扫描包找到@Mapping的类
        List<String> classNames = new ArrayList<String>();
        try {
            classNames = ClassFindUtil.loop(new File(Thread.currentThread().getContextClassLoader().getResource("").getPath()),"");
        } catch (Exception e) {
            throw new InitRouteMappingException("init route mapping fail.");
        }
        if (classNames.size() != 0) {
            for (String className : classNames) {
                try {
                    //把spring bean 放入缓存
                    String index0 = className.substring(0,1);
                    className = className.replace(index0,index0.toLowerCase());
                    Object o = this.context.getBean(className);
                    Method[] methods = o.getClass().getDeclaredMethods(); // 获取实体类的所有属性，返回Field数组
                    for (int j = 0; j < methods.length; j++) { // 遍历所有属性
                        Mapping m = methods[j].getAnnotation(Mapping.class);
                        if (m != null) {//手动注入
                            initService(o);
                            Invoke invoke = new Invoke();
                            invoke.setO(o);
                            invoke.setM(methods[j]);
                            routes.put(m.value(), invoke);
                        }
                    }
                } catch (Exception e) {
                    throw new InitRouteMappingException("init "+className+" route mapping fail.");
                }
            }
        }
    }

    private void initService(Object o) throws IllegalAccessException {
        Field[] fields = o.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        for (int j = 0; j < fields.length; j++) { // 遍历所有属性
            Autowired a = fields[j].getAnnotation(Autowired.class);
            if (a != null) {//手动注入
                fields[j].setAccessible(true);
                fields[j].set(o, this.context.getBean(fields[j].getName()));
            }
        }
    }

    private Invoke getInvoke(String cxt) throws RouteNotFoundException {
        // TODO 分析cxt得到key
        Invoke invoke = routes.get("/"+cxt);
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
        response.setContentType("application/json");
        response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
        response.appendBody(invoke.invoke());
        return response;
    }

}
