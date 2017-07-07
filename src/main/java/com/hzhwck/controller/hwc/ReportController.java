package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.*;
import com.hzhwck.myEnum.HwcUserType;
import com.hzhwck.myEnum.TableNames;
import com.hzhwck.util.ResponseUtil;
import com.hzhwck.util.TimeUtil;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/16.
 */
public class ReportController extends BaseController{

    @ActionKey("/api/hwc/reports")
    public void test(){
        boolean success = false;
        if(getRequest().getMethod().equals("POST")){
            System.out.println("[POST] add");
            forwardAction("/api/hwc/reports/add");
            success = true;
        }else if(getRequest().getMethod().equals("GET")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[GET] show id -- " + id);
                forwardAction("/api/hwc/reports/id/" + id);
                success = true;
            }else {
                System.out.println("[GET] show list");
                forwardAction("/api/hwc/reports/search");
                success = true;
            }
        }else if(getRequest().getMethod().equals("PUT")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[PUT] update id -- " + id);
                forwardAction("/api/hwc/reports/modify/" + id);
                success = true;
            }
        }else if(getRequest().getMethod().equals("DELETE")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[DELETE] id -- " + id);
                //success = true;
            }
        }
        if(success == false){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("02", "url出错或者请求方法出错", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("02", "url出错或者请求方法出错", null));
            }
        }
    }

    /**
     * 获取分页报表
     * @Param pageNumber 第几页,默认值1
     * @Param pageSize 分页大小，默认值20
     * @Param orderBy 排序字段，默认id
     * @Param oder desc, asc
     * @Param filter 过滤信息，字符串【过滤字段:过滤信息,过滤字段:过滤信息..】,自动过滤不合法字段
     */
    @ActionKey("/api/hwc/reports/search")
    public void getPage(){
        String hr = TableNames.hwcReports.split(" ")[1] + ".";
        String hs = TableNames.hwcSamples.split(" ")[1] + ".";

        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);
        String oderBy = getPara("oderBy", "id");
        String oder = getPara("oder", "asc");
        String filter = "";

        //报表记录字段过滤
        String sampleId = getPara("sampleId");
        Map<String, Object> loginUser = getSessionAttr("user");
        if(loginUser.get("type").toString().equals(HwcUserType.sample)){
            //企业用户
            Samples sample = (Samples) loginUser.get("sample");
            sampleId = sample != null ? sample.get("id").toString() : "";
        }else if(loginUser.get("type").toString().equals(HwcUserType.admin)){
            //评审员用户
        }
        if(sampleId != null){
            filter += " where " + hr + "sampleId = " + sampleId + " ";
        }
        String tableId = getPara("tableId");
        if(tableId != null){
            if(filter.equals(""))   filter = " where " + hr + "tableId = " + tableId + " ";
            else
                filter += "  and " + hr + "tableId = " + tableId + " ";
        }
        //String round = getPara("round");
        String status = getPara("status");
        if(status != null){
            if(filter.equals(""))   filter = " where " + hr + "status = " + status + " ";
            else
                filter += " and " + hr + "status = " + status + " " ;
        }

        //样本企业过滤
        if(filter.equals(""))   filter = " where " + hr + "sampleId = " + hs + "id ";
        else
            filter += " and " + hr + "sampleId = " + hs + "id ";
        String jsdwmc = getPara("jsdwmc");
        if(jsdwmc != null){
            filter += " and " + hs + "jsdwmc like '%" + jsdwmc + "%' " ;
        }
        String shtyxydm = getPara("shtyxydm");
        if(shtyxydm != null){
            filter += " and " + hs + "shtyxydm like '%" + shtyxydm + "%' " ;
        }

        Page<Reports> reports =  Reports.getPage(pageNumber, pageSize, oderBy, oder, filter, ", " + TableNames.hwcSamples);
        for(Reports report : reports.getList()){
            report.put("table", Tables.dao.findById(report.get("tableId")));
            report.put("sample", Samples.dao.findById(report.get("sampleId")));
            report.put("plan", ReportPlans.getReportPlansAndTableGroupsById(report.get("planId").toString()));
            if(report.get("warehouseId") == null)   continue;
            report.put("warehouse", Warehouses.dao.findById(report.get("warehouseId")));
        }
        renderJson(ResponseUtil.setRes("00", "获取分页报表成功",
                reports));
    }

    /**
     * 表报填写
     * @Param id
     */
    @ActionKey("/api/hwc/reports/add")
    public void add(){
        String planId = getPara("planId");
        String sampleId = getPara("sampleId");
        String tableId = getPara("tableId");
        String warehouseId = getPara("warehouseId");
        Tables table = Tables.dao.findById(tableId);
        Reports report = new Reports().set("planId", planId)
                .set("sampleId", sampleId)
                .set("tableId", tableId)
                .set("createBy", ((Map<String, Object>)getSessionAttr("user")).get("hwcUserId"))
                .set("createAt", TimeUtil.getNowTime())
                .set("status", 0)
                .set("tableNo", table.get("theNo"));
        if(table.getInt("isRelatedWithWarehouse") == 1){
            report.set("warehouseId", warehouseId);
        }
        if(report.save() == false){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("01", "添加报表失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("01", "添加报表失败，数据库异常", null));
            }
            return ;
        }
        Record r = Reports.dao.findById(report.get("id")).toRecord();
        r.set("table", Tables.dao.findById(r.get("tableId")));
        r.set("sample", Samples.dao.findById(r.get("sampleId")));
        r.set("plan", ReportPlans.dao.findById(r.get("planId")));
        r.set("warehouse", Warehouses.dao.findById(r.get("warehouseId")));
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "表报填写成功",r));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "表报填写成功", r));
        }
    }

    /**
     * 表报修改
     * @Param id
     */
    @ActionKey("/api/hwc/reports/modify")
    public void modify(){
        String id = getPara(0);
        Reports report = Reports.dao.findById(id);
        //report.set("modifyAt", TimeUtil.getNowTime());
        //report.set("modifyBy", ((Map<String, Object>)getSessionAttr("user")).get("hwcUserId"));
        String status = getPara("status");
        Map<String, Object> loginUser = getSessionAttr("user");
        boolean flag = false;
        if(loginUser.get("type").toString().equals(HwcUserType.admin) && status != null){
            String areaCode = (String) loginUser.get("areaCode");
            if(areaCode.equals("top")){
                report.set("finalApproveComment", getPara("comment"));
                report.set("finalApproveBy", loginUser.get("accountId"));
                report.set("finalApproveAt", TimeUtil.getNowTime());
            }else {
                report.set("firstApproveComment", getPara("comment"));
                report.set("firstApproveBy", loginUser.get("accountId"));
                report.set("firstApproveAt", TimeUtil.getNowTime());
            }
            report.set("status", status);
            flag = true;
        }

        if(flag && report.update() == false){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("01", "表报修改失败，数据库异常，请联系管理员", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("01", "表报修改失败，数据库异常，请联系管理员", null));
            }
            return ;
        }
        Record r = Reports.dao.findById(id).toRecord();
        r.set("table", Tables.dao.findById(r.get("tableId")));
        r.set("sample", Samples.dao.findById(r.get("sampleId")));
        r.set("plan", ReportPlans.dao.findById(r.get("planId")));
        r.set("warehouse", Warehouses.dao.findById(r.get("warehouseId")));
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "表报修改成功", r));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "表报修改成功", r));
        }
    }

    /**
     * 根据id获取报表
     */
    @ActionKey("/api/hwc/reports/id")
    public void getReportById(){
        String id = getPara(0);
        Record r = Reports.dao.findById(id).toRecord();
        r.set("table", Tables.dao.findById(r.get("tableId")));
        r.set("sample", Samples.dao.findById(r.get("sampleId")));
        r.set("plan", ReportPlans.dao.findById(r.get("planId")));
        r.set("warehouse", Warehouses.dao.findById(r.get("warehouseId")));
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "根据id获取报表成功", r));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "根据id获取报表成功", r));
        }
    }
}
