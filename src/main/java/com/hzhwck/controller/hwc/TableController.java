package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.interceptor.LoginInterceptor;
import com.hzhwck.model.hwc.*;
import com.hzhwck.myEnum.HwcUserType;
import com.hzhwck.myEnum.TableNames;
import com.hzhwck.util.ResponseUtil;
import com.hzhwck.util.TimeUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
        String hp = TableNames.hwcReportPlans.split(" ")[1] + ".";
        filter = " where " + hr + "sampleId = " + hs + "id and " + hr + "planId = " + hp + "id ";
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
            filter = " where " + hr + "status = 5 ";
        else
            filter += " and " + hr + "status = 5 ";

        Date startAt = getParaToDate("startAt");

        Date endAt = getParaToDate("endAt");

        if(tableId.equals("1")){
            SimpleDateFormat sf = new SimpleDateFormat("yyyy");
            if(startAt != null){
                filter += " and DATE_FORMAT(" + hp + "round, '%Y') >= '" + sf.format(startAt) + "' ";
            }
            if(startAt != null){
                filter += " and DATE_FORMAT(" + hp + "round, '%Y') <= '" + sf.format(endAt) + "' ";
            }
            String ht1 = TableNames.hwcTable1.split(" ")[1] + ".";
            if(filter.equals(""))   filter = " where " + hr + "tableId = 1 and " + hr + "tableReportId = " + ht1 + "id ";
            else
                filter += " and " + hr + "tableId = 1 and " + hr + "tableReportId = " + ht1 + "id ";
            Page<Table1> tables = Table1.getPage(pageNumber, pageSize, orderBy, order, filter, TableNames.hwcReports + ", " + TableNames.hwcTable1 + ", " + TableNames.hwcSamples + ", " + TableNames.hwcReportPlans);
            for(Table1 table : tables.getList()){
                table.put("sample", Samples.dao.findById(table.get("sampleId")));
                table.put("plan", ReportPlans.getReportPlansAndTableGroupsById(table.get("planId").toString()));
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
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
            if(startAt != null){
                filter += " and DATE_FORMAT(" + hp + "round, '%Y-%m') >= '" + sf.format(startAt) + "' ";
            }
            if(startAt != null){
                filter += " and DATE_FORMAT(" + hp + "round, '%Y-%m') <= '" + sf.format(endAt) + "' ";
            }
            String ht2 = TableNames.hwcTable2.split(" ")[1] + ".";
            if(filter.equals(""))   filter = " where " + hr + "tableId = 2 and " + hr + "tableReportId = " + ht2 + "id ";
            else
                filter += " and " + hr + "tableId = 2 and " + hr + "tableReportId = " + ht2 + "id ";
            Page<Table2> tables = Table2.getPage(pageNumber, pageSize, orderBy, order, filter, TableNames.hwcReports + ", " + TableNames.hwcTable2 + ", " + TableNames.hwcSamples + ", " + TableNames.hwcReportPlans);
            for(Table2 table : tables.getList()){
                table.put("sample", Samples.dao.findById(table.get("sampleId")));
                table.put("plan", ReportPlans.getReportPlansAndTableGroupsById(table.get("planId").toString()));
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
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
            if(startAt != null){
                filter += " and DATE_FORMAT(" + hp + "round, '%Y-%m') >= '" + sf.format(startAt) + "' ";
            }
            if(startAt != null){
                filter += " and DATE_FORMAT(" + hp + "round, '%Y-%m') <= '" + sf.format(endAt) + "' ";
            }
            String ht3 = TableNames.hwcTable3.split(" ")[1] + ".";
            if(filter.equals(""))   filter = " where " + hr + "tableId = 3 and " + hr + "tableReportId = " + ht3 + "id ";
            else
                filter += " and " + hr + "tableId = 3 and " + hr + "tableReportId = " + ht3 + "id ";
            Page<Table3> tables = Table3.getPage(pageNumber, pageSize, orderBy, order, filter, TableNames.hwcReports + ", " + TableNames.hwcTable3 + ", " + TableNames.hwcSamples + ", " + TableNames.hwcReportPlans);
            for(Table3 table : tables.getList()){
                table.put("sample", Samples.dao.findById(table.get("sampleId")));
                table.put("plan", ReportPlans.getReportPlansAndTableGroupsById(table.get("planId").toString()));
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
    private String r = TableNames.hwcReports.split(" ")[1] + ".";

    private String s = TableNames.hwcSamples.split(" ")[1] + ".";

    private String p = TableNames.hwcReportPlans.split(" ")[1] + ".";

    private String w = TableNames.hwcWarehouses.split(" ")[1] + ".";

    /**
     * 报表数据统计
     */
    @Before(GET.class)
    @ActionKey("/api/hwc/tables/data")
    public void getData(){
        Table3 table3 = Table3.dao.findById("1");
        for(String temp : table3.toRecord().getColumnNames()){
            System.out.print("sum(" + TableNames.hwcTable3.split(" ")[1] + "." + temp + ") as " + temp + ", ");
        }
        System.out.println();

        Date date = getParaToDate("date");
        String tableId = getPara("tableId");

        if(tableId.equals("1")){
            String t = TableNames.hwcTable1.split(" ")[1] + ".";
            SimpleDateFormat sf = new SimpleDateFormat("yyyy");
            List<Record> records = Db.find("select " + s + "ssqx, sum(" + t + "xyhwcgs) as xyhwcgs, sum(" + t + "yyy) as yyy, sum(" + t + "zj) as zj " +
                    "from " + TableNames.hwcReports + ", " + TableNames.hwcTable1 + ", " + TableNames.hwcSamples + ", " + TableNames.hwcReportPlans + " " +
                    "where " + r + "sampleId = " + s + "id and " + r + "tableId = 1 and " + r + "status = 5 and " + r + "tableReportId = " + t + "id " + " " +
                    "and " + p + "id = " + r + "planId and DATE_FORMAT(" + p + "round, '%Y') = '" + sf.format(date) + "' " +
                    "group by " + s + "ssqx order by " + s + "ssqx");
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "表1数据统计", records));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("00", "表3数据统计", records));
            }
        }else if(tableId.equals("2")){

        }else if(tableId.equals("3")){
            String t = TableNames.hwcTable3.split(" ")[1] + ".";
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
            List<Record> records = Db.find("select " + s + "ssqx, sum(ht3.fwxyysr) as fwxyysr, sum(ht3.ckddlzyms) as ckddlzyms, sum(ht3.byljckz) as byljckz, sum(ht3.fwqysl) as fwqysl, sum(ht3.ckgztr) as ckgztr, sum(ht3.qmcyrs) as qmcyrs, sum(ht3.gwry) as gwry, sum(ht3.qmcyryqt) as qmcyryqt, sum(ht3.hzqy) as hzqy, sum(ht3.zjmj) as zjmj, sum(ht3.reportId) as reportId, sum(ht3.ckzltr) as ckzltr, sum(ht3.ygxc) as ygxc, sum(ht3.id) as id, sum(ht3.tc) as tc, sum(ht3.ytrsymj) as ytrsymj, sum(ht3.ljjstzeqt) as ljjstzeqt, sum(ht3.ckz) as ckz, sum(ht3.ckddlbhms) as ckddlbhms, sum(ht3.xssr) as xssr, sum(ht3.zyxs) as zyxs, sum(ht3.xymj) as xymj, sum(ht3.sbgztr) as sbgztr, sum(ht3.rkddlbhms) as rkddlbhms, sum(ht3.hyqys) as hyqys, sum(ht3.ljjstze) as ljjstze, sum(ht3.glry) as glry, sum(ht3.rkddlzyms) as rkddlzyms, sum(ht3.gnry) as gnry, sum(ht3.fwxyysrqt) as fwxyysrqt, sum(ht3.jsry) as jsry, sum(ht3.byljrkz) as byljrkz, sum(ht3.yyxsr) as yyxsr, sum(ht3.cc) as cc, sum(ht3.ps) as ps, sum(ht3.qmzkhz) as qmzkhz " +
                    "from " + TableNames.hwcReports + ", " + TableNames.hwcTable3 + ", " + TableNames.hwcSamples + ", " + TableNames.hwcReportPlans + " " +
                    "where " + r + "sampleId = " + s + "id and " + r + "tableId = 3 and " + r + "status = 5 and " + r + "tableReportId = " + t + "id " + " " +
                    "and " + p + "id = " + r + "planId and DATE_FORMAT(" + p + "round, '%Y-%m') = '" + sf.format(date) + "' " +
                    "group by " + s + "ssqx order by " + s + "ssqx");
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "表3数据统计", records));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("00", "表3数据统计", records));
            }
        }
    }

    /**
     * 统计分析
     */
    @ActionKey("/api/hwc/tables/analysis")
    public void analysis(){
        String type = getPara("type");
        Map<String , Object> data = new HashMap<String, Object>();
        if(type.equals("1")){
            String[] names = {"xymj", "ytrsymj", "zjmj"};
            data.put("month", getByMonth(TimeUtil.getNow(), TimeUtil.getNow(), names));
            data.put("priMonth", getByMonth(TimeUtil.getPriNow(), TimeUtil.getPriNow(), names));
        }else if(type.equals("2")){
            String[] names = {"ljjstze", "sbgztr", "ckzltr", "ckgztr", "ljjstzeqt"};
            data.put("month", getByMonth(TimeUtil.getNow(), TimeUtil.getNow(), names));
            data.put("priMonth", getByMonth(TimeUtil.getPriNow(), TimeUtil.getPriNow(), names));
        }else if(type.equals("3")){
            String[] names = {"ckz", "yyxsr", "fwxyysr", "xssr"};
            data.put("month", getByMonth(TimeUtil.getNow(), TimeUtil.getNow(), names));
            data.put("priMonth", getByMonth(TimeUtil.getPriNow(), TimeUtil.getPriNow(), names));
            data.put("year", getByMonth(TimeUtil.getYearFirstMonthTime(), TimeUtil.getNow(), names));
            data.put("priYear", getByMonth(TimeUtil.getPriYearFirstMonthTime(), TimeUtil.getPriNow(), names));
        }else if(type.equals("4")){
            String[] names = {"fwxyysr", "cc", "tc", "ps", "fwxyysrqt"};
            data.put("month", getByMonth(TimeUtil.getNow(), TimeUtil.getNow(), names));
            data.put("priMonth", getByMonth(TimeUtil.getPriNow(), TimeUtil.getPriNow(), names));
            data.put("year", getByMonth(TimeUtil.getYearFirstMonthTime(), TimeUtil.getNow(), names));
            data.put("priYear", getByMonth(TimeUtil.getPriYearFirstMonthTime(), TimeUtil.getPriNow(), names));
        }else if(type.equals("5")){
            String[] names = {"xssr", "zyxs"};
            data.put("month", getByMonth(TimeUtil.getNow(), TimeUtil.getNow(), names));
            data.put("priMonth", getByMonth(TimeUtil.getPriNow(), TimeUtil.getPriNow(), names));
            data.put("year", getByMonth(TimeUtil.getYearFirstMonthTime(), TimeUtil.getNow(), names));
            data.put("priYear", getByMonth(TimeUtil.getPriYearFirstMonthTime(), TimeUtil.getPriNow(), names));
        }else if(type.equals("6")){
            String[] names = {"rkddlbhms", "rkddlzyms", "ckddlbhms", "ckddlzyms"};
            data.put("month", getByMonth(TimeUtil.getNow(), TimeUtil.getNow(), names));
            data.put("priMonth", getByMonth(TimeUtil.getPriNow(), TimeUtil.getPriNow(), names));
            data.put("year", getByMonth(TimeUtil.getYearFirstMonthTime(), TimeUtil.getNow(), names));
            data.put("priYear", getByMonth(TimeUtil.getPriYearFirstMonthTime(), TimeUtil.getPriNow(), names));
        }else if(type.equals("7")){
            String[] names = {"byljrkz", "byljckz", "qmzkhz"};
            data.put("month", getByMonth(TimeUtil.getNow(), TimeUtil.getNow(), names));
            data.put("priMonth", getByMonth(TimeUtil.getPriNow(), TimeUtil.getPriNow(), names));
            String[] temp = {"byljrkz", "byljckz"};
            data.put("year", getByMonth(TimeUtil.getYearFirstMonthTime(), TimeUtil.getNow(), temp));
            data.put("priYear", getByMonth(TimeUtil.getPriYearFirstMonthTime(), TimeUtil.getPriNow(), temp));
        }else if(type.equals("8")){
            String[] names = {"fwqysl", "hzqy", "hyqys"};
            data.put("month", getByMonth(TimeUtil.getNow(), TimeUtil.getNow(), names));
            data.put("priMonth", getByMonth(TimeUtil.getPriNow(), TimeUtil.getPriNow(), names));
        }else if(type.equals("9")){
            String[] names = {"ygxc"};
            data.put("month", getByMonth(TimeUtil.getNow(), TimeUtil.getNow(), names));
            data.put("priMonth", getByMonth(TimeUtil.getPriNow(), TimeUtil.getPriNow(), names));
            data.put("year", getByMonth(TimeUtil.getYearFirstMonthTime(), TimeUtil.getNow(), names));
            data.put("priYear", getByMonth(TimeUtil.getPriYearFirstMonthTime(), TimeUtil.getPriNow(), names));
        }else if(type.equals("10")){
            String[] names = {"qmcyrs", "gnry", "gwry", "jsry", "glry", "qmcyryqt"};
            data.put("month", getByMonth(TimeUtil.getNow(), TimeUtil.getNow(), names));
            data.put("priMonth", getByMonth(TimeUtil.getPriNow(), TimeUtil.getPriNow(), names));
        }
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "表3数据统计分析", data));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "表3数据统计分析", data));
        }
    }



    /**
     * 分析数据表
     */
    @Clear(LoginInterceptor.class)
    @Before(GET.class)
    @ActionKey("/api/hwc/analysis")
    public void getAnalysisNames(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("1", "海外仓数量与面积分析");
        map.put("2", "海外仓建设投入情况分析");
        map.put("3", "海外仓出口额与营业收入分析 ");
        map.put("4", "海外仓服务收入结构分析");
        map.put("5", "海外仓销售收入结构分析");
        map.put("6", "海外仓订单处理量分析");
        map.put("7", "海外仓或物质分析");
        map.put("8", "海外仓服务对象企业分析");
        map.put("9", "海外仓薪酬分析");
        map.put("10", "海外仓人员构成分析");
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "统计分析", map));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "统计分析", map));
        }
    }

}
