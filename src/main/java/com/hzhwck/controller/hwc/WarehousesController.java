package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.*;
import com.hzhwck.myEnum.CodeType;
import com.hzhwck.myEnum.HwcUserType;
import com.hzhwck.myEnum.TableNames;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/12.
 */
public class WarehousesController extends BaseController{

    @ActionKey("/api/hwc/warehouses")
    public void test(){
        boolean success = false;
        if(getRequest().getMethod().equals("POST")){
            System.out.println("[POST] add");
            //forwardAction("/api/hwc/warehouses/add");
            redirect("/api/hwc/warehouses/add" + "?" + getParam());
            success = true;
        }else if(getRequest().getMethod().equals("GET")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[GET] show id -- " + id);
                forwardAction("/api/hwc/warehouses/id/" + id);
                success = true;
            }else {
                System.out.println("[GET] show list");
                forwardAction("/api/hwc/warehouses/search");
                success = true;
            }
        }else if(getRequest().getMethod().equals("PATCH")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[PATCH] update id -- " + id);
                //forwardAction("/api/hwc/warehouses/modify/" + id);
                redirect("/api/hwc/warehouses/modify/" + id + "?" + getParam());
                success = true;
            }
        }else if(getRequest().getMethod().equals("DELETE")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[DELETE] id -- " + id);
                forwardAction("/api/hwc/warehouses/delete/" + id);
                success = true;
            }
        }
        if(success == false){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.paramError, "url出错或者请求方法出错", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes(CodeType.paramError, "url出错或者请求方法出错", null));
            }
        }
    }

    /**
     * 添加海外仓库信息
     */
    @ActionKey("/api/hwc/warehouses/add")
    public void add(){
        Warehouses warehouses = getModel(Warehouses.class, "warehouse");
        Map<String, Object> loginUser = getSessionAttr("user");
        User user = User.dao.findById(loginUser.get("hwcUserId"));
        warehouses.set("sampleId", user.get("sampleId"));
        //warehouses.set("status", 1);
        if(warehouses.save() == false){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加海外仓库失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加海外仓库失败，数据库异常", null));
            }
            return ;
        }
        if(user.get("warehouseIds") == null) {
            user.set("warehouseIds", warehouses.get("id"));
        }else {
            user.set("warehouseIds", user.get("warehouseIds") + "," + warehouses.get("id"));
        }
        loginUser.put("warehouseIds", user.get("warehouseIds"));
        if(User.modify(user) == null){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加海外仓库失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加海外仓库失败，数据库异常", null));
            }
            warehouses.delete();
            return ;
        }
        Warehouses w = Warehouses.dao.findById(warehouses.get("id"));
        w.put("ssgj", Country.getByCode(w.get("hwcssgj").toString()));
        w.put("ssz", Contient.getByCode(w.get("hwcssz").toString()));
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "添加海外仓库成功", w));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "添加海外仓库成功", w));
        }
    }

    /**
     * 修改海外仓库信息
     * @Param id 海外仓id
     */
    @ActionKey("/api/hwc/warehouses/modify")
    public void modify(){
        Map<String, Object> loginUser = getSessionAttr("user");
        Warehouses warehouses = getModel(Warehouses.class, "warehouse");
        String id = getPara(0);
        warehouses.set("id", id);
        if(warehouses.update() == false){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "修改海外仓库失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "修改海外仓库失败，数据库异常", null));
            }
            return ;
        }
        Warehouses w = Warehouses.dao.findById(warehouses.get("id"));
        w.put("ssgj", Country.getByCode(w.get("hwcssgj").toString()));
        w.put("ssz", Contient.getByCode(w.get("hwcssz").toString()));
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "更新海外仓库成功", w));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "更新海外仓库成功", w));
        }
    }

    /**
     * 删除海外仓
     * @Param id
     */
    @ActionKey("/api/hwc/warehouses/delete")
    public void delete(){
        boolean success = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                String id = getPara(0);
                Warehouses warehouse = Warehouses.dao.findById(id);
                String serviceCompanyIds = warehouse.getStr("serviceCompanyIds");
                if(serviceCompanyIds != null) {
                    for (String serviceCompanyId : serviceCompanyIds.split(",")) {
                        if (ServiceCompanys.dao.deleteById(serviceCompanyId) == false) return false;
                    }
                }
                List<Reports> reports = Reports.findByWarehouseId(id);
                for(Reports report : reports){
                    if(report.delete() == false)    return false;
                }
                if(warehouse.delete() == false)     return false;
                Map<String, Object> loginUser = getSessionAttr("user");
                String[] warehouseIds = ((String)loginUser.get("warehouseIds")).split(",");
                String res = "";
                int j = 0;
                for(int i = 0;i < warehouseIds.length;++i){
                    if(warehouseIds[i].equals(id))  continue;
                    ++j;
                    res += warehouseIds[i];
                    if(j != warehouseIds.length - 1)    res += ",";
                }
                User user = User.dao.findById(loginUser.get("hwcUserId"));
                if(res.equals(""))      res = null;
                user.set("warehouseIds", res);
                if(user.update() == false)  return false;
                return true;
            }
        });
        if(success == false){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "删除海外仓失败，数据库异常", null));
                renderJson(getPara(CodeType.dataBaseError, "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("01", "删除海外仓失败，数据库异常", null));
            }
            return ;
        }else {
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "删除海外仓成功", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes(CodeType.success, "删除海外仓成功", null));
            }
        }
    }

    /**
     * 获取企业用户自己所拥有的海外仓信息
     */
    /*@ActionKey("/api/hwc/warehouse")
    public void getWarehouses(){
        Map<String, Object> loginUser = getSessionAttr("user");
        String warehouseIds = (String)loginUser.get("warehouseIds");
        List<Warehouses> warehouses = new LinkedList<Warehouses>();
        if(warehouseIds != null){
            for(String warehouseId : warehouseIds.split(",")){
                warehouses.add(Warehouses.dao.findById(warehouseId));
            }
        }
        renderJson(ResponseUtil.setRes("00", "获取企业用户自己所拥有的海外仓信息", warehouses));
    }*/

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
        String s = TableNames.hwcSamples.split(" ")[1] + ".";
        String w = TableNames.hwcWarehouses.split(" ")[1] + ".";
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);
        String oderBy = getPara("oderBy", w + "id");
        String oder = getPara("oder", "desc");
        String filter = "";
        filter = " where " + s + "id = " + w + "sampleId ";
        Map<String, Object> loginUser = getSessionAttr("user");
        if(loginUser.get("type").toString().equals(HwcUserType.sample)){
            Object ids = loginUser.get("warehouseIds");
            String xx;
            if(ids == null) xx = "-1";
            else
                xx = ids.toString();
            filter += " and " + w + "id in(" + ids + ") ";
        }else if(loginUser.get("type").toString().equals(HwcUserType.qxAdmin)){
            filter += " and " + s + "ssqx = '" + loginUser.get("areaCode") + "' ";
        }else if(loginUser.get("type").toString().equals(HwcUserType.admin)){
            String ssqx = getPara("areaCode");
            if(ssqx != null){
                filter += " and " + s + "ssqx = '" + ssqx + "' ";
            }
        }else if(loginUser.get("type").toString().equals(HwcUserType.yqadmin)){
            filter += " and " + s + "ssyq = '" + loginUser.get("yqCode") + "' ";
        }

        String country = getPara("country");
        if(country != null){
            filter += " and " + w + "hwcssgj = '" + country + "' ";
        }

        String contient = getPara("contient");
        if(contient != null){
            filter += " and " + w + "hwcssz = '" + contient + "' ";
        }

        String jsdwmc = getPara("jsdwmc");
        if(jsdwmc != null){
            filter += " and " + s + "jsdwmc like '%" + jsdwmc + "%' ";
        }

        String shtyxydm = getPara("shtyxydm");
        if(shtyxydm != null){
            filter += " and " + s + "shtyxydm like '%" + shtyxydm + "%' ";
        }

        String hwcmc = getPara("hwcmc");
        if(hwcmc != null){
            filter += " and " + w + "hwcmc like '%" + hwcmc + "%' ";
        }

        String status = getPara("status");
        if(status != null){
            filter += " and " + w + "status = " + status + " ";
        }

        Page<Warehouses> data = Warehouses.getPage(pageNumber, pageSize, oderBy, oder, filter, TableNames.hwcSamples + ", " + TableNames.hwcWarehouses);
        for(Warehouses warehouse : data.getList()){
            warehouse.put("sample", Samples.dao.findById(warehouse.get("sampleId")));
            warehouse.put("ssgj", Country.getByCode(warehouse.get("hwcssgj").toString()));
            warehouse.put("ssz", Contient.getByCode(warehouse.get("hwcssz").toString()));
        }
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "获取海外仓库信息成功", data));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes(CodeType.success, "获取海外仓库信息成功", data));
        }
    }

    /**
     * 根据id获取海外仓库信息
     */
    @ActionKey("/api/hwc/warehouses/id")
    public void getWarehouseById(){
        String id = getPara(0);
        Warehouses w = Warehouses.dao.findById(id);
        w.put("ssgj", Country.getByCode(w.get("hwcssgj").toString()));
        w.put("ssz", Contient.getByCode(w.get("hwcssz").toString()));
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "根据id获取海外仓库信息成功", w));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes(CodeType.success, "根据id获取海外仓库信息成功", w));
        }
    }

    /**
     * 获取海外仓经纬度
     */
    @Before(GET.class)
    @ActionKey("/api/hwc/warehouses/latandlng")
    public void getLatAndLng(){
        List<Warehouses> warehouses = Warehouses.getLatAndLng();
        for(Warehouses w : warehouses){
            w.put("jsdwmc", Samples.dao.findById(w.get("sampleId")).get("jsdwmc"));
        }
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "获取海外仓经纬度成功", warehouses));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes(CodeType.success, "获取海外仓经纬度成功", warehouses));
        }
    }
}
