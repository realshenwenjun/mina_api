package com.mina.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderAdapter;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public class HttpRequestDecoder extends MessageDecoderAdapter {
    private static final byte[] CONTENT_LENGTH = new String("Content-Length:").getBytes();
    static String defaultEncoding;
    private CharsetDecoder decoder;

    public CharsetDecoder getDecoder() {
        return decoder;
    }

    public void setEncoder(CharsetDecoder decoder) {
        this.decoder = decoder;
    }

    private HttpRequestMessage request = null;

    public HttpRequestDecoder() {
        decoder = Charset.forName(defaultEncoding).newDecoder();
    }

    //每接收到数据 运行至此 是否可以解码
//	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
//		try {
//			System.out.println("can decodable?");
//			return messageComplete(in) ? MessageDecoderResult.OK
//					: MessageDecoderResult.NEED_DATA;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//
//		return MessageDecoderResult.NOT_OK;
//	}


    //mina调用判断是否能收完整
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        try {
            //System.out.println("can decodable?");
            if(messageComplete(in)){
                //System.out.println("MessageDecoderResult.OK");
                return MessageDecoderResult.OK;
            }
            else{
                //System.out.println("MessageDecoderResult.NEED_DATA");
                return MessageDecoderResult.NEED_DATA;
            }
//		return messageComplete(in) ? MessageDecoderResult.OK
//				: MessageDecoderResult.NEED_DATA;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        //System.out.println("MessageDecoderResult.NOT_OK");
        return MessageDecoderResult.NOT_OK;
    }


    //mina接收到完整数据，进入decode
    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {
        HttpRequestMessage m = decodeBody(in);
        // Return NEED_DATA if the body is not fully read.
		/*
		if (m == null) {
			System.out.println("MessageDecoderResult.NEED_DATA");
			//return MessageDecoderResult.NEED_DATA;
			return MessageDecoderResult.OK;
		}*/
        //这里必须保证m对象里的headers成员不为空。为空则调用write实际向上层未传递数据导致messageReceived未触发
        //造成线程得不到事件而不能处理业务数据， 客户端会出现接收数据超时
        if(m==null  ||  m.getHeaders()==null){
            //System.out.println("no data----");
        }
        out.write(m); //向上层输出数据  触发HttpServerHandler.java的messageReceived
        //System.out.println("decodeBody ok");
        return MessageDecoderResult.OK;
    }

    /*v1.1
     *
     */
    private boolean messageComplete(IoBuffer in) {
        //测试代码
        //if(true)
        //	return true;
        //测试代码结束

        try{
            int last = in.remaining() - 1;
            if (in.remaining() < 4) { //解码最少需要接收4字节
                //System.out.println("<4");
                return false;
            }
            // to speed up things we check if the Http request is a GET or POST
            //get数据，末尾收到2个回车换行视为接收完成
            if (in.get(0) == (byte) 'G' && in.get(1) == (byte) 'E' && in.get(2) == (byte) 'T') {
                // Http GET request therefore the last 4 bytes should be 0x0D 0x0A
                // 0x0D 0x0A
                return in.get(last) == (byte) 0x0A && in.get(last - 1) == (byte) 0x0D
                        && in.get(last - 2) == (byte) 0x0A
                        && in.get(last - 3) == (byte) 0x0D;
            } else if (in.get(0) == (byte) 'P' && in.get(1) == (byte) 'O'
                    && in.get(2) == (byte) 'S' && in.get(3) == (byte) 'T') {
                // Http POST request
                // first the position of the 0x0D 0x0A 0x0D 0x0A bytes
                int eoh = -1;
                for (int i = last; i > 2; i--) { //末尾是连续2个回车换行
                    if (in.get(i) == (byte) 0x0A && in.get(i - 1) == (byte) 0x0D
                            && in.get(i - 2) == (byte) 0x0A
                            && in.get(i - 3) == (byte) 0x0D) {
                        eoh = i + 1;
                        break;
                    }
                }
                if (eoh == -1) {
                    return false;
                }
				
				/*查看in数据的代码
				StringBuffer sb=new StringBuffer();
				sb.append(new String(in.toString()));
				String test=in.toString();
				String test1=new String(in.array());
				*/
				/*
				String test1=new String(in.array());
				System.out.println(test1);*/

                for (int i = 0; i < last; i++) {
                    boolean found = false;
                    for (int j = 0; j < CONTENT_LENGTH.length; j++) { //识别出Content-Length:
                        if (in.get(i + j) != CONTENT_LENGTH[j]) {
                            found = false;
                            break;
                        }
                        found = true;
                    }
                    if (found) {
                        // retrieve value from this position till next 0x0D 0x0A
                        StringBuilder contentLength = new StringBuilder();
                        for (int j = i + CONTENT_LENGTH.length; j < last; j++) {
                            if (in.get(j) == 0x0D) {
                                break;
                            }
                            contentLength.append(new String(
                                    new byte[] { in.get(j) }));
                        }
                        // if content-length worth of data has been received then
                        // the message is complete
                        //数据长度=contentLength+eoh(body区前半部分长度)
                        return  Integer.parseInt(contentLength.toString().trim())+ eoh == in.remaining(); //数据收齐
                    }
                }
            }
            // the message is not complete and we need more data
            return false;
        }
        catch(Exception e){ //处理失败 简单返回false 直到发起接收超时
            return false;
        }

    }

	/*====判断是否收   原始代码  未精简  大并发时效率低  V1.0
	private boolean messageComplete(IoBuffer in) {
		try{
			int last = in.remaining() - 1;
			if (in.remaining() < 4) { //解码最少需要接收4字节
				System.out.println("<4");
				return false;
			}
			// to speed up things we check if the Http request is a GET or POST
			//get数据，末尾收到2个回车换行视为接收完成
			if (in.get(0) == (byte) 'G' && in.get(1) == (byte) 'E' && in.get(2) == (byte) 'T') {
				// Http GET request therefore the last 4 bytes should be 0x0D 0x0A
				// 0x0D 0x0A
				return in.get(last) == (byte) 0x0A && in.get(last - 1) == (byte) 0x0D
						&& in.get(last - 2) == (byte) 0x0A
						&& in.get(last - 3) == (byte) 0x0D;
			} else if (in.get(0) == (byte) 'P' && in.get(1) == (byte) 'O'
					&& in.get(2) == (byte) 'S' && in.get(3) == (byte) 'T') {
				// Http POST request
				// first the position of the 0x0D 0x0A 0x0D 0x0A bytes
				int eoh = -1;
				for (int i = last; i > 2; i--) { //末尾是连续2个回车换行
					if (in.get(i) == (byte) 0x0A && in.get(i - 1) == (byte) 0x0D
							&& in.get(i - 2) == (byte) 0x0A
							&& in.get(i - 3) == (byte) 0x0D) {
						eoh = i + 1;
						break;
					}
				}
				if (eoh == -1) {
					return false;
				}
				for (int i = 0; i < last; i++) {
					boolean found = false;
					for (int j = 0; j < CONTENT_LENGTH.length; j++) { //识别出Content-Length:
						if (in.get(i + j) != CONTENT_LENGTH[j]) {
							found = false;
							break;
						}
						found = true;
					}
					if (found) {
						// retrieve value from this position till next 0x0D 0x0A
						StringBuilder contentLength = new StringBuilder();
						for (int j = i + CONTENT_LENGTH.length; j < last; j++) {
							if (in.get(j) == 0x0D) {
								break;
							}
							contentLength.append(new String(
									new byte[] { in.get(j) }));
						}
						// if content-length worth of data has been received then
						// the message is complete
						//数据长度=contentLength+eoh(body区前半部分长度)
						return  Integer.parseInt(contentLength.toString().trim())+ eoh == in.remaining(); //数据收齐  
					}
				}
			}
			// the message is not complete and we need more data
			return false;
		}
		catch(Exception e){ //处理失败 简单返回false 直到发起接收超时
			return false;
		}

	}
	==========================*/


    private HttpRequestMessage decodeBody(IoBuffer in) {
        request = new HttpRequestMessage();
//		String recvdata="";
//		try{
//			recvdata=in.getString(decoder);
//			//能正确转为utf-8 则继续
//		}
//		catch (CharacterCodingException ex)
//		{
//			/*
//			//vc客户端当软件名称为中文时
//			recvdata=new String(in.array()); //重新取值
//			recvdata=recvdata.replaceAll("Lark7618资源管理工具", "entryhelper");	
//			try {
//				recvdata= new String(recvdata.getBytes(), "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}*/
//		}


        //vc客户端中文程序名时in.getString(decoder)))解码失败
        try {
            request.setHeaders(parseRequest(new StringReader(in.getString(decoder))));
//			request.setHeaders(parseRequest(new StringReader(in.getString(Charset.forName("iso-8859-1").newDecoder()))));
            return request;
        }
        catch (CharacterCodingException e)
        {
            //System.out.println(e.getMessage());
        }
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("@error", new String[] {"error"}); //解包出错则人为设置error
        request.setHeaders(map);
        //出现异常 则request对象的headers为null,上层模块应判断headers是否为null空
        return request;
    }

    /*  在nginx中配置
     *    server {
            listen       80;
            server_name  localhost;
            location / {
                    proxy_connect_timeout   3;
                    proxy_send_timeout      30;
                    proxy_read_timeout      30;
                proxy_pass http://localhost;
                proxy_method POST;
                proxy_redirect    off;
                             proxy_set_header  Host             $host;
                             proxy_set_header  X-Real-IP        $remote_addr;
                             proxy_set_header  X-Forwarded-For  $proxy_add_x_forwarded_for;
            }
   }
       会在请求头中增加真实IP
     X-Real-IP: 192.168.1.111
     X-Forwarded-For: 192.168.1.111
     调用 request.get("X-Real-IP")得到真实ip
     */
    private Map<String, String[]> parseRequest(StringReader is) {
        Map<String, String[]> map = new HashMap<String, String[]>();
        BufferedReader rdr = new BufferedReader(is);

        try {
            // Get request URL.
            String line = rdr.readLine();
            String[] url = line.split(" ");
            if (url.length < 3) {
                return map;
            }

            map.put("URI", new String[] { line });
            map.put("Method", new String[] { url[0].toUpperCase() });
            map.put("Context", new String[] { url[1].substring(1) }); //substring(1)  去掉空格
            map.put("Protocol", new String[] { url[2] });
            // Read header
            boolean  flag=false;
            while ((line = rdr.readLine()) != null && line.length() > 0) {
                StringBuffer  body=new StringBuffer();
                String[] tokens = line.split(": ");
                map.put(tokens[0], new String[] { tokens[1] });
            }
            //System.out.println("xunhuan over");
            // If method 'POST' then read Content-Length worth of data
            if (url[0].equalsIgnoreCase("POST")) {
                int len = Integer.parseInt(map.get("Content-Length")[0]);
                if(len>0){
                    char[] buf = new char[len];
                    int  readLen=rdr.read(buf);
                    //if (rdr.read(buf) == len) {
                    line = String.copyValueOf(buf);
                }
                else
                    line=null;

            } else if (url[0].equalsIgnoreCase("GET")) { //不支持get 屏蔽代码
				/*
				int idx = url[1].indexOf('?');
				if (idx != -1) {
					map.put("Context",
							new String[] { url[1].substring(1, idx) });
					line = url[1].substring(idx + 1);
				} else {
					line = null;
				}*/
                return map;
            }

            if (line != null) {
				/*body数据形如 xx=xxxx的处理 目前云宝不存在这种情况
				String[] match = line.split("\\&");
				for (String element : match) {
					String[] params = new String[1];
					String[] tokens = element.split("=");
					switch (tokens.length) {
					case 0:
						map.put("@".concat(element), new String[] {});
						break;
					case 1:
						//map.put("@".concat(tokens[0]), new String[] {});
						map.put("@body", new String[] {tokens[0]}); //post的body数据
						break;
					default:
						String name = "@".concat(tokens[0]);
						if (map.containsKey(name)) {
							params = map.get(name);
							String[] tmp = new String[params.length + 1];
							for (int j = 0; j < params.length; j++) {
								tmp[j] = params[j];
							}
							params = null;
							params = tmp;
						}
						params[params.length - 1] = tokens[1].trim();
						map.put(name, params);
					}
				}*/
                //改为直接设置body值
                map.put("@body", new String[] {line});
            }
        } catch (IOException ex) {
            //ex.printStackTrace();
            map.put("@error", new String[] {"error"});
        }
        catch (Exception ex) {
            //ex.printStackTrace();
            map.put("@error", new String[] {"error"});
        }
        return map;
    }

}
