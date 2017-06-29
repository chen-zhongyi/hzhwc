package com.hzhwck.controller;

import com.hzhwck.interceptor.LoginInterceptor;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

/**
 * Created by 陈忠意 on 2017/5/16.
 */
@Clear(LoginInterceptor.class)
public class HelloController extends Controller{
    public void index(){
        render("index.html");
    }
}
