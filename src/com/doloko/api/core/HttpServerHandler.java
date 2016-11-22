package com.doloko.api.core;

import com.alibaba.fastjson.JSONException;
import com.doloko.api.core.exception.InfException;
import com.doloko.api.route.exception.RouteNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;

public class HttpServerHandler extends IoHandlerAdapter {
    private static final Log log = LogFactory.getLog(HttpServerHandler.class);
    private HttpHandler handler;

    public HttpHandler getHandler() {
        return handler;
    }

    public void setHandler(HttpHandler handler) {
        this.handler = handler;
    }


    //	@Override
//    public void sessionCreated(IoSession session) throws Exception {   
//        SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();   
//        cfg.setReceiveBufferSize(1024);   
//        cfg.setReadBufferSize(1024);   
//        cfg.setKeepAlive(true);   
//        
//         /*网上解决方案cfg.setSoLinger(0);可解决TIME_WAIT过多问题 但发现客户端会出现conticon  teset
//          * 暂时不调用此句  避免连接被reset导致客户端收不到数据
//          * 
//          */
//        
//         cfg.setSoLinger(0); //   解决系统的TIME_WAIT过多问题
//    } 
    @Override
    public void sessionOpened(IoSession session) {
        // set idle time to 60 seconds
        //System.out.println("session open");
        //session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10); //10秒没数据自动关闭连接   考虑配置为参数
    }

	/*连接关闭，做收尾工作
    @Override
	public void sessionClosed(IoSession session) {
		System.out.println("session closed");
	}*/

    //old
    /*
	public void messageReceived(IoSession session, Object message) {
		// Check that we can service the request context
	   //在使用nginx情况下 这里获取的是nginx服务器的ip
		try{
			//System.out.println("messageReceived====");
			String clientIP = ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
			if(message==null){
				//System.out.println("message==null  close");
				session.close(false);
			}
			HttpRequestMessage request = (HttpRequestMessage) message;
			//if(request.getHeaders()!=null)肯定不为空
			String[] realIp=request.getHeader("X-Real-IP"); //启用nginx获取真实ip
			if( realIp!=null &&  !"".equals(realIp[0])){
				clientIP=realIp[0];
				//System.out.println("clientIP="+clientIP);
			}
			request.getHeaders().put("ip", new String[]{clientIP});
			//System.out.println("clientIP="+clientIP);
			HttpResponseMessage response = handler.handle(request);
			//System.out.println("handler.handle  over");
			if (response != null) 
				session.write(response).addListener(IoFutureListener.CLOSE); //socket发送回应 关闭连接
			else
				session.close(false);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			session.close(false);
		}
	}
	*/


    @Override
    public void messageReceived(IoSession session, Object message) {
        // Check that we can service the request context
        //在使用nginx情况下 这里获取的是nginx服务器的ip
        try {
            //System.out.println("messageReceived====");
            String clientIP = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
            //String clientIP="127.0.0.1";
            if (message == null) {
                //logger.error("received message from client [" + session.getRemoteAddress() + "] is null.");
            } else {
                HttpRequestMessage request = (HttpRequestMessage) message;
                //if(request.getHeaders()!=null)肯定不为空
                String[] realIp = request.getHeader("X-Real-IP"); //启用nginx获取真实ip
                if (realIp != null && !"".equals(realIp[0])) {
                    clientIP = realIp[0];
                    //System.out.println("clientIP="+clientIP);
                }
                request.getHeaders().put("ip", new String[]{clientIP});
                //System.out.println("clientIP="+clientIP);
                HttpResponseMessage response = handler.handle(request);
                //System.out.println("handler.handle  over");
                if (response != null) {
                    if (session.isConnected()) {
                        //session.write(response).addListener(IoFutureListener.CLOSE); //socket发送回应 关闭连接
                        session.write(response);
                        //Thread.sleep(10); 等待客户端接收?
                    } else {
                        //在finally中关闭
                        //session.close(true);
                        //logger.debug("client is close ,reply stop");
                    }
                } else {
                    //logger.debug("response  is null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //	e.printStackTrace();
            HttpResponseMessage response = new HttpResponseMessage();
            response.setContentType("application/json");
            if (e instanceof InfException)
                response.setResponseCode(HttpResponseMessage.HTTP_STATUS_EXCEPTION);
            else if (e instanceof RouteNotFoundException)
                response.setResponseCode(HttpResponseMessage.HTTP_STATUS_NOT_FOUND);
            else if (e instanceof JSONException)
                response.setResponseCode(HttpResponseMessage.HTTP_STATUS_BAD_REQUEST);
            if (session.isConnected()) {
                //session.write(response).addListener(IoFutureListener.CLOSE); //socket发送回应 关闭连接
                session.write(response);
                //Thread.sleep(10); 等待客户端接收?
            }
            //logger.error("mina [" + session.getRemoteAddress() + "] error", e);
        } finally {

            if (session.isConnected())
                session.close(false);  //等socket发送完毕再关闭socket连接
            else
                session.close(true);    //为立即关闭socket..*/
            //session.close(false);
        }
    }


    //接收超时  10秒没接收到数据运行至此，连接关闭
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        //测试时注释掉
        System.out.println("timueout closed");
        session.close(false);
    }

    //socket异常，如在socket建立后强制终止客户端程序，则提示远程主机强迫关闭了一个现有的连接
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        //System.out.println(cause.getMessage());
        try {
            if (session != null) {
                //logger.error("Connection [" + session.getRemoteAddress() + "] Break," + cause, cause);
            } else {
                //logger.error("iosession error,iosession is null!" + cause, cause);
            }
        } catch (Exception e) {
            //logger.error("mina [" + session.getRemoteAddress() + "] error", e);
        } finally {
            session.close(false);
        }
    }
}
