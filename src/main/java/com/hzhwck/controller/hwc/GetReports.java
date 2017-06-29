package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
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
            System.out.println("nishi");
            Samples sample = (Samples) loginUser.get("sample");
            sampleId = sample == null ? null : sample.get("id").toString();
        }
        String wSampleId = "";
        if(sampleId != null)    wSampleId = " and w.sampleId = " + sampleId + " ";
        if(sampleId == null)    sampleId = "";
        else sampleId = " and s.id = " + sampleId + " ";
        String areaCode = getPara("ssqy");
        String status = getPara("status");
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);
        if(status != null){
            if(status.equals("-1")){
                status = "null";
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
            sampleFilter += " and s.ssqy = " + areaCode + " ";
        }
        Page<Record> records = Db.paginate(pageNumber, pageSize, "select ta.* ", "from (select al.*, null as warehouseId, r.status, r.id from " +
                "(select t.id as tableId, p.id as planId, s.id as sampleId, p.startAt, p.endAt, p.round from hwc_tables t, hwc_reportplans p, hwc_samples s where p.tableGroupId = t.groupId and t.isRelatedWithWarehouse = 0 " + sampleId + tableId + " " + sampleFilter + ") as al " +
                "left outer join " +
                "hwc_reports as r " +
                "on r.tableId = al.tableId and r.planId = al.planId and r.sampleId = al.sampleId"
                + " union all " +
                "select al.*, r.status, r.id from " +
                "(select t.id as tableId, p.id as planId, w.sampleId, p.startAt, p.endAt, p.round, w.id as warehouseId from hwc_tables t, hwc_reportplans p, hwc_warehouses w where p.tableGroupId = t.groupId and t.isRelatedWithWarehouse = 1 " + wSampleId + tableId + " " + sampleFilter + ") as al " +
                "left outer join " +
                "hwc_reports as r " +
                "on r.tableId = al.tableId and r.planId = al.planId and r.sampleId = al.sampleId and r.warehouseId = al.warehouseId) ta");
        for(Record record : records.getList()){
            record.set("table", Tables.dao.findById(record.get("tableId").toString()));
            record.set("sample", Samples.dao.findById(record.get("sampleId").toString()));
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
