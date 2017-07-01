package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.Yuanqu;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.kit.JsonKit;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/7/1.
 */
public class YuanquController extends BaseController{

    /**
     * 获取园区
     */
    @Before(GET.class)
    @ActionKey("/api/hwc/yuanqus")
    public void getYuanqu(){
        List<Yuanqu> data = Yuanqu.getAll();
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "获取园区成功",data));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "获取园区成功", data));
        }
    }
}
