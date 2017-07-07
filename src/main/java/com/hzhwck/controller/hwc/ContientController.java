package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.interceptor.LoginInterceptor;
import com.hzhwck.model.hwc.Contient;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.kit.JsonKit;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/7/1.
 */
@Clear(LoginInterceptor.class)
public class ContientController extends BaseController{

    /**
     * 获取大洲
     */
    @Before(GET.class)
    @ActionKey("/api/hwc/continents")
    public void getContients(){
        List<Contient> data = Contient.getAll();
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "获取大洲", data));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "获取大洲", data));
        }
    }
}
