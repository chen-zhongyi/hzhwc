package com.hzhwck.controller.hwc;

import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 陈忠意 on 2017/7/12.
 */
public class ImageHandler extends Handler {

    @Override
    public void handle(String target, HttpServletRequest request,
                       HttpServletResponse response, boolean[] isHandled) {
        /*int index = target.lastIndexOf("/api/simpleFileupload");
        if (target.contains("/api/simpleFileupload")) {
            System.out.println(target);
            if (request.getSession().getAttribute(Const.LOGINUSER) == null) {
                System.out.println("没有登陆!跳转到login.html");
                target = "/login.html";
            } else {
                System.out.println("登陆了，放行!");
            }
        }
        nextHandler.handle(target, request, response, isHandled);*/
    }

}
