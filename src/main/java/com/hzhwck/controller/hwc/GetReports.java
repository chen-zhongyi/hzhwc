package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.ReportPlans;
import com.hzhwck.model.hwc.Samples;
import com.hzhwck.model.hwc.Tables;
import com.hzhwck.model.hwc.Warehouses;
import com.hzhwck.myEnum.HwcUserType;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/25.
 */
public class GetReports extends BaseController{


    @Before(GET.class)
    @ActionKey("/api/hwc/getreports")
    public void getReports() {
        String tableId = getPara("tableId");
        String sampleName = getPara("jsdwmc");
        String sampleId = getPara("sampleId");
        Map<String, Object> loginUser = getSessionAttr("user");
        if(loginUser.get("type").toString().equals(HwcUserType.sample)){
            Samples sample = (Samples) loginUser.get("sample");
            sampleId = sample == null ? null : sample.get("id").toString();
        }
        String wSampleId = "";
        if(sampleId != null)    wSampleId = " and w.sampleId = " + sampleId + " ";
        if(sampleId == null)    sampleId = "";
        else sampleId = " and s.id = " + sampleId + " ";
        String areaCode = getPara("areaCode");
        if(loginUser.get("type").toString().equals(HwcUserType.qxAdmin)){
            areaCode = (String) loginUser.get("areaCode");
        }
        String yqCode = getPara("yqCode");
        if(loginUser.get("type").toString().equals(HwcUserType.yqadmin)){
            yqCode = (String) loginUser.get("yqCode");
        }
        String status = getPara("status");
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);

        if(status != null){
            if(status.equals("0")){
                status = " where (ta.status = 0 or ta.status is null) ";
            }else {
                status = " where ta.status = " + status + " ";
            }
        }else {
            status = "";
        }
        if(tableId == null)  tableId = "";
        else {
            tableId = " and t.id = " + tableId + " ";
        }
        String sampleFilter = "";
        if(sampleName != null){
            sampleFilter = " and s.jsdwmc like '%" + sampleName + "%' ";
        }
        if(areaCode != null){
            sampleFilter += " and s.ssqx = '" + areaCode + "' ";
        }
        if(yqCode != null){
            sampleFilter += " and s.ssyq = '" + yqCode + "' ";
        }

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        Date startAt = getParaToDate("startAt");
        if(startAt != null){
            sampleFilter += " and DATE_FORMAT(p.round, '%Y-%m') >= '" + sf.format(startAt) + "' ";
        }

        Date endAt = getParaToDate("endAt");
        if(endAt != null){
            sampleFilter += " and DATE_FORMAT(p.round, '%Y-%m') <= '" + sf.format(endAt) + "' ";
        }

        Page<Record> records = Db.paginate(pageNumber, pageSize, "select ta.* ", "from (select al.*, null as warehouseId, r.status, r.id from " +
                "(select t.id as tableId, p.id as planId, s.id as sampleId, p.startAt, p.endAt, p.round from hwc_tables t, hwc_reportplans p, hwc_samples s where p.tableGroupId = t.groupId and t.isRelatedWithWarehouse = 0 and p.status = 1 " + sampleId + tableId + " " + sampleFilter + " ) as al " +
                "left outer join " +
                "hwc_reports as r " +
                "on r.tableId = al.tableId and r.planId = al.planId and r.sampleId = al.sampleId"
                + " union all " +
                "select al.*, r.status, r.id from " +
                "(select t.id as tableId, p.id as planId, w.sampleId, p.startAt, p.endAt, p.round, w.id as warehouseId from hwc_tables t, hwc_reportplans p, hwc_warehouses w, hwc_samples s where s.id = w.sampleId and p.tableGroupId = t.groupId and t.isRelatedWithWarehouse = 1 and w.status = 1 and p.status = 1 " + wSampleId + tableId + " " + sampleFilter + " ) as al " +
                "left outer join " +
                "hwc_reports as r " +
                "on r.tableId = al.tableId and r.planId = al.planId and r.sampleId = al.sampleId and r.warehouseId = al.warehouseId) ta " + status );
        for(Record record : records.getList()){
            record.set("table", Tables.dao.findById(record.get("tableId").toString()));
            record.set("sample", Samples.dao.findById(record.get("sampleId").toString()));
            record.set("plan", ReportPlans.getReportPlansAndTableGroupsById(record.get("planId").toString()));
            if(record.get("warehouseId") == null)   continue;
            record.set("warehouse", Warehouses.dao.findById(record.get("warehouseId").toString()));
        }
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "获取报表成功", records));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "获取报表成功", records));
        }
    }
}
