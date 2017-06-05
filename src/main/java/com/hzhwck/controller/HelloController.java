package com.hzhwck.controller;

import com.hzhwck.model.User;
import com.hzhwck.validator.UserValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/5/16.
 */
public class HelloController extends Controller{
    private static Logger log = Logger.getLogger(HelloController.class);

    @Before(UserValidator.class)
    public void index(){
        log.info("ok ");
        render("index.jsp");
    }

    public void getUser(){
        List<User> users = User.getUser();
        renderJson(users);
    }
}
