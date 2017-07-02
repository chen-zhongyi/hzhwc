package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.interceptor.LoginInterceptor;
import com.hzhwck.model.hwc.Samples;
import com.hzhwck.model.hwc.ServiceCompanys;
import com.hzhwck.model.hwc.Warehouses;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/7/1.
 */
@Clear(LoginInterceptor.class)
public class ExcelController extends BaseController{
    private static final int N = 7;

    /**
     * 通过Excel导入海外仓服务企业信息
     */
    @Clear(POST.class)
    @ActionKey("/api/hwc/excel/in")
    public void in(){
        UploadFile excel = getFile("excel");
        String warehouseId = getPara("warehouseId");
        Warehouses warehouse = Warehouses.dao.findById(warehouseId);
        Map<String, Object> loginUser = getSessionAttr("user");
        Samples sample = (Samples) loginUser.get("sample");
        if(sample == null || warehouse == null || sample.getInt("id") != warehouse.getInt("sampleId")){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("02", "参数出错，海外仓不存在, 或者添加对不属于自己的海外仓操作", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("02", "参数出错，海外仓不存在, 或者添加对不属于自己的海外仓操作", null));
            }
            return ;
        }
        String fileName = excel.getFileName();
        String suffix = fileName.substring(fileName.indexOf(".")).toLowerCase();
        System.out.println("suffix --> " + suffix );
        if(!(suffix.equals(".xls") || suffix.equals(".xlsx"))){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("02", "参数出错，只支持.xls和.xlsx格式的Excel", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("02", "参数出错，只支持.xls和.xlsx格式的Excel", null));
            }
            return ;
        }
        try {
            Workbook wb = WorkbookFactory.create(excel.getFile());
            for(Sheet sheet : wb){
                int st = Math.min(20, sheet.getFirstRowNum());
                int end = Math.max(200, sheet.getLastRowNum());
                Row row = sheet.getRow(st);
                int maxNum = Math.max(20, row.getLastCellNum());
                int[] index = new int[N];
                for(int i = 0;i < maxNum;++i){
                    Cell cell = row.getCell(i, Row.RETURN_BLANK_AS_NULL);
                    if(cell == null)    continue;
                    if(cell.getStringCellValue().trim().equals("企业名称")){
                        index[0] = i;
                    }
                    if(cell.getStringCellValue().trim().equals("联系人")){
                        index[1] = i;
                    }
                    if(cell.getStringCellValue().trim().equals("企业介绍")){
                        index[2] = i;
                    }
                    if(cell.getStringCellValue().trim().equals("地址")){
                        index[3] = i;
                    }
                    if(cell.getStringCellValue().trim().equals("货物类型")){
                        index[4] = i;
                    }
                    if(cell.getStringCellValue().trim().equals("合同截止时间")){
                        index[5] = i;
                    }
                }
                System.out.println("index --> " + Arrays.toString(index));
                String[] data = new String[N];
                for(int i = st + 1;i < end;++i){
                    row = sheet.getRow(i);
                    if(row == null) continue;
                    maxNum = Math.max(20, row.getLastCellNum());
                    boolean success = true;
                    for(int j = 0;j < 6;++j){
                        if(index[j] >= maxNum)  {
                            success = false;
                            break;
                        }
                        Cell cell = row.getCell(index[j], Row.RETURN_BLANK_AS_NULL);
                        if(cell == null){
                            success = false;
                            break;
                        }
                        if(j == 5){
                            String date = cell.getStringCellValue();
                            System.out.println("date --> " + date);
                            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                            sf.setLenient(false);
                            try {
                                sf.parse(date);
                                data[j] = date;
                            }catch (ParseException e){
                                e.printStackTrace();
                                success = false;
                                break;
                            }
                        }else {
                            data[j] = cell.getStringCellValue();
                        }
                    }
                    if(success){
                        ServiceCompanys s = new ServiceCompanys()
                                .set("qymc", data[0])
                                .set("lxr", data[1])
                                .set("qyjs", data[2])
                                .set("dz", data[3])
                                .set("hwlx", data[4])
                                .set("htjzsj", data[5])
                                .set("warehouseId", warehouseId);
                        System.out.println("isExist --> " + ServiceCompanys.isExist(s));
                        if(ServiceCompanys.isExist(s) == true)  continue;
                        if(s.save() == false){
                            if(getPara("callback") != null) {
                                String json = JsonKit.toJson(ResponseUtil.setRes("01", "导入失败，数据库异常", null));
                                renderJson(getPara("callback", "default") + "(" + json + ")");
                            } else {
                                renderJson(ResponseUtil.setRes("01", "导入失败，数据库异常", null));
                            }
                            return ;
                        }
                    }
                }
            }
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "导入成功", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("00", "导入成功", null));
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        //excel.getFile().delete();
        excel.getFile().deleteOnExit();
    }

    /**
     * 海外仓服务企业，导出Excel
     */
    @Before(GET.class)
    @ActionKey("/api/hwc/excel/out")
    public void out(){
        String warehouseId = getPara("warehouseId");
        List<ServiceCompanys> serviceCompanys = ServiceCompanys.getServiceCompanysByWarehouseId(warehouseId);
        Warehouses w = Warehouses.dao.findById(warehouseId);
        Samples s = Samples.dao.findById(w.get("sampleId"));
        String name = s.getStr("jsdwmc") + "-" + w.getStr("hwcmc");
        createExcel(serviceCompanys, name);
        String path = PathKit.getWebRootPath() + File.separator + name + ".xls";
        File file = new File(path);
        renderFile(file);
    }
    private void createExcel(List<ServiceCompanys> serviceCompanys, String name) {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("服务企业名单");

        // Create a new font and alter it.
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)12);
        font.setFontName("Courier New");

        // Fonts are set into a style so create a new one to use.
        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);

        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow(0);

        // Create a cell and put a value in it.
        setCell(row.createCell(0), "序号", style);
        setCell(row.createCell(1), "企业名称", style);
        setCell(row.createCell(2), "联系人", style);
        setCell(row.createCell(3), "企业介绍", style);
        setCell(row.createCell(4), "地址", style);
        setCell(row.createCell(5), "货物类型", style);
        setCell(row.createCell(6), "合同截止时间", style);

        for(int i = 0;i < serviceCompanys.size();++i){
            ServiceCompanys temp = serviceCompanys.get(i);
            ///System.out.println(temp.toRecord().getColumns());
            Row r = sheet.createRow(i + 1);
            setCell(r.createCell(0), String.valueOf(i + 1), style);
            setCell(r.createCell(1), temp.getStr("qymc"), style);
            setCell(r.createCell(2), temp.getStr("lxr"), style);
            setCell(r.createCell(3), temp.getStr("qyjs"), style);
            setCell(r.createCell(4), temp.getStr("dz"), style);
            setCell(r.createCell(5), temp.getStr("hwlx"), style);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            setCell(r.createCell(6), sf.format(temp.getDate("htjzsj")), style);
        }

        for(int i = 0;i < N;++i) {
            sheet.autoSizeColumn(i);
        }
        FileOutputStream fileOut = null;
        try {
            // Write the output to a file
            fileOut = new FileOutputStream(PathKit.getWebRootPath() + File.separator + name + ".xls");
            wb.write(fileOut);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                fileOut.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void setCell(Cell cell, String value, CellStyle style){
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
}
