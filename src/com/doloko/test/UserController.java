package com.doloko.test;

import com.doloko.api.core.HttpRequestMessage;
import com.doloko.api.route.annotation.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ASUS on 2016/11/18.
 */

@Service
@Mapping
public class UserController {
    @Autowired
    private UserService userService;
    @Mapping("/user/regist")
    public String regist(HttpRequestMessage request){
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
