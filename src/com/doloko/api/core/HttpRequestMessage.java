package com.doloko.api.core;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 使用Mina解析出的HTTP请求对象
 *
 * @author Ajita
 */
public class HttpRequestMessage {
    /**
     * HTTP请求的主要属性及内容
     */
    private Map<String, String[]> headers = null;

    public Map<String, String[]> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String[]> headers) {
        this.headers = headers;
    }

    public String getHost() {
        String[] host = headers.get("Host");
        return host == null ? "" : host[0];
    }

    public String getIp() {
        String[] ip = headers.get("ip");
        return ip == null ? "" : ip[0];
    }

    /**     * 将url参数转换成map
     * @param param aa=11&bb=22&cc=33
     * @return
     */


    /**
     * 获取HTTP请求的Context信息
     */
    public String getContext() {
//		String[] context = headers.get("Context");
//		return context == null ? "" : context[0];
//		v1/adapter?appid=test0&sign=test8&version=test2&openid=test1&configid=test4
        String[] url = headers.get("Context");
        String context = "";
        String temp = url[0];
        String[] arr = temp.split("[?]");
        context = arr[0];
        if ("POST".equals(headers.get("Method")[0])) {
            if (headers.get("Content-Type")[0].contains("text/xml")) {
                headers.put("context", new String[]{arr[1]});
            } else if (headers.get("Content-Type")[0].contains("application/json")) {
                setJsonParameter(headers.get("@body")[0]);
            } else if (headers.get("Content-Type")[0].contains("application/x-www-form-urlencoded")) {
                setParameter(headers.get("@body")[0]);
            } else if ("multipart/form-data".equals(headers.get("Content-Type"))) {

            }
        } else
            setParameter(arr[1]);
        return context;
    }

    private void setParameter(String params) {

        String[] paramArr = params.split("&");
        for (int i = 0; i < paramArr.length; i++) {
            String[] p = paramArr[i].split("=");
            if (p.length == 2) {
                headers.put("@" + p[0], new String[]{p[1]});
            }
        }
    }

    private void setJsonParameter(String jsonString) {
        Map<String, String> m = JSON.parseObject(jsonString, HashMap.class);
        if (m != null) {
            Iterator<Entry<String, String>> entries = m.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                headers.put("@" + entry.getKey(), new String[]{String.valueOf(entry.getValue())});
            }
        }

    }

    public String getTempContext() {
        String[] url = headers.get("Context");
        return url[0];
    }

    /**
     * 根据属性名称获得属性值数组第一个值，用于在url中传递的参数
     */
    public String getParameter(String name) {
        String[] param = headers.get("@".concat(name));
        return param == null ? "" : param[0];
    }

    /**
     * 根据属性名称获得属性值，用于在url中传递的参数
     */
    public String[] getParameters(String name) {
        String[] param = headers.get("@".concat(name));
        return param == null ? new String[]{} : param;
    }

    /**
     * 根据属性名称获得属性值，用于请求的特征参数
     */
    public String[] getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (Entry<String, String[]> e : headers.entrySet()) {
            str.append(e.getKey() + " : " + arrayToString(e.getValue(), ',')
                    + "\n");
        }
        return str.toString();
    }

    /**
     * 静态方法，用来把一个字符串数组拼接成一个字符串
     *
     * @param s   要拼接的字符串数组
     * @param sep 数据元素之间的烦恼歌负
     * @return 拼接成的字符串
     */
    public static String arrayToString(String[] s, char sep) {
        if (s == null || s.length == 0) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        if (s != null) {
            for (int i = 0; i < s.length; i++) {
                if (i > 0) {
                    buf.append(sep);
                }
                buf.append(s[i]);
            }
        }
        return buf.toString();
    }

}
