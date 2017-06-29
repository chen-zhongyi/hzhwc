package com.hzhwck.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * Created by 陈忠意 on 2017/6/24.
 */
public class CrossGlobleInterceptor implements Interceptor {

    public void intercept (Invocation ai){
        Controller c = ai.getController();
        if(c.getRequest().getMethod().equals("OPTIONS")){
            c.getResponse().setHeader("Access-Control-Allow-Origin", "*");
            c.getResponse().setHeader("Access-Control-Allow-Headers", "accept, content-type");
            c.getResponse().setHeader("Access-Control-Allow-Method", "POST");
            c.getResponse().setHeader("Access-Control-Allow-Method", "OPTIONS");
        }else {
            c.getResponse().setHeader("Access-Control-Allow-Origin", "*");
            c.getResponse().setHeader("Access-Control-Allow-Headers", "accept, content-type");
            c.getResponse().setHeader("Access-Control-Allow-Method", "POST");
            ai.invoke();
        }
    }
}
