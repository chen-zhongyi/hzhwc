package com.hzhwck.controller.system;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.system.Role;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.ActionKey;

/**
 * Created by 陈忠意 on 2017/6/8.
 */
public class RoleController extends BaseController{

    /**
     * 获取系统所有角色
     */
    @ActionKey("/api/system/roles/all")
    public void getRoles(){
        renderJson(ResponseUtil.setRes("00", "获取系统角色成功", Role.getRoles()));
    }
    
}
