package com.mina.core;


import com.mina.core.util.DateUtil;
import org.apache.mina.core.buffer.IoBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseMessage {


    /**
     * HTTP response codes
     */
    public static final int HTTP_STATUS_SUCCESS = 200;

    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    public static final int HTTP_STATUS_NOT_FOUND = 404;

    public static final int HTTP_STATUS_EXCEPTION = 500;

    /**
     * Map<String, String>
     */
    private final Map<String, String> headers = new HashMap<String, String>();

    /**
     * Storage for body of HTTP response.
     */
    private final ByteArrayOutputStream body = new ByteArrayOutputStream(1024);

    private int responseCode = HTTP_STATUS_SUCCESS;


    private int responseMode = 1;  //1  http格式应答  默认   2 全文本模式的tcp数据包应答

    public int getResponseMode() {
        return responseMode;
    }

    public void setResponseMode(int responseMode) {
        this.responseMode = responseMode;
    }

    public HttpResponseMessage() {
        // headers.put("Server", "HttpServer (" + Server.VERSION_STRING + ')');
        headers.put("Server", "HttpServer (" + "Mina 2.0" + ')');
        headers.put("Cache-Control", "private");
        //headers.put("Content-Type", "text/html; charset=iso-8859-1");
        headers.put("Content-Type", "text/html; charset=utf-8");
        headers.put("Connection", "keep-alive");
        headers.put("Keep-Alive", "200");
//		headers.put("Date", new SimpleDateFormat(
//				"EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date()));
//		headers.put("Last-Modified", new SimpleDateFormat(
//				"EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date()));

		/*
		headers.put("Date", new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date()));*/
        headers.put("Date", DateUtil.formatDateTime(new Date()));
		/*
		headers.put("Last-Modified", new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date()));*/
        headers.put("Last-Modified", DateUtil.formatDateTime(new Date()));

    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setContentType(String contentType) {
        headers.put("Content-Type", contentType);
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void appendBody(byte[] b) {
        try {
            body.write(b);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void appendBody(String s) {
        try {
//			body.write(s.getBytes());
            body.write(s.getBytes("UTF-8"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public IoBuffer getBody() {
        return IoBuffer.wrap(body.toByteArray());
    }

    public int getBodyLength() {
        return body.size();
    }

}
