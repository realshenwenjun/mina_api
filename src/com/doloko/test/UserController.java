package com.doloko.test;

import com.mina.core.HttpRequestMessage;
import com.mina.route.annotation.Mapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ASUS on 2016/11/18.
 */

@Service
@Mapping
public class UserController {
    Logger logger = Logger.getLogger(UserController.class);
    private static final Log log = LogFactory.getLog(UserController.class.getName());
    @Autowired
    private UserService userService;
    @Mapping("/user/regist")
    public String regist(HttpRequestMessage request){
        logger.info("logf4j");
        log.info("common log");
        userService.say();
        return "成功了";
    }

    @Mapping("/user/login")
    public String login(HttpRequestMessage request){
        userService.say();
        String q = request.getParameter("e");
        return "{\"id\":"+q+"}";
    }
}
