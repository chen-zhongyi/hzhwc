package com.hzhwck.util;

import com.hzhwck.model.hwc.*;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/7/9.
 */
public class PDF {

    public static void getPdf(Map<String, Object> warehouse, Map<String, Object> data, String path){
        try {
            createPdf(path, warehouse, data);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void createPdf(String dest, Map<String, Object> w, Map<String, Object> data) throws IOException{
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PageSize pageSize = PageSize.A4;
        Document doc = new Document(pdfDoc, pageSize);

        PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H", false);
        Style code = new Style();
        code.setFont(font)
                .setFontSize(17)
                .setFontColor(Color.BLACK);

        Paragraph text = new Paragraph("杭州公共海外仓评审材料")
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(font)
                .setFontSize(30);
        Samples sample = (Samples) w.get("sample");

        Warehouses warehouse = (Warehouses) w.get("warehouse");
        Record table1 = (Record) w.get("table1");
        Record table2 = (Record) w.get("table2");
        Record month = (Record) w.get("month");
        Record year = (Record) w.get("year");
        ServiceCompanys serviceCompanys = (ServiceCompanys) w.get("servicecompanys");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        Date date = new Date(System.currentTimeMillis());

        doc.add(text);
        doc.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));
        doc.add(new Paragraph());
        doc.add(new Paragraph());
        doc.add(new Paragraph());
        doc.add(new Paragraph("项目名称：______" + data.get("xmmc") + "_____________")
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(font)
                .setFontSize(20));
        doc.add(new Paragraph("申请单位：______" + sample.get("jsdwmc") + "_____________")
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(font)
                .setFontSize(20));
        doc.add(new Paragraph("联 系 人：______" + sample.get("lxr") + "_____________")
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(font)
                .setFontSize(20));
        doc.add(new Paragraph("联系电话：_______" + sample.get("lxsj") + "____________")
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(font)
                .setFontSize(20));
        doc.add(new Paragraph("申请日期：_______" + sf.format(date) + "____________")
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(font)
                .setFontSize(20));

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        doc.add(new Paragraph("承诺书")
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(font)
                .setFontSize(20));
        doc.add(new Paragraph(" 我公司郑重承诺：\n" )
                .setFirstLineIndent(40)
                .setFont(font)
                .setFontSize(20));
        doc.add(new Paragraph(
                "   在本次市级公共海外仓评审中所申报的内容及相关资质证明材料，均系真实内容。如评审单位发现有不真实之处，我公司无条件接受处罚决定，并承担由此造成的一切后果。\n" )
                .setFirstLineIndent(40)
                .setFont(font)
                .setFontSize(20));
        doc.add(new Paragraph("   我公司将认真对待本次评审，完全接受评审单位做出的评审结果。如果我公司被评为市级公共海外仓，作为服务单位，我公司将接受市级公共海外仓的管理规定，履行相关责任和义务，遵守服务承诺。\n")
                .setFirstLineIndent(40)
                .setFont(font)
                .setFontSize(20));
        doc.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));
        doc.add(new Paragraph("          海外仓运营企业：" + sample.get("jsdwmc") + "           （盖章）")
                .setFont(font)
                .setFontSize(20));
        doc.add(new Paragraph("法定代表人（或委托代理人）：" + sample.get("frdb") + "           （盖章）")
                .setFont(font)
                .setFontSize(20));
        doc.add(new Paragraph("                  日期：" + sf.format(date))
                .setFont(font)
                .setFontSize(20));

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        Paragraph title = new Paragraph("表1  建设单位情况");
        title.setTextAlignment(TextAlignment.CENTER)
                .addStyle(code);
        doc.add(title);

        Table table = new Table(10);
        //------1
        Cell cell= new Cell(1, 10).add("一、基本情况")
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("建设单位名称")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(sample.getStr("jsdwmc"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("所属区县")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(Area.findByAreaCode(sample.getStr("ssqx")).getStr("name"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("入驻园区")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(Yuanqu.getYuanquByCode(sample.getStr("ssyq")).getStr("name"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);

        table.addCell(cell);
        cell = new Cell(1, 2).add("法人代表")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(sample.getStr("frdb"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("社会统一信用代码")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(sample.getStr("shtyxydm"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("企业注册地址")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(sample.getStr("qyzcdz"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("办公地址")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(sample.getStr("bgdz"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(2, 2).add("联系人")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add(sample.getStr("lxr"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add("联系电话")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 4).add("(手机)" + sample.getStr("lxsj"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 4).add("(电话)" + sample.getStr("lxdh"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("二、企业情况简介")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add(data.get("qyqkjs").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("三、企业经营情况")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("企业主营业务收入")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(table1.get("zyywsr").toString() + " 万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("出口总值")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(table1.get("ckzz").toString() + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("总资产")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 1).add(table1.get("zzc").toString() + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("总负债")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 1).add(table1.get("zfz").toString() + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("净资产")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(table1.get("jzc").toString() + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("总利润")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 1).add(table1.get("lrze").toString() + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("缴税额")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 1).add(table1.get("rjze").toString() + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("从业人员数")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(table1.get("nmcyrys").toString() + "人")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("四、海外仓业务开展情况")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("海外仓业务运营开始时间")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("hwcyykssj").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("海外仓建设累计投入资金")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(month.get("ljjstze").toString() + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(2, 2).add("现有海外仓个数")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 3).add(table1.get("xyhwcgs").toString() + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("其中：已运营")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(table1.get("yyy").toString() + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("在建")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(table1.get("zj").toString() + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        doc.add(table);

        //------2
        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        title = new Paragraph("表2  公共海外仓评审表");
        title.setTextAlignment(TextAlignment.CENTER)
                .addStyle(code);
        doc.add(title);

        table = new Table(10);
        cell = new Cell(1, 10).add("第一部分 基本情况")
                .setTextAlignment(TextAlignment.CENTER)
                .addStyle(code);
        table.addCell(cell);

        table = new Table(10);
        cell = new Cell(1, 10).add("一、平台名称")
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("海外仓名称")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(warehouse.getStr("hwcmc"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("海外仓境外运营公司名称")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(warehouse.getStr("yygsmc"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(2, 2).add("仓库租赁合同起讫时间")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("起始时间")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(sf.format(warehouse.getDate("ckzlqssj")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add("合同租赁面积")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add(warehouse.getStr("ckzlmj"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("终止时间")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(sf.format(warehouse.getDate("ckzljssj")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("进出口权")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(warehouse.getStr("jckq"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("登记证书编号")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(warehouse.getStr("zjhm") + "m2")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(2, 2).add("营销平台")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("（境内）")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 6).add(warehouse.getStr("jnyxpt"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("（境外）")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 6).add(warehouse.getStr("jwyxpt"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(3, 2).add("海外仓联系人（境内）")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(3, 3).add(data.get("jnlxr").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("联系电话")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("jnlxdh").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("电子邮箱")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("jndzyx").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("传真")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("jncz").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(3, 2).add("海外仓联系人（境外）")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(3, 3).add(data.get("jwlxr").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("联系电话")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("jwlxdh").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("电子邮箱")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("jwdzyx").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("传真")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("jwcz").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("二、海外仓位置")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("所在国")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(Country.getByCode(warehouse.getStr("hwcssgj")).getStr("name"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("城市")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(warehouse.getStr("hwcsscs"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("详细地址")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(warehouse.getStr("hwcxxdz"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("位置中枢")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(warehouse.getStr("hwcwzzs"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("区位优势")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(data.get("qwys").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("三、海外仓用途与类型")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("仓库用途")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(warehouse.getStr("ckyt"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("仓库类型")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(warehouse.getStr("cklx"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他说明")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(data.get("qtsm1").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("四、区域划分")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add(data.get("ccqyhf").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("仓库平面图。*附件方式提交*，备查")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("五、货物摆放")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add(data.get("hwbf").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("六、海外仓发展目标")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add(data.get("hwcfzmb").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("七、投资情况说明")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add(data.get("tzqksm").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("第二部分 整体规模")
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("一、基础投入规模")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("（一）海外仓面积")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(2, 2).add("现有面积")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add(month.get("xymj").toString() + "m2")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("其中：已投入使用面积")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(month.get("ytrsymj").toString() + "m2")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("在建面积")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(month.get("zjmj") + "m2")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他说明")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(data.get("qtsm3").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("（二）投资与设施")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("累计建设投资额")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(month.get("ljjstze") + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(4, 2).add("其中")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("设备购置投入")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 5).add(month.get("sbgztr") + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("仓库租赁投入")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 5).add(month.get("ckzltr") + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("仓库改造投入")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 5).add(month.get("ckgztr") + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("其他")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 5).add(month.get("ljjstzeqt") + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("生产设备简要描述")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(data.get("scsbjyms").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("（三）人员配置")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("从业人员数")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(month.get("qmcyrs") + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(2, 2).add("按国籍分")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("国内人员")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 6).add(month.get("gnry") + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("国外人员")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 6).add(month.get("gwry") + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(4, 2).add("按岗位分")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("技术人员")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 6).add(month.get("jsry") + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("管理人员")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 6).add(month.get("glry") + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("营销人员")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 6).add(month.get("yxry") + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("其他")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 6).add(month.get("qmcyryqt") + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他说明")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(data.get("qtsm2").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("二、交易规模")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("（一）营收规模（2016年度）")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("年度出口值")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(data.get("ndckz").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("年度营业收入总额")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(data.get("ndyysrze").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(7, 1).add("其中")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(5, 2).add("服务性营业收入")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(5, 2).add(data.get("fwxyysr").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 5).add("其中")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("头程")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("tc").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("仓储")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("cc").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("配送")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("ps").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("其他")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("qt").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add("销售收入")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add(data.get("xssr").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 5).add("其中")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("自营销售")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("zysr").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他说明")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(data.get("qtsm4").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("（二）海外仓吞吐能力（2016年度）")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 5).add("入库订单量（货量）")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 5).add("出库订单量")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(2, 1).add("其中")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("备货模式")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(data.get("rkbhms").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 1).add("其中")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("备货模式")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(data.get("ckbhms").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("直邮模式")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(data.get("rkzyms").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("直邮模式")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(data.get("ckzyms").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("日均订单量最大处理能力")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("rddzdclnl").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("实际日均订单量")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(data.get("sjrjddclnl").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他说明")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(data.get("qtsm5").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("第三部分 服务能力与服务创新")
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("一、服务能力")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(2, 2).add("服务企业数量")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 3).add(month.get("fwqysl") + "家")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("其中杭州企业")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(month.get("hzqy") + "家")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("活跃企业数")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(month.get("hyqys") + "家")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他说明")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(data.get("qtsm6").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("二、服务配套")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("物流服务")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(table2.getStr("wlfw"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("金融服务")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(table2.getStr("jrfw"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("代办服务")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(table2.getStr("dbyw"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("营销推广")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(table2.getStr("yxtg"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他服务")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(table2.getStr("qt"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("第四部分 仓储管理信息化建设")
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("一、已采用的管理系统")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add(data.get("ycydglxt").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("二、系统对接情况")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("与电商平台的对接情况")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(data.get("ydsptdjdqk").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(data.get("xtqt").toString())
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        doc.add(table);

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        doc.add(new Paragraph("附件 1 境内营业执照")
                .setFontSize(25)
                .setFont(font));
        Image img1 = new Image(ImageDataFactory.create(PathKit.getWebRootPath() + File.separator + warehouse.getStr("yyzz")));
        doc.add(img1);

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        doc.add(new Paragraph("附件 2 境外营业执照（或经营许可）")
                .setFontSize(25)
                .setFont(font));

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        doc.add(new Paragraph("附件 3 仓库租赁协议")
                .setFontSize(25)
                .setFont(font));

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        doc.add(new Paragraph("附件 4 投报单位注册登记证书")
                .setFontSize(25)
                .setFont(font));

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        doc.add(new Paragraph("附件 5 境外公司股权证明")
                .setFontSize(25)
                .setFont(font));

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        doc.add(new Paragraph("附件 6 境外公司投资协议")
                .setFontSize(25)
                .setFont(font));

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        doc.add(new Paragraph("附件 7 清关资质证明材料")
                .setFontSize(25)
                .setFont(font));

    /*int n = pdfDoc.getNumberOfPages();
    Paragraph footer;
    for (int page = 1; page <= n; page++) {
        footer = new Paragraph(String.format("Page %s of %s", page, n));
        doc.showTextAligned(footer, 297.5f, 20, page,
                TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
    }*/

        doc.close();

    /*doc.close();
    PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
    Document document = new Document(pdf);
    //document.setTextAlignment(TextAlignment.JUSTIFIED);
            //.setHyphenation(new HyphenationConfig("en", "uk", 3, 3));
    PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H", false);
    Style code = new Style();
    code.setFont(font)
            .setFontSize(12)
            .setFontColor(Color.RED);*/

    /*BufferedReader br = new BufferedReader(new FileReader(SRC));
    String line;
    while((line = br.readLine()) != null) {
        //System.out.print(line + "\n");
        Text text = new Text(line + "\n");
        document.add(new Paragraph().add(text));
    }
    br.close();*/
        //document.close();
    /*PdfPage page = pdf.addNewPage();
    PdfCanvas pdfCanvas = new PdfCanvas(page);
    Rectangle rectangle = new Rectangle(36, 650, 100, 100);
    pdfCanvas.rectangle(rectangle);
    pdfCanvas.stroke();
    Canvas canvas = new Canvas(pdfCanvas, pdf, rectangle);


    //bold加粗 italic斜体
    Text title = new Text("第一个PDF, 你是好人， 我是坏人， 哈哈哈哈哈哈哈哈").setFont(font)
            .setFontSize(23)
            //.setBold()
            //.setItalic()
            .setStrokeColor(Color.RED)
            .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.FILL_STROKE);
    Text author = new Text("陈忠意").addStyle(code).setBackgroundColor(Color.LIGHT_GRAY);
    Paragraph p = new Paragraph().add(title).add(" by ").add(author);
    canvas.add(p);
    pdf.close();*/
    }

}
