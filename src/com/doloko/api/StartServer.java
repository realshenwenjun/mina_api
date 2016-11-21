package com.doloko.api;

import com.doloko.api.core.HttpServer;
import com.doloko.api.route.DefaultRoute;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by ASUS on 2016/11/18.
 */
public class StartServer {
    private static final Log log = LogFactory.getLog(StartServer.class.getName());

    private static final String[] configFiles = new String[]{
//            "spring/global-context.xml",
            "applicationContext.xml",
//            "spring/commserver-context.xml",
//            "spring/context_core_memcached.xml",
//            "spring/business-context.xml",
//            "spring/context_boss_quartz.xml",
//            "spring/system-dao-context.xml",
//            "spring/system-service-context.xml",
//            "spring/context_schedule_task.xml",
//            "spring/network-decrypt-context.xml"

    };
    private static ApplicationContext cfx;

    private static void initSpring() {
        log.info("正在加载spring 配置文件...");
//        try {
        cfx = new ClassPathXmlApplicationContext(configFiles);
//            YytxServerContext.getInstance().setCtx(new ClassPathXmlApplicationContext(configFiles));
//            YytxServerContext.getInstance().init();
//
//        } catch (BeansException e) {
//            e.printStackTrace();
//        }
        log.info("已成功加载spring 配置文件");
    }


    private static void initCommServerConfigration() {
        //待加
    }

    public static void main(String[] args) throws Exception {


        initSpring();
        initCommServerConfigration();


        //启动时获取一次accesstoken
//		try {
//			Xmly2016ResSyncUtil xmly2016ResSyncUtil = Xmly2016ResSyncUtil.getInstance();
//			xmly2016ResSyncUtil.getAccessToken();
//		} catch (Exception e) {
//			Ximalaya2016Constants.accesstokenstatus=0;
//			Ximalaya2016Constants.accesstoken="notknowen";
//		}

//        Xmly2016ResSyncUtil xmly2016ResSyncUtil = Xmly2016ResSyncUtil.getInstance();
//        xmly2016ResSyncUtil.getAccessToken();

        HttpServer server = new HttpServer();
        server.setEncoding("UTF-8");
        initSysData();  //初始化系统数据
        server.setHttpHandler(new DefaultRoute(cfx));
        server.run();
    }


    public static void initSysData() {
    }
}
