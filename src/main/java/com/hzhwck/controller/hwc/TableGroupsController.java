package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.TableGroups;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.ActionKey;

/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class TableGroupsController extends BaseController{
    /**
     * 获取所有填报表格分组
     */
    @ActionKey("/api/hwc/tablegroups")
    public void getTableGroups(){
        renderJson(ResponseUtil.setRes("00", "获取所有填报表格分组成功", TableGroups.getTableGroups()));
    }
}
