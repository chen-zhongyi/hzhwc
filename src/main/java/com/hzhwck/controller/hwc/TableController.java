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

import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/26.
 */
public class TableController extends BaseController{

    @ActionKey("/api/hwc/tables")
    public void test(){
        boolean success = false;
        if(getRequest().getMethod().equals("POST")){
            System.out.println("[POST] add");
            forwardAction("/api/hwc/tables/add");
            success = true;
        }else if(getRequest().getMethod().equals("GET")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[GET] show id -- " + id);
                forwardAction("/api/hwc/tables/id/" + id);
                success = true;
            }else {
                System.out.println("[GET] show list");
                forwardAction("/api/hwc/tables/search");
                success = true;
            }
        }else if(getRequest().getMethod().equals("PUT")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[PUT] update id -- " + id);
                forwardAction("/api/hwc/tables/modify/" + id);
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

    /***
     * 报表数据填写
     * @Param reportId 报表id
     * @Param tableNo 报表类型
     */
    @ActionKey("/api/hwc/tables/add")
    public void add(){
        String reportId = getPara("reportId");
        String tableId = getPara("tableId");
        Reports report = Reports.dao.findById(reportId);
        if(tableId.equals("1")){
            Table1 table1 = getModel(Table1.class, "table1");
            table1.set("reportId", reportId);
            if(table1.save() == false){
                if(getPara("callback") != null){
                    String json = JsonKit.toJson(ResponseUtil.setRes("01", "报表数据填写失败，数据库异常", null));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                }else {
                    renderJson(ResponseUtil.setRes("01", "报表数据填写失败，数据库异常", null));
                }
            }else {
                if(getPara("callback") != null){
                    String json = JsonKit.toJson(ResponseUtil.setRes("00", "报表数据填写成功", table1));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                }else {
                    renderJson(ResponseUtil.setRes("00", "报表数据填写成功", table1));
                }
            }
            report.set("tableReportId", table1.get("id"));

        }else if(tableId.equals("2")){
            Table2 table2 = getModel(Table2.class, "table2");
            table2.set("reportId", reportId);
            if(table2.save() == false){
                if(getPara("callback") != null){
                    String json = JsonKit.toJson(ResponseUtil.setRes("01", "报表数据填写失败，数据库异常", null));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                }else {
                    renderJson(ResponseUtil.setRes("01", "报表数据填写失败，数据库异常", null));
                }
            }else {
                if(getPara("callback") != null){
                    String json = JsonKit.toJson(ResponseUtil.setRes("00", "报表数据填写成功", table2));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                }else {
                    renderJson(ResponseUtil.setRes("00", "报表数据填写成功", table2));
                }
            }
            report.set("tableReportId", table2.get("id"));
        }else if(tableId.equals("3")){
            Table3 table3 = getModel(Table3.class, "table3");
            table3.set("reportId", reportId);
            if(table3.save() == false){
                if(getPara("callback") != null){
                    String json = JsonKit.toJson(ResponseUtil.setRes("01", "报表数据填写失败，数据库异常", null));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                }else {
                    renderJson(ResponseUtil.setRes("01", "报表数据填写失败，数据库异常", null));
                }
            }else {
                if(getPara("callback") != null){
                    String json = JsonKit.toJson(ResponseUtil.setRes("00", "报表数据填写成功", table3));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                }else {
                    renderJson(ResponseUtil.setRes("00", "报表数据填写成功", table3));
                }
            }
            report.set("tableReportId", table3.get("id"));
        }
        report.set("status", 1);
        report.set("modifyAt", TimeUtil.getNowTime());
        report.set("modifyBy", ((Map<String, Object>)getSessionAttr("user")).get("hwcUserId"));
        report.update();
    }

    /***
     * 报表数据修改
     * @Param tableNo 报表类型
     */
    @ActionKey("/api/hwc/tables/modify")
    public void update(){
        String id = getPara(0);
        String tableId = getPara("tableId");
        if(tableId.equals("1")){
            Table1 table1 = getModel(Table1.class, "table1");
            table1.set("id", id);
            if(table1.update() == false){
                if(getPara("callback") != null){
                    String json = JsonKit.toJson(ResponseUtil.setRes("01", "报表数据修改失败，数据库异常", null));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                }else {
                    renderJson(ResponseUtil.setRes("01", "报表数据修改失败，数据库异常", null));
                }
            }else {
                if(getPara("callback") != null){
                    String json = JsonKit.toJson(ResponseUtil.setRes("00", "报表数据修改成功", table1));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                }else {
                    renderJson(ResponseUtil.setRes("00", "报表数据修改成功", table1));
                }
            }
            Reports report = Reports.dao.findById(table1.get("reportId"));
            report.set("modifyAt", TimeUtil.getNowTime());
            report.set("modifyBy", ((Map<String, Object>)getSessionAttr("user")).get("hwcUserId"));
            report.update();
        }else if(tableId.equals("2")){
            Table2 table2 = getModel(Table2.class, "table2");
            table2.set("id", id);
            if(table2.update() == false){
                if(getPara("callback") != null){
                    String json = JsonKit.toJson(ResponseUtil.setRes("01", "报表数据修改失败，数据库异常", null));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                }else {
                    renderJson(ResponseUtil.setRes("01", "报表数据修改失败，数据库异常", null));
                }
            }else {
                if(getPara("callback") != null){
                    String json = JsonKit.toJson(ResponseUtil.setRes("00", "报表数据修改成功", table2));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                }else {
                    renderJson(ResponseUtil.setRes("00", "报表数据修改成功", table2));
                }
            }
            Reports report = Reports.dao.findById(table2.get("reportId"));
            report.set("modifyAt", TimeUtil.getNowTime());
            report.set("modifyBy", ((Map<String, Object>)getSessionAttr("user")).get("hwcUserId"));
            report.update();
        }else if(tableId.equals("3")){
            Table3 table3 = getModel(Table3.class, "table3");
            table3.set("id", id);
            if(table3.update() == false){
                if(getPara("callback") != null){
                    String json = JsonKit.toJson(ResponseUtil.setRes("01", "报表数据修改失败，数据库异常", null));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                }else {
                    renderJson(ResponseUtil.setRes("01", "报表数据修改失败，数据库异常", null));
                }
            }else {
                if(getPara("callback") != null){
                    String json = JsonKit.toJson(ResponseUtil.setRes("00", "报表数据修改成功", table3));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                }else {
                    renderJson(ResponseUtil.setRes("00", "报表数据修改成功", table3));
                }
            }
            Reports report = Reports.dao.findById(table3.get("reportId"));
            report.set("modifyAt", TimeUtil.getNowTime());
            report.set("modifyBy", ((Map<String, Object>)getSessionAttr("user")).get("hwcUserId"));
            report.update();
        }
    }

    /**
     * 根据报表id获取报表信息
     */
    @ActionKey("/api/hwc/tables/id")
    public void getTableById(){
        String id = getPara(0);
        String tableId = getPara("tableId");
        if(tableId.equals("1")){
            Table1 table1 = Table1.dao.findById(id);
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "根据报表id获取报表信息成功", table1));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("00", "根据报表id获取报表信息成功", table1));
            }
        }else if(tableId.equals("2")){
            Table2 table2 = Table2.dao.findById(id);
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "根据报表id获取报表信息成功", table2));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("00", "根据报表id获取报表信息成功", table2));
            }
        }else if(tableId.equals("3")){
            Table3 table3 = Table3.dao.findById(id);
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "根据报表id获取报表信息成功", table3));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("00", "根据报表id获取报表信息成功", table3));
            }
        }
    }

    /**
     * 报表数据查询
     */
    @ActionKey("/api/hwc/tables/search")
    public void getPage(){

        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);
        String order = getPara("order", "asc");
        String orderBy = getPara("orderBy");

        String filter = "";
        String hr = TableNames.hwcReports.split(" ")[1] + ".";
        String hs = TableNames.hwcSamples.split(" ")[1] + ".";
        filter = " where " + hr + "sampleId = " + hs + "id ";
        String tableId = getPara("tableId");
        Map<String, Object> loginUser = getSessionAttr("user");
        if(loginUser.get("type").toString().equals(HwcUserType.sample)){
            Samples sample = (Samples) loginUser.get("sample");
            String sampleId = sample == null ? "-1" : sample.get("id").toString();
            if(filter.equals(""))   filter = " where " + hr + "sampleId = " + sampleId + " ";
            else
                filter += " and " + hr + "sampleId = " + sampleId + " " ;
        }else {
            String ssqx = getPara("ssqx");
            if(ssqx != null){
                if(filter.equals(""))   filter = " where " + hs + "ssqx = '" + ssqx + "' ";
                else
                    filter += " and " + hs + "ssqx = '" + ssqx + "' ";
            }
            String jsdwmc = getPara("jsdwmc");
            if(jsdwmc != null){
                if(filter.equals(""))   filter = " where " + hs + "jsdwmc like '%" + jsdwmc + "%' ";
                else
                    filter += " and " + hs + "jsdwmc like '%" + jsdwmc + "%' ";
            }
        }
        //status
        if(filter.equals(""))
            filter = " where " + hr + "status = 4 ";
        else
            filter += " and " + hr + "status = 4 ";
        if(tableId.equals("1")){
            String ht1 = TableNames.hwcTable1.split(" ")[1] + ".";
            if(filter.equals(""))   filter = " where " + hr + "tableId = 1 and " + hr + "tableReportId = " + ht1 + "id ";
            else
                filter += " and " + hr + "tableId = 1 and " + hr + "tableReportId = " + ht1 + "id ";
            Page<Table1> tables = Table1.getPage(pageNumber, pageSize, orderBy, order, filter, TableNames.hwcReports + ", " + TableNames.hwcTable1 + ", " + TableNames.hwcSamples);
            for(Table1 table : tables.getList()){
                table.put("sample", Samples.dao.findById(table.get("sampleId")));
                if(table.get("warehouseId") != null){
                    table.put("warehouse", Warehouses.dao.findById(table.get("warehouseId")));
                }
            }
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "报表数据查询", tables));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("00", "报表数据查询", tables));
            }
        }else if(tableId.equals("2")){
            String ht2 = TableNames.hwcTable2.split(" ")[1] + ".";
            if(filter.equals(""))   filter = " where " + hr + "tableId = 2 and " + hr + "tableReportId = " + ht2 + "id ";
            else
                filter += " and " + hr + "tableId = 2 and " + hr + "tableReportId = " + ht2 + "id ";
            Page<Table2> tables = Table2.getPage(pageNumber, pageSize, orderBy, order, filter, TableNames.hwcReports + ", " + TableNames.hwcTable2 + ", " + TableNames.hwcSamples);
            for(Table2 table : tables.getList()){
                table.put("sample", Samples.dao.findById(table.get("sampleId")));
                if(table.get("warehouseId") != null){
                    table.put("warehouse", Warehouses.dao.findById(table.get("warehouseId")));
                }
            }
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "报表数据查询", tables));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("00", "报表数据查询", tables));
            }
        }else if(tableId.equals("3")){
            String ht3 = TableNames.hwcTable3.split(" ")[1] + ".";
            if(filter.equals(""))   filter = " where " + hr + "tableId = 3 and " + hr + "tableReportId = " + ht3 + "id ";
            else
                filter += " and " + hr + "tableId = 3 and " + hr + "tableReportId = " + ht3 + "id ";
            Page<Table3> tables = Table3.getPage(pageNumber, pageSize, orderBy, order, filter, TableNames.hwcReports + ", " + TableNames.hwcTable3 + ", " + TableNames.hwcSamples);
            for(Table3 table : tables.getList()){
                table.put("sample", Samples.dao.findById(table.get("sampleId")));
                if(table.get("warehouseId") != null){
                    table.put("warehouse", Warehouses.dao.findById(table.get("warehouseId")));
                }
            }
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "报表数据查询", tables));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("00", "报表数据查询", tables));
            }
        }
    }
}
