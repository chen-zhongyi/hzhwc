package com.hzhwck.controller.hwc;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.*;
import com.hzhwck.myEnum.CodeType;
import com.hzhwck.myEnum.HwcUserType;
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
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 陈忠意 on 2017/7/9.
 */
public class PDFController extends BaseController{

    private static final Logger log = Logger.getLogger(PDFController.class);

    /**
     * 查看pdf
     */
    @Before(GET.class)
    @ActionKey("/api/hwc/getpdf")
    public void search(){
        Map<String, Object> loginUser = getSessionAttr("user");
        String s = TableNames.hwcSamples.split(" ")[1] + ".";
        String u = TableNames.hwcUsers.split(" ")[1] + ".";
        String w = TableNames.hwcWarehouses.split(" ")[1] + ".";
        String p = TableNames.hwcPdf.split(" ")[1] + ".";
        String filter = " where " + s + "userId = " + u + "id and " + s + "id = " + w + "sampleId and " + w + "id = " + p + "warehouseId "
                + " and " + u + "type = " + HwcUserType.sample + " and " + p + "status != 0 ";
        String type = loginUser.get("type").toString();
        if(type.equals(HwcUserType.sample)){
            Samples sample = (Samples) loginUser.get("sample");
            if(sample == null)
                filter += " and " + s + "id = -1 ";
            else
                filter += " and " + s + "id = " + sample.get("id") + " ";
        }else if(type.equals(HwcUserType.admin)){
            String areaCode = getPara("areaCode");
            String yqCode = getPara("yqCode");
            if(areaCode != null)
                filter += " and " + s + "ssqx = '" + areaCode + "' ";
            if(yqCode != null)
                filter += " and " + s + "ssyq = '" + yqCode + "' ";
        }else if(type.equals(HwcUserType.qxAdmin)){
            filter += " and " + s + "ssqx = '" + loginUser.get("areaCode") + "' " ;
        }else if(type.equals(HwcUserType.yqadmin)){
            filter += " and " + s + "ssyq = '" + loginUser.get("yqCode") + "' ";
        }

        String jsdwmc = getPara("jsdwmc");
        String shtyxydm = getPara("shtyxydm");
        String hwcmc = getPara("hwcmc");
        String status = getPara("status");
        if(jsdwmc != null){
            filter += " and " + s + "jsdwmc like '%" + jsdwmc + "%' ";
        }
        if(shtyxydm != null){
            filter += " and " + s + "shtyxydm like '%" + shtyxydm + "%' ";
        }
        if(hwcmc != null){
            filter += " and " + w + "hwcmc like '%" + hwcmc + "%' ";
        }
        if(status != null){
            filter += " and " + p + "status = " + status + " ";
        }

        Page<Pdf> pdfs = Pdf.getPage(getParaToInt("pageNumber", 1), getParaToInt("pageSize", 20),
                p + "id", "desc", filter,
                TableNames.hwcSamples + ", " + TableNames.hwcPdf + ", " +TableNames.hwcWarehouses + ", " + TableNames.hwcUsers);
        for(Pdf pdf : pdfs.getList()){
            pdf.remove("approveBy");
            pdf.remove("approveAt");
            pdf.remove("finalApproveBy");
            pdf.remove("finalApproveAt");
            pdf.put("sample", Samples.dao.findById(pdf.get("sampleId")));
            pdf.put("warehouse", Warehouses.dao.findById(pdf.get("warehouseId")));
        }
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "获取pdf成功", pdfs));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "获取pdf成功", pdfs));
        }
    }

    /**
     * 对评审材料进行评审
     */
    @ActionKey("/api/hwc/approvepdf")
    public void approve(){
        HttpServletRequest request = this.getRequest();
        if(!request.getMethod().equals("PATCH")){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("02", "url出错或者请求方法出错", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("02", "url出错或者请求方法出错", null));
            }
            return ;
        }
        BufferedReader br = null;
        StringBuffer str = new StringBuffer("");
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String temp = null;
            while((temp = br.readLine()) != null){
                str.append(temp);
                //log.info(temp);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if(br != null) {
                try {
                    br.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        System.out.println("请求body = " + str);
        String contentType = request.getHeader("Content-Type");
        String approveComment = null;
        String status = null;
        String finalApproveComment = null;
        String id = getPara(0);
        if(contentType.equals("application/json")){
            Map<String, String> map = new Gson().fromJson(str.toString(), new TypeToken<Map<String, String>>() {}.getType());
            approveComment = map.get("approveComment");
            status = map.get("status");
            finalApproveComment = map.get("finalApproveComment");
        }else {
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("02", "要求application/json", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("02", "要求application/json", null));
            }
            return ;
        }
        Pdf pdf = Pdf.dao.findById(id);
        if(status != null)
            pdf.set("status", status);
        if(approveComment != null)
            pdf.set("approveComment", approveComment);
        if(approveComment != null)
            pdf.set("finalApproveComment", finalApproveComment);
        Map<String, String> loginUer = getSessionAttr("user");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if(status != null && (status.equals("2") || status.equals("3"))) {
            pdf.set("approveBy", loginUer.get("id"));
            pdf.set("approveAt", new Date(System.currentTimeMillis()));
            log.info("approveBy = " + String.valueOf(loginUer.get("id")) + ", approveAt = " + sf.format(new Date(System.currentTimeMillis())));
        }
        if(status != null && (status.equals("4") || status.equals("5"))) {
            pdf.set("finalApproveBy", loginUer.get("id"));
            pdf.set("finalApproveAt", new Date(System.currentTimeMillis()));
            log.info("finalApproveBy = " + String.valueOf(loginUer.get("id")) + ", finalApproveAt = " + sf.format(new Date(System.currentTimeMillis())));
        }
        pdf.update();
        pdf.remove("approveBy");
        pdf.remove("approveAt");
        pdf.remove("finalApproveBy");
        pdf.remove("finalApproveAt");
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "评审成功", pdf));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "评审成功", pdf));
        }
    }

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
        int type = getParaToInt("type", 0); //保存0，提交1
        if(p == null){
            Pdf pdf = new Pdf()
                    .set("warehouseId", warehouseId)
                    .set("content", data)
                    .set("path", File.separator + "pdfs" + File.separator + name)
                    .set("status", type);
            pdf.save();
        }else {
            String tempPath = p.get("path");
            if(tempPath != null){
                File tempFile = new File(PathKit.getWebRootPath() + tempPath);
                tempFile.delete();
            }
            p.set("content", data);
            p.set("path", File.separator + "pdfs" + File.separator + name);
            if(!p.get("status").toString().equals("3") && type == 1){
                p.set("status", 1);
            }
            p.update();
        }
        Pdf pdf = Pdf.getPdfByWarehouseId(warehouseId);
        pdf.remove("approveAt");
        pdf.remove("approveBy");
        pdf.remove("finalApproveBy");
        pdf.remove("finalApproveAt");
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "成功", pdf));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes(CodeType.success, "成功", pdf));
        }
    }

    /**
     * 下载pdf
     */
    @Before(GET.class)
    @ActionKey("/api/hwc/pdf/download")
    public void download(){
        String path = getPara("path");
        File file = new File(PathKit.getWebRootPath() + path);
        if(file.exists()) {
            renderFile(file);
        }else {
            renderFile(new File(PathKit.getWebRootPath() + File.separator + "pdfs" + File.separator + "说明.pdf"));
        }
    }

    private Map<String, Object> getData(String warehouseId){
        Date date = TimeUtil.getNow();
        Map<String, Object> data = new HashMap<String, Object>();
        Warehouses warehouse = Warehouses.dao.findById(warehouseId);
        Pdf pdf = Pdf.getPdfByWarehouseId(warehouseId);
        if(pdf == null)
            data.put("pdf", null);
        else {
            pdf.remove("approveAt");
            pdf.remove("approveBy");
            pdf.remove("finalApproveBy");
            pdf.remove("finalApproveAt");
            data.put("pdf", pdf);
        }

        Samples sample = Samples.dao.findById(warehouse.get("sampleId"));
        sample.put("area", Area.findByAreaCode(sample.getStr("ssqx")));
        sample.put("yuanqu", Yuanqu.getYuanquByCode(sample.getStr("ssyq")));
        data.put("sample", sample);

        warehouse.put("country", Country.getByCode(warehouse.getStr("hwcssgj")));
        data.put("warehouse", warehouse);
        Pdf record = Pdf.getPdfByWarehouseId(warehouseId);
        if(record == null)
            data.put("data", null);
        else{
            data.put("path", record.get("path"));
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

        date = Db.findFirst("select max(p.round) as round from hwc_reportplans p where p.tableGroupId = 2").getDate("round");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        List<Record> records = Db.find("select " + t1 + "* from " + "" +
                TableNames.hwcReports + ", " + TableNames.hwcReportPlans + ", " + TableNames.hwcSamples + ", " + TableNames.hwcTable1 + " " +
                "where " + r + "status != 0 and " + r + "tableId = 1 and " + s + "id = " + warehouse.get("sampleId") + " and " + r + "sampleId = " + s + "id and " + r + "tableReportId = " + t1 + "id " +
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
                "where " + r + "status != 0 and " + r + "tableId = 2 and " + w + "id = " + warehouseId + " and " + r + "warehouseId = " + w + "id and " + r + "tableReportId = " + t2 + "id " +
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

        date = Db.findFirst("select max(p.round) as round from hwc_reportplans p where p.tableGroupId = 3").getDate("round");
        sf = new SimpleDateFormat("yyyy-MM");
        records = Db.find("select " + t3 + "table9 as table9, " + "sum(" + t3 + "xymj) as xymj, sum(" + t3 + "ytrsymj) as ytrsymj, sum(" + t3 + "zjmj) as zjmj, sum(" + t3 + "ljjstze) as ljjstze, sum(" + t3 + "sbgztr) as sbgztr, sum(" + t3 + "ckzltr) as ckzltr, sum(" + t3 + "ckgztr) as ckgztr, sum(" + t3 + "ljjstzeqt) as ljjstzeqt, sum(" + t3 + "ckz) as ckz, sum(" + t3 + "yyxsr) as yyxsr, sum(" + t3 + "xssr) as xssr, sum(" + t3 + "zyxs) as zyxs, sum(" + t3 + "rkddlbhms) as rkddlbhms, sum(" + t3 + "rkddlzyms) as rkddlzyms, sum(" + t3 + "ckddlbhms) as ckddlbhms, sum(" + t3 + "ckddlzyms) as ckddlzyms, sum(" + t3 + "byljrkz) as byljrkz, sum(" + t3 + "byljckz) as byljckz, sum(" + t3 + "qmzkhz) as qmzkhz, sum(" + t3 + "fwqysl) as fwqysl, sum(" + t3 + "hzqy) as hzqy, sum(" + t3 + "hyqys) as hyqys, sum(" + t3 + "qmcyrs) as qmcyrs, sum(" + t3 + "gnry) as gnry, sum(" + t3 + "gwry) as gwry, sum(" + t3 + "jsry) as jsry, sum(" + t3 + "qmcyryqt) as qmcyryqt, sum(" + t3 + "ygxc) as ygxc, sum(" + t3 + "yxry) as yxry, sum(" + t3 + "rkhwzl) as rkhwzl, sum(" + t3 + "ckhwzl) as ckhwzl, sum(" + t3 + "qmzkhwzl) as qmzkhwzl from " + "" +
                TableNames.hwcReports + ", " + TableNames.hwcReportPlans + ", " + TableNames.hwcWarehouses + ", " + TableNames.hwcTable3 + " " +
                "where " + r + "status != 0 and " + r + "tableId = 3 and " + w + "id = " + warehouseId + " and " + r + "warehouseId = " + w + "id and " + r + "tableReportId = " + t3 + "id " +
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, 0);
        Date firstMonthDate = calendar.getTime();
        records = Db.find("select group_concat(" + t3 + "table11  separator '-_-_-') as table11, " + "sum(" + t3 + "xymj) as xymj, sum(" + t3 + "ytrsymj) as ytrsymj, sum(" + t3 + "zjmj) as zjmj, sum(" + t3 + "ljjstze) as ljjstze, sum(" + t3 + "sbgztr) as sbgztr, sum(" + t3 + "ckzltr) as ckzltr, sum(" + t3 + "ckgztr) as ckgztr, sum(" + t3 + "ljjstzeqt) as ljjstzeqt, sum(" + t3 + "ckz) as ckz, sum(" + t3 + "yyxsr) as yyxsr, sum(" + t3 + "xssr) as xssr, sum(" + t3 + "zyxs) as zyxs, sum(" + t3 + "rkddlbhms) as rkddlbhms, sum(" + t3 + "rkddlzyms) as rkddlzyms, sum(" + t3 + "ckddlbhms) as ckddlbhms, sum(" + t3 + "ckddlzyms) as ckddlzyms, sum(" + t3 + "byljrkz) as byljrkz, sum(" + t3 + "byljckz) as byljckz, sum(" + t3 + "qmzkhz) as qmzkhz, sum(" + t3 + "fwqysl) as fwqysl, sum(" + t3 + "hzqy) as hzqy, sum(" + t3 + "hyqys) as hyqys, sum(" + t3 + "qmcyrs) as qmcyrs, sum(" + t3 + "gnry) as gnry, sum(" + t3 + "gwry) as gwry, sum(" + t3 + "jsry) as jsry, sum(" + t3 + "qmcyryqt) as qmcyryqt, sum(" + t3 + "ygxc) as ygxc, sum(" + t3 + "yxry) as yxry, sum(" + t3 + "rkhwzl) as rkhwzl, sum(" + t3 + "ckhwzl) as ckhwzl, sum(" + t3 + "qmzkhwzl) as qmzkhwzl from " + "" +
                TableNames.hwcReports + ", " + TableNames.hwcReportPlans + ", " + TableNames.hwcWarehouses + ", " + TableNames.hwcTable3 + " " +
                "where " + r + "status != 0 and " + r + "tableId = 3 and " + w + "id = " + warehouseId + " and " + r + "warehouseId = " + w + "id and " + r + "tableReportId = " + t3 + "id " +
                "and " + r + "planId = " + p + "id and DATE_FORMAT(" + p + "round, '%Y-%m') >= '" + sf.format(firstMonthDate) + "' " +
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
        System.out.println(JsonKit.toJson(data));
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
