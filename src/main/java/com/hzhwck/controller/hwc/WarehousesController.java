package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.User;
import com.hzhwck.model.hwc.Warehouses;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.ActionKey;

import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/12.
 */
public class WarehousesController extends BaseController{
    /**
     * 添加海外仓库信息
     */
    @ActionKey("/api/hwc/warehouses/add")
    public void add(){
        Warehouses warehouses = getModel(Warehouses.class, "warehouse");
        if(warehouses.save() == false){
            ResponseUtil.setRes("01", "添加海外仓库失败，数据库异常", null);
            return ;
        }
        Map<String, Object> loginUser = getSessionAttr("user");
        User user = User.dao.findById(loginUser.get("hwcUserId"));
        if(user.get("warehouseIds") == null) {
            user.set("warehouseIds", warehouses.get("id"));
        }else {
            user.set("warehouseIds", user.get("warehouseIds") + "," + warehouses.get("id"));
        }
        if(User.modify(user) == null){
            ResponseUtil.setRes("01", "添加海外仓库失败，数据库异常", null);
            warehouses.delete();
            return ;
        }
        renderJson(ResponseUtil.setRes("00", "添加海外仓库成功", warehouses));
    }

    /**
     * 修改海外仓库信息
     * @Param id 海外仓id
     */
    @ActionKey("/api/hwc/warehouses/modify")
    public void modify(){
        Map<String, Object> loginUser = getSessionAttr("user");
        Warehouses warehouses = getModel(Warehouses.class, "warehouse");
        String id = getPara("id");
        warehouses.set("id", id);
        if(warehouses.update() == false){
            ResponseUtil.setRes("01", "更新海外仓库失败，数据库异常", null);
            return ;
        }
        renderJson(ResponseUtil.setRes("00", "更新海外仓库成功", warehouses));
    }

    /**
     * 分页查询海外仓库信息
     * @Param pageNumber 第几页,默认值1
     * @Param pageSize 分页大小，默认值20
     * @Param orderBy 排序字段，默认id
     * @Param oder desc, asc
     * @Param filter 过滤信息，字符串【过滤字段:过滤信息,过滤字段:过滤信息..】,自动过滤不合法字段
     */
    @ActionKey("/api/hwc/warehouses/search")
    public void getPage(){
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);
        String oderBy = getPara("oderBy", "id");
        String oder = getPara("oder", "asc");
        String filter = getPara("filter", "");
        renderJson(ResponseUtil.setRes("00", "获取海外仓库信息成功",
                Warehouses.getPage(pageNumber, pageSize, oderBy, oder, filter)));
    }
}
