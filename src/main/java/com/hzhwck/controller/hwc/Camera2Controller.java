package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.Camera2;
import com.hzhwck.model.hwc.Samples;
import com.hzhwck.model.hwc.Warehouses;
import com.hzhwck.myEnum.CodeType;
import com.hzhwck.myEnum.HwcUserType;
import com.hzhwck.myEnum.TableNames;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;

import java.util.Map;

/**
 * Created by 陈忠意 on 2017/7/11.
 */
public class Camera2Controller extends BaseController {
    @ActionKey("/api/hwc/cameras2")
    public void test(){
        boolean success = false;
        if(getRequest().getMethod().equals("POST")){
            if(getPara(0) == null) {
                System.out.println("[POST] add");
                //forwardAction("/api/hwc/cameras/add");
                redirect("/api/hwc/cameras2/add" + "?" + getParam());
                success = true;
            }
        }else if(getRequest().getMethod().equals("GET")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[GET] show id -- " + id);
                forwardAction("/api/hwc/cameras2/id/" + id);
                success = true;
            }else {
                System.out.println("[GET] show list");
                forwardAction("/api/hwc/cameras2/search");
                success = true;
            }
        }else if(getRequest().getMethod().equals("PATCH")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[PUT] update id -- " + id);
                //forwardAction("/api/hwc/cameras/modify/" + id);
                redirect("/api/hwc/cameras2/modify/" + id + "?" + getParam());
                success = true;
            }
        }else if(getRequest().getMethod().equals("DELETE")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[DELETE] id -- " + id);
                forwardAction("/api/hwc/cameras2/delete/" + id);
                success = true;
            }
        }
        if(success == false){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.paramError, "url出错或者请求方法出错", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.paramError, "url出错或者请求方法出错", null));
            }
        }
    }

    /**
     * 添加海外仓视频监控
     */
    @ActionKey("/api/hwc/cameras2/add")
    public void add(){
        Camera2 camera = new Camera2()
                .set("warehouseId", getPara("warehouseId"))
                .set("name", getPara("name"))
                .set("video", getPara("video"));
        if(camera.save() == false){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加海外仓视频监控失败，数据库异常，请联系管理员", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加海外仓视频监控失败，数据库异常，请联系管理员", null));
            }
        }else{
            Warehouses w = Warehouses.dao.findById(getPara("warehouseId"));
            camera.put("warehouse", w);
            camera.put("sample", Samples.dao.findById(w.get("sampleId")));
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "添加海外仓视频监控成功", camera));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.success, "添加海外仓视频监控成功", camera));
            }
        }
    }

    /**
     * 修改海外仓视频监控
     */
    @ActionKey("/api/hwc/cameras2/modify")
    public void modify(){
        Camera2 camera = new Camera2()
                .set("id", getPara(0));
        boolean chance = false;
        String name = getPara("name");
        if(name != null){
            camera.set("name", name);
            chance = true;
        }
        String video = getPara("video");
        if(video != null){
            camera.set("video", video);
            chance = true;
        }
        if(chance == true && camera.update() == false){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "修改海外仓视频监控失败，数据库异常，请联系管理员", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "修改海外仓视频监控失败，数据库异常，请联系管理员", null));
            }
        }else {
            camera = Camera2.dao.findById(getPara(0));
            Warehouses w = Warehouses.dao.findById(getPara("warehouseId"));
            camera.put("warehouse", w);
            camera.put("sample", Samples.dao.findById(w.get("sampleId")));
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "修改海外仓视频监控成功", camera));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.success, "修改海外仓视频监控成功", camera));
            }
        }
    }

    /**
     * 根据id查询camera
     */
    @ActionKey("/api/hwc/cameras2/id")
    public void getCameraById(){
        Camera2 camera = Camera2.dao.findById(getPara(0));
        Warehouses w = Warehouses.dao.findById(camera.get("warehouseId"));
        camera.put("warehouse", w);
        camera.put("sample", Samples.dao.findById(w.get("sampleId")));
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "根据id查询视频监控成功", camera));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "根据id查询视频监控成功", camera));
        }
    }

    /**
     * 根据id删除视频监控
     */
    @ActionKey("/api/hwc/cameras2/delete")
    public void delete(){
        Camera2.dao.deleteById(getPara(0));
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "根据id删除视频监控成功", null));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "根据id删除视频监控成功", null));
        }
    }

    /**
     * 视频监控搜索
     */
    @ActionKey("/api/hwc/cameras2/search")
    public void search(){
        Map<String, Object> loginUser = getSessionAttr("user");
        String s = TableNames.hwcSamples.split(" ")[1] + ".";
        String w = TableNames.hwcWarehouses.split(" ")[1] + ".";
        String sc = TableNames.hwcCameras2.split(" ")[1] + ".";
        String filter = " where " + sc + "warehouseId = " + w + "id and " + w + "sampleId = " + s + "id ";
        if(loginUser.get("type").toString().equals(HwcUserType.sample)){
            Samples sample = (Samples) loginUser.get("sample");
            if(sample == null)
                filter += " and " + s + "id = -1 " ;
            else
                filter += " and " + s + "id = " + sample.get("id") + " ";
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
        String name = getPara("name");
        if(name != null){
            filter += " and " + sc + "name like '%" + name + "%' ";
        }
        Page<Camera2> cameras = Camera2.getPage(getParaToInt("pageNumber", 1), getParaToInt("pageSize", 20),
                sc + "id", "desc", filter,
                TableNames.hwcSamples + ", " + TableNames.hwcCameras2 + ", " +TableNames.hwcWarehouses);

        for(Camera2 camera : cameras.getList()) {
            Warehouses warehouse = Warehouses.dao.findById(camera.get("warehouseId"));
            camera.put("warehouse", warehouse);
            camera.put("sample", Samples.dao.findById(warehouse.get("sampleId")));
        }
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "视频监控搜索", cameras));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "视频监控搜索", cameras));
        }
    }
}
