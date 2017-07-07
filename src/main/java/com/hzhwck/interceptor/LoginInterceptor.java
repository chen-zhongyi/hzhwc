package com.hzhwck.interceptor;

import com.hzhwck.myEnum.CodeType;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;

import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/24.
 */
public class LoginInterceptor implements Interceptor {

    public void intercept(Invocation ai){
        Controller c = ai.getController();
        Map<String, Object> login = c.getSessionAttr("user");
        if(login == null){
            if(c.getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.notLogin, "用户未登录", null));
                c.renderJson(c.getPara("callback", "default") + "(" + json + ")");
            }else {
                c.renderJson(ResponseUtil.setRes(CodeType.notLogin, "用户未登录", null));
            }
        }else {
            ai.invoke();
        }
    }
}
