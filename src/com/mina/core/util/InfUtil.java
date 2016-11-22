package com.mina.core.util;

import com.mina.core.HttpRequestMessage;
import com.mina.core.exception.InfException;
import org.apache.commons.lang.StringUtils;

/**
 * Created by ASUS on 2016/11/18.
 */
public class InfUtil {

    public static String chkInterface(HttpRequestMessage request) throws InfException {
        /*大并发测试 直接返回结果 不进行后台业务处理 测试响应数量*/
        if (request == null || request.getHeaders() == null) {//底层解析请求协议错误，或者编码格式错误
            throw new InfException("协议格式错误,无识别结果");
        }
        String context = request.getContext();
        if ("".equals(StringUtils.trimToEmpty(context))) {
            throw new InfException("协议格式错误,无识别结果");
        }
        return context.indexOf("?") > 0 ? context.substring(0, context.indexOf("?")) : context;
    }

}
