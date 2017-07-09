package com.hzhwck.controller.hwc;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.*;
import com.hzhwck.myEnum.CodeType;
import com.hzhwck.myEnum.TableNames;
import com.hzhwck.util.PDF;
import com.hzhwck.util.ResponseUtil;
import com.hzhwck.util.TimeUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/7/9.
 */
public class PDFController extends BaseController{

    /**
     * 导出评审材料
     */
    @Before(GET.class)
    @ActionKey("/api/hwc/pdf")
    public void getPDF(){

        String warehouseId = getPara("warehouseId");
        //PDF.getPdf(data, "chen.pdf");
        Map<String, Object> data = getData(warehouseId);
        if(data != null)
        {
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "获取导出评审材料数据成功", data));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes(CodeType.success, "获取导出评审材料数据成功", data));
            }
        }
    }

    /**
     * 导出pdf
     */
    @Before(POST.class)
    @ActionKey("/api/hwc/pdf/out")
    public void get(){
        String warehouseId = getPara("warehouseId");
        String data = getPara("data");
        Warehouses w = Warehouses.dao.findById(warehouseId);
        Map<String, Object> map;
        try {
             map = new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {}.getType());
        }catch (Exception e){
            e.printStackTrace();
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.isNotAllowGetPdf, "数据数据转换失败，请联系管理员", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes(CodeType.isNotAllowGetPdf, "数据数据转换失败，请联系管理员", null));
            }
            return ;
        }
        String name =  w.getStr("hwcmc") + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date(System.currentTimeMillis()))+ ".pdf";
        Pdf p = Pdf.getPdfByWarehouseId(warehouseId);
        String path = PathKit.getWebRootPath() + File.separator + "pdfs" + File.separator + name;
        PDF.getPdf(getData(warehouseId), map, path);
        if(p == null){
            Pdf pdf = new Pdf()
                    .set("warehouseId", warehouseId)
                    .set("content", data)
                    .set("path", File.separator + "pdfs" + File.separator + name);
            pdf.save();
        }else {
            p.set("content", data);
            p.set("path", File.separator + "pdfs" + File.separator + name);
            p.update();
        }
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "成功", Pdf.getPdfByWarehouseId(warehouseId)));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes(CodeType.success, "成功", Pdf.getPdfByWarehouseId(warehouseId)));
        }
    }

    /**
     * 下载pdf
     */
    @Before(GET.class)
    @ActionKey("/api/hwc/pdf/download")
    public void download(){
        String path = getPara("path");
        renderFile(new File(PathKit.getWebRootPath() + path));
    }

    private Map<String, Object> getData(String warehouseId){
        Date date = TimeUtil.getNow();
        Map<String, Object> data = new HashMap<String, Object>();
        Warehouses warehouse = Warehouses.dao.findById(warehouseId);

        Samples sample = Samples.dao.findById(warehouse.get("sampleId"));
        sample.put("area", Area.findByAreaCode(sample.getStr("ssqx")));
        sample.put("yuanqu", Yuanqu.getYuanquByCode(sample.getStr("ssyq")));
        data.put("sample", sample);

        warehouse.put("country", Country.getByCode(warehouse.getStr("hwcssgj")));
        data.put("warehouse", warehouse);
        Pdf record = Pdf.getPdfByWarehouseId(warehouseId);
        data.put("path", record.get("path"));
        if(record == null)
            data.put("data", null);
        else{
            try {
                Map<String, Object> temp = new Gson().fromJson(record.getStr("content"), new TypeToken<Map<String, Object>>() {}.getType());
                data.put("data", temp);
            }catch (Exception e){
                e.printStackTrace();
                if(getPara("callback") != null){
                    String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.isNotAllowGetPdf, "数据数据转换失败，请联系管理员", null));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                }else {
                    renderJson(ResponseUtil.setRes(CodeType.isNotAllowGetPdf, "数据数据转换失败，请联系管理员", null));
                }
                return null;
            }
        }

        String s = TableNames.hwcSamples.split(" ")[1] + ".";
        String w = TableNames.hwcWarehouses.split(" ")[1] + ".";
        String p = TableNames.hwcReportPlans.split(" ")[1] + ".";
        String r = TableNames.hwcReports.split(" ")[1] + ".";
        String t1 = TableNames.hwcTable1.split(" ")[1] + ".";
        String t2 = TableNames.hwcTable2.split(" ")[1] + ".";
        String t3 = TableNames.hwcTable3.split(" ")[1] + ".";
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        List<Record> records = Db.find("select " + t1 + "* from " + "" +
                TableNames.hwcReports + ", " + TableNames.hwcReportPlans + ", " + TableNames.hwcSamples + ", " + TableNames.hwcTable1 + " " +
                "where " + r + "status = 5 and " + r + "tableId = 1 and " + s + "id = " + warehouse.get("sampleId") + " and " + r + "sampleId = " + s + "id and " + r + "tableReportId = " + t1 + "id " +
                "and " + r + "planId = " + p + "id and DATE_FORMAT(" + p + "round, '%Y') = '" + sf.format(date) + "'" );
        if(records == null || records.size() == 0){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.isNotAllowGetPdf, "数据不完整，无法执行此操作", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes(CodeType.isNotAllowGetPdf, "数据不完整，无法执行此操作", null));
            }
            return null;
        }
        data.put("table1", records.get(0));
        records = Db.find("select " + t2 + "* from " + "" +
                TableNames.hwcReports + ", " + TableNames.hwcReportPlans + ", " + TableNames.hwcWarehouses + ", " + TableNames.hwcTable2 + " " +
                "where " + r + "status = 5 and " + r + "tableId = 2 and " + w + "id = " + warehouseId + " and " + r + "warehouseId = " + w + "id and " + r + "tableReportId = " + t2 + "id " +
                "and " + r + "planId = " + p + "id order by " + p + "round desc");
        if(records == null || records.size() == 0){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.isNotAllowGetPdf, "数据不完整，无法执行此操作", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes(CodeType.isNotAllowGetPdf, "数据不完整，无法执行此操作", null));
            }
            return null;
        }
        data.put("table2", records.get(0));

        sf = new SimpleDateFormat("yyyy-MM");
        records = Db.find("select " + "sum(" + t3 + "xymj) as xymj, sum(" + t3 + "ytrsymj) as ytrsymj, sum(" + t3 + "zjmj) as zjmj, sum(" + t3 + "ljjstze) as ljjstze, sum(" + t3 + "sbgztr) as sbgztr, sum(" + t3 + "ckzltr) as ckzltr, sum(" + t3 + "ckgztr) as ckgztr, sum(" + t3 + "ljjstzeqt) as ljjstzeqt, sum(" + t3 + "ckz) as ckz, sum(" + t3 + "yyxsr) as yyxsr, sum(" + t3 + "fwxyysr) as fwxyysr, sum(" + t3 + "tc) as tc, sum(" + t3 + "cc) as cc, sum(" + t3 + "ps) as ps, sum(" + t3 + "fwxyysrqt) as fwxyysrqt, sum(" + t3 + "xssr) as xssr, sum(" + t3 + "zyxs) as zyxs, sum(" + t3 + "rkddlbhms) as rkddlbhms, sum(" + t3 + "rkddlzyms) as rkddlzyms, sum(" + t3 + "ckddlbhms) as ckddlbhms, sum(" + t3 + "ckddlzyms) as ckddlzyms, sum(" + t3 + "byljrkz) as byljrkz, sum(" + t3 + "byljckz) as byljckz, sum(" + t3 + "qmzkhz) as qmzkhz, sum(" + t3 + "fwqysl) as fwqysl, sum(" + t3 + "hzqy) as hzqy, sum(" + t3 + "hyqys) as hyqys, sum(" + t3 + "qmcyrs) as qmcyrs, sum(" + t3 + "gnry) as gnry, sum(" + t3 + "gwry) as gwry, sum(" + t3 + "jsry) as jsry, sum(" + t3 + "glry) as glry, sum(" + t3 + "qmcyryqt) as qmcyryqt, sum(" + t3 + "ygxc) as ygxc from " + "" +
                TableNames.hwcReports + ", " + TableNames.hwcReportPlans + ", " + TableNames.hwcWarehouses + ", " + TableNames.hwcTable3 + " " +
                "where " + r + "status = 5 and " + r + "tableId = 3 and " + w + "id = " + warehouseId + " and " + r + "warehouseId = " + w + "id and " + r + "tableReportId = " + t3 + "id " +
                "and " + r + "planId = " + p + "id and DATE_FORMAT(" + p + "round, '%Y-%m') = '" + sf.format(date) + "'");
        if(records == null || records.size() == 0){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.isNotAllowGetPdf, "数据不完整，无法执行此操作", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes(CodeType.isNotAllowGetPdf, "数据不完整，无法执行此操作", null));
            }
            return null;
        }
        data.put("month", records.get(0));
        records = Db.find("select " + "sum(" + t3 + "xymj) as xymj, sum(" + t3 + "ytrsymj) as ytrsymj, sum(" + t3 + "zjmj) as zjmj, sum(" + t3 + "ljjstze) as ljjstze, sum(" + t3 + "sbgztr) as sbgztr, sum(" + t3 + "ckzltr) as ckzltr, sum(" + t3 + "ckgztr) as ckgztr, sum(" + t3 + "ljjstzeqt) as ljjstzeqt, sum(" + t3 + "ckz) as ckz, sum(" + t3 + "yyxsr) as yyxsr, sum(" + t3 + "fwxyysr) as fwxyysr, sum(" + t3 + "tc) as tc, sum(" + t3 + "cc) as cc, sum(" + t3 + "ps) as ps, sum(" + t3 + "fwxyysrqt) as fwxyysrqt, sum(" + t3 + "xssr) as xssr, sum(" + t3 + "zyxs) as zyxs, sum(" + t3 + "rkddlbhms) as rkddlbhms, sum(" + t3 + "rkddlzyms) as rkddlzyms, sum(" + t3 + "ckddlbhms) as ckddlbhms, sum(" + t3 + "ckddlzyms) as ckddlzyms, sum(" + t3 + "byljrkz) as byljrkz, sum(" + t3 + "byljckz) as byljckz, sum(" + t3 + "qmzkhz) as qmzkhz, sum(" + t3 + "fwqysl) as fwqysl, sum(" + t3 + "hzqy) as hzqy, sum(" + t3 + "hyqys) as hyqys, sum(" + t3 + "qmcyrs) as qmcyrs, sum(" + t3 + "gnry) as gnry, sum(" + t3 + "gwry) as gwry, sum(" + t3 + "jsry) as jsry, sum(" + t3 + "glry) as glry, sum(" + t3 + "qmcyryqt) as qmcyryqt, sum(" + t3 + "ygxc) as ygxc from " + "" +
                TableNames.hwcReports + ", " + TableNames.hwcReportPlans + ", " + TableNames.hwcWarehouses + ", " + TableNames.hwcTable3 + " " +
                "where " + r + "status = 5 and " + r + "tableId = 3 and " + w + "id = " + warehouseId + " and " + r + "warehouseId = " + w + "id and " + r + "tableReportId = " + t3 + "id " +
                "and " + r + "planId = " + p + "id and DATE_FORMAT(" + p + "round, '%Y-%m') >= '" + sf.format(TimeUtil.getYearFirstMonthTime()) + "' " +
                "and DATE_FORMAT(" + p + "round, '%Y-%m') <= '" + sf.format(date) + "'");
        if(records == null || records.size() == 0){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.isNotAllowGetPdf, "数据不完整，无法执行此操作", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes(CodeType.isNotAllowGetPdf, "数据不完整，无法执行此操作", null));
            }
            return null;
        }
        data.put("year", records.get(0));
        data.put("serviceCompanys", ServiceCompanys.getServiceCompanysByWarehouseId(warehouseId));
        return data;
    }


    public static void main(String[] args){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("E:\\慧数海外仓\\表3.txt")));
            String line ;
            String res = "";
            while((line = br.readLine()) != null){
                int j = 0;
                for(int i = 0;i < line.length();++i)if(line.charAt(i) >= 'a' && line.charAt(i) <= 'z'){
                    j = i;
                }else
                    break;
                res += "sum(\" + t3 + \"" + line.substring(0, j + 1) + ") as " + line.substring(0, j + 1) + ", ";
            }
            System.out.println(res);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
