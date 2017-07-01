package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.TableGroups;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.kit.JsonKit;

/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class TableGroupsController extends BaseController{
    /**
     * 获取所有填报表格分组
     */
    @Before(GET.class)
    @ActionKey("/api/hwc/tablegroups")
    public void getTableGroups(){
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "获取所有填报表格分组成功", TableGroups.getTableGroups()));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "获取所有填报表格分组成功", TableGroups.getTableGroups()));
        }
    }
}
