package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.interceptor.LoginInterceptor;
import com.hzhwck.model.hwc.Systems;
import com.hzhwck.myEnum.CodeType;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.kit.JsonKit;

/**
 * Created by 陈忠意 on 2017/7/4.
 */
@Clear(LoginInterceptor.class)
public class SystemsController extends BaseController{

    /**
     * 获取海外仓系统模块
     */
    @Before(GET.class)
    @ActionKey("/api/hwc/systems")
    public void getSystems(){
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "获取海外仓系统模块成功", Systems.findAll()));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "获取海外仓系统模块成功",  Systems.findAll()));
        }
    }
}
