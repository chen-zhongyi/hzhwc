package com.hzhwck.controller.system;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.system.Rights;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.kit.JsonKit;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/8.
 */
public class RightsController extends BaseController{

    /**
     * 获取所有权限
     */
    @Before(GET.class)
    @ActionKey("/api/system/rights")
    public void getRights(){
        List<Rights> rights = Rights.getRights();
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "获取权限成功", rights));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "获取权限成功", rights));
        }
    }
}
