package com.hzhwck.controller.system;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.system.Rights;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.ActionKey;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/8.
 */
public class RightsController extends BaseController{

    /**
     * 获取所有权限
     */
    @ActionKey("/api/system/rights")
    public void getRights(){
        List<Rights> rights = Rights.getRights();
        renderJson(ResponseUtil.setRes("00", "获取权限成功", rights));
    }
}
