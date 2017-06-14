package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.Tables;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.ActionKey;

/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class TablesController extends BaseController{

    /**
     * 获取所有填报表格
     */
    @ActionKey("/api/hwc/tables")
    public void getTables(){
        renderJson(ResponseUtil.setRes("00", "获取所有填报表格成功", Tables.getTables()));
    }
}
