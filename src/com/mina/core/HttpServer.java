package com.mina.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HttpServer {
    private static final Log log = LogFactory.getLog(HttpServer.class.getName());
    /**
     * Default HTTP port
     */
    private static final int DEFAULT_PORT = 8080;
    private NioSocketAcceptor acceptor;
    private boolean isRunning;

    private String encoding;
    private HttpHandler httpHandler;

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
        HttpRequestDecoder.defaultEncoding = encoding;
        HttpResponseEncoder.defaultEncoding = encoding;
    }

    public HttpHandler getHttpHandler() {
        return httpHandler;
    }

    public void setHttpHandler(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    /**
     * 启动HTTP服务端箭筒HTTP请求
     *
     * @param port 要监听的端口号
     * @throws IOException
     */
    public void run(int port) throws IOException {
        synchronized (this) {
            if (isRunning) {
                log.warn("Server is already running.");
                return;
            }
            acceptor = new NioSocketAcceptor();
            acceptor.getFilterChain().addLast(
                    "protocolFilter",
                    new ProtocolCodecFilter(
                            new HttpServerProtocolCodecFactory()));

            //用固定大小线程池，并发性能得到极大提升 是不是设为200  就最多只有200个连进来
            //java.util.concurrent.Executor threadPool = Executors.newFixedThreadPool(1000);  //根据需要调整值
            java.util.concurrent.Executor threadPool = Executors.newFixedThreadPool(1000);  //根据需要调整值
            //  java.util.concurrent.Executor threadPool = Executors.newCachedThreadPool();
            //加入过滤器（Filter）到Acceptor
            acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(threadPool));

            acceptor.getFilterChain().addLast("logger", new LoggingFilter());  //会打印request的原始信息，上线时可去掉日志
            acceptor.getSessionConfig().setReadBufferSize(800);
            acceptor.getSessionConfig().setMinReadBufferSize(600);
            acceptor.getSessionConfig().setMaxReadBufferSize(2048);
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);


            //acceptor.getSessionConfig().setSoLinger(0); //此参数设为0会导致app的http请求得不到响应   待分析


            //acceptor.getSessionConfig().setSendBufferSize(3000);

            acceptor.setBacklog(2000); //设置最大连接数  这个参数不起作用


            HttpServerHandler handler = new HttpServerHandler();
            FileReceiveHandler fileReceiveHandler = new FileReceiveHandler();
            handler.setHandler(httpHandler);
//            acceptor.setHandler(handler);
            acceptor.setHandler(fileReceiveHandler);


            acceptor.bind(new InetSocketAddress(port));
            isRunning = true;
            log.info("Server now listening on port " + port);
        }
    }

    /**
     * 使用默认端口8080
     *
     * @throws IOException
     */
    public void run() throws IOException {
        run(DEFAULT_PORT);
    }

    /**
     * 停止监听HTTP服务
     */
    public void stop() {
        synchronized (this) {
            if (!isRunning) {
                System.out.println("Server is already stoped.");
                return;
            }
            isRunning = false;
            try {
                acceptor.unbind();
                acceptor.dispose();
                System.out.println("Server is stoped.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-port")) {
                port = Integer.parseInt(args[i + 1]);
            }
        }

        try {
            // Create an acceptor
            NioSocketAcceptor acceptor = new NioSocketAcceptor();

            // Create a service configuration
            acceptor.getFilterChain().addLast(
                    "protocolFilter",
                    new ProtocolCodecFilter(
                            new HttpServerProtocolCodecFactory()));
            acceptor.getFilterChain().addLast("logger", new LoggingFilter());
            acceptor.setHandler(new HttpServerHandler());
            acceptor.bind(new InetSocketAddress(port));

            System.out.println("Server now listening on port " + port);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



/*知识点
 * 
 * 第一:

区分IoHandlerAdapter的sessionOpened和sessionCreated两个的区别,

sessionCreated:

Invoked from an I/O processor thread when a new connection has been created. Because this method is supposed to be called from the same thread that handles I/O of multiple sessions, please implement this method to perform tasks that consumes minimal amount of time such as socket parameter and user-defined session attribute initialization.

这个方法是在I/O处理阶段连接建立完成时调用的,这个线程同时要处理很多其他的会话进程,所以这个方法的实现,调用成本会很高,尽量不要在这里做代码实现

sessionOpened则是在另一个线程调用的,不会影响主I/O线程的效率.如果没有绝对的需要,最好用sessionOpened来代替sessionCreated做一些处理代码.

这部分处理完以后,mina的处理就可以流畅一些.

第二:

该动手时就动手

继承IoHandlerAdapter,可以实现很多方法,除了必须实现的几个,如messageReceived以外,,其他的也最好可以实现,当出现了不正常的情况时,当任务已经完成时,当发现Idle时,尽快断开客户端连接,避免线程堆积.

第三:

返回主题,这里涉及的就是如何合理的设置线程并发和线程队列了

NioSocketAcceptor在初始化的时候,可以指定并发数量的,这个值,在一些地方推荐的是CPU核心数 +1 ,但是在当今的服务器CPU处理效率上看,这个已经偏低了,当然,还需要根据业务的需求去设置.我增加了一下这个值.

为NioSocketAcceptor增加一个线程的处理池:acceptor.getFilterChain().addLast("ThreadPool",new ExecutorFilter(Executors.newCachedThreadPool()));

另外,backlog,也就是处理队列的大小,有些人可能认为这个队列越大越好.但是实际上,这个也需要考虑客户端的需求,客户端需要的是效率还是质量,客户端的等待是不是会带来问题.对于我的应用来说,客户端等待的时间越长,越容易产生问题.所以我减小了这个值.

经过这几个修改,感觉mina在处理的时候比以前要流畅了很多.



服务端和客户端设置的一些常用参数信息:
	1.IoSessionConfig getSessionConfig() 获得IoSession的配置对象IoSessionConfig，通过它可以设置Socket连接的一些选项。
	a. void setReadBufferSize(int size) 这个方法设置读取缓冲的字节数，但一般不需要调用这个方法，
	       因为IoProcessor 会自动调整缓冲的大小。你可以调用setMinReadBufferSize()、setMaxReadBufferSize()方法，
	       这样无论 IoProcessor 无论如何自动调整，都会在你指定的区间
	       
	       Executors.newCachedThreadPool(); 这个方法就是根据需要来创建线程。过多的请求（每一个请求执行时间较长）处理不了就会增加新的线程。
newFixedThreadPool 就是设置固定线程来处理的，过多的请求(每一个请求执行时间较长)就会导致后续请求延迟非常大。
 根据你这种情况，应该将接受到的请求加入到另一个线程池的队列中，由这个线程池来处理这些请求。
  mina执行业务逻辑不应该被查询数据阻塞。所以查询数据必须由其他几个线程来执行。 
	       */

