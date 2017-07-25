package com.hzhwck.util;

import com.hzhwck.model.hwc.*;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
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

    public static String isNull(Object object){
        if(object == null)  return "";
        return object.toString();
    }

    public static void createPdf(String dest, Map<String, Object> w, Map<String, Object> data) throws IOException{
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PageSize pageSize = PageSize.A4;
        Document doc = new Document(pdfDoc, pageSize, false);
        doc.setLeftMargin(60);
        doc.setRightMargin(60);
        doc.setTopMargin(60);
        doc.setBottomMargin(60);

        Samples sample = (Samples) w.get("sample");
        Warehouses warehouse = (Warehouses) w.get("warehouse");
        Record table1 = (Record) w.get("table1");
        Record table2 = (Record) w.get("table2");
        Record month = (Record) w.get("month");
        Record year = (Record) w.get("year");
        java.util.List<ServiceCompanys> serviceCompanys = (java.util.List<ServiceCompanys>) w.get("serviceCompanys");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
        Date date = new Date(System.currentTimeMillis());

        PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H", false);
        Style code = new Style();
        code.setFont(font)
                .setFontSize(11)
                .setFontColor(Color.BLACK)
                //.setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        Style codeTitle = new Style();
        codeTitle.setFont(font)
                .setFontSize(14)
                .setFontColor(Color.BLACK)
                .setBold();

        Style codeArea = new Style();
        codeArea.setFont(font)
                .setFontSize(14)
                .setFontColor(Color.BLACK);

        Style front = new Style()
                .setFont(font)
                .setFontSize(18)
                .setFontColor(Color.BLACK);

        doc.add(new Paragraph("\n\n\n\n\n\n\n\n\n"));
        Paragraph text = new Paragraph("杭州公共海外仓评审材料")
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(font)
                .setFontSize(40);
        doc.add(text);
        doc.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n"));
        doc.add(new Paragraph());
        doc.add(new Paragraph());
        doc.add(new Paragraph());
        doc.add(new Paragraph().add(new Tab()).add(new Tab()).add("项目名称：" + isNull(data.get("xmmc")))
                .addStyle(front).setFontSize(20));
        doc.add(new Paragraph().add(new Tab()).add(new Tab()).add("申请单位：" + isNull(sample.get("jsdwmc")))
                .addStyle(front).setFontSize(20));
        doc.add(new Paragraph().add(new Tab()).add(new Tab()).add("联  系   人：" + isNull(sample.get("lxr")))
                .addStyle(front).setFontSize(20));
        doc.add(new Paragraph().add(new Tab()).add(new Tab()).add("联系电话：" + isNull(sample.get("lxsj")))
                .addStyle(front).setFontSize(20));
        doc.add(new Paragraph().add(new Tab()).add(new Tab()).add("申请日期：" + sf.format(date))
                .addStyle(front).setFontSize(20));

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        doc.add(new Paragraph("承诺书")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .addStyle(front));

        doc.add(new Paragraph("\n\n"));
        doc.add(new Paragraph(" 我公司郑重承诺：\n" )
                .setFirstLineIndent(40)
                .addStyle(front));
        doc.add(new Paragraph(
                "   在本次市级公共海外仓评审中所申报的内容及相关资质证明材料，均系真实内容。如评审单位发现有不实之处，我公司无条件接受处罚决定，并承担由此造成的一切后果。" )
                .setFirstLineIndent(40)
                .addStyle(front));
        doc.add(new Paragraph("   我公司将认真对待本次评审，完全接受评审单位做出的评审结果。如果我公司被评为市级公共海外仓，作为服务单位，我公司将接受市级公共海外仓的管理规定，履行相关责任和义务，遵守服务承诺。")
                .setFirstLineIndent(40)
                .addStyle(front));
        doc.add(new Paragraph("\n\n\n\n\n\n\n\n\n"));
        doc.add(new Paragraph().add(new Tab()).add("海外仓运营企业：" + isNull(sample.get("jsdwmc")) + "                     （盖章）")
                .addStyle(front));
        doc.add(new Paragraph().add(new Tab()).add("法定代表人（或委托代理人）："  + isNull(sample.get("frdb")) + "           （盖章）")
                .addStyle(front));
        doc.add(new Paragraph().add(new Tab()).add("日期：" + sf.format(date))
                .addStyle(front));

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        Paragraph title = new Paragraph("表1  建设单位情况");
        title.setTextAlignment(TextAlignment.CENTER)
                .addStyle(codeTitle);
        doc.add(title);

        Table table = new Table(10);
        //------1
        Cell cell= new Cell(1, 10).add("一、基本情况")
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 2).add("建设单位名称")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                //.setTextAlignment(TextAlignment.CENTER)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(sample.get("jsdwmc")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("所属区县")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(sample.get("ssqx") == null ? "" : Area.findByAreaCode(sample.getStr("ssqx")).getStr("name"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("入驻园区")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(sample.get("ssyq") == null ? "" : Yuanqu.getYuanquByCode(sample.getStr("ssyq")).getStr("name"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);

        table.addCell(cell);
        cell = new Cell(1, 2).add("法人代表")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(sample.get("frdb")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("社会统一信用代码")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(sample.get("shtyxydm")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("企业注册地址")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(sample.get("qyzcdz")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("办公地址")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(sample.get("bgdz")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(2, 2).add("联系人")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add(isNull(sample.get("lxr")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add("联系电话")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 4).add("(手机)" + isNull(sample.get("lxsj")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 4).add("(电话)" + isNull(sample.get("lxdh")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("二、企业情况简介")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 10).add(isNull(data.get("qyqkjs")))
                .setHeight(200)
                .addStyle(codeArea);
        table.addCell(cell);

        cell = new Cell(1, 10).add("三、企业经营情况")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 3).add("企业主营业务收入")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(table1.get("zyywsr")) + " 万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("出口总值")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(table1.get("ckzz")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 1).add("总资产")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(table1.get("zzc")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 1).add("总负债")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(table1.get("zfz")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("净资产")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(table1.get("jzc")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 1).add("总利润")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(table1.get("lrze")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 1).add("缴税额")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(table1.get("rjze")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("从业人员数")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(table1.get("nmcyrys")) + "人")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("四、海外仓业务开展情况")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 3).add("海外仓业务运营开始时间")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(data.get("hwcyykssj")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("海外仓建设累计投入资金")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(month.get("ljjstze")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(2, 3).add("现有海外仓个数")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add(isNull(table1.get("xyhwcgs")) + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("已运营")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(table1.get("yyy")) + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("在建")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(table1.get("zj")) + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        doc.add(table);

        //------2
        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        title = new Paragraph("表2  公共海外仓评审表");
        title.setTextAlignment(TextAlignment.CENTER)
                .addStyle(codeTitle);
        doc.add(title);

        table = new Table(10);
        cell = new Cell(1, 10).add("第一部分 基本情况")
                .setTextAlignment(TextAlignment.CENTER)
                .addStyle(codeTitle);
        table.addCell(cell);

        table = new Table(10);
        cell = new Cell(1, 10).add("一、平台名称")
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 3).add("海外仓名称")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 7).add(isNull(warehouse.get("hwcmc")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 3).add("海外仓境外运营公司名称")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 7).add(isNull(warehouse.get("yygsmc")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(2, 3).add("仓库租赁合同起讫时间")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("起始时间")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(warehouse.get("ckzlqssj") == null ? "" : sf.format(warehouse.getDate("ckzlqssj")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add("合同租赁面积")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 1).add(isNull(warehouse.get("ckzlmj")) + "m2")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("终止时间")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(warehouse.get("ckzljssj") == null ? "" : sf.format(warehouse.getDate("ckzljssj")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 3).add("进出口权")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(warehouse.get("jckq")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("登记证书编号")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(warehouse.get("zjhm")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(2, 3).add("营销平台")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("（境内）")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 5).add(isNull(warehouse.get("jnyxpt")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("（境外）")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 5).add(isNull(warehouse.get("jwyxpt")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(3, 3).add("海外仓联系人（境内）")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(3, 2).add(isNull(data.get("jnlxr")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("联系电话")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(data.get("jnlxdh")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("电子邮箱")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(data.get("jndzyx")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("传真")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(data.get("jncz")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(3, 3).add("海外仓联系人（境外）")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(3, 2).add(isNull(data.get("jwlxr")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("联系电话")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(data.get("jwlxdh")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("电子邮箱")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(data.get("jwdzyx")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("传真")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(data.get("jwcz")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("二、海外仓位置")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 2).add("所在国")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(warehouse.get("hwcssgj") == null ? "" : Country.getByCode(warehouse.getStr("hwcssgj")).getStr("name"))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("城市")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(warehouse.get("hwcsscs")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("详细地址")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(warehouse.get("hwcxxdz")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("位置中枢")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(warehouse.get("hwcwzzs")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("区位优势")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(data.get("qwys")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("三、海外仓用途与类型")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 2).add("仓库用途")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(warehouse.get("ckyt")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("仓库类型")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(warehouse.get("cklx")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他说明")
                .setHeight(180)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(data.get("qtsm1")))
                .setHeight(180)
                .addStyle(codeArea);
        table.addCell(cell);

        cell = new Cell(1, 10).add("四、区域划分")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 10).add(isNull(data.get("ccqyhf")))
                .setHeight(140)
                .addStyle(codeArea);
        table.addCell(cell);

        cell = new Cell(1, 10).add("五、货物摆放")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 10).add(isNull(data.get("hwbf")))
                .setHeight(140)
                .addStyle(codeArea);
        table.addCell(cell);

        cell = new Cell(1, 10).add("六、海外仓发展目标")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 10).add(isNull(data.get("hwcfzmb")))
                .setHeight(170)
                .addStyle(codeArea);
        table.addCell(cell);

        cell = new Cell(1, 10).add("七、投资情况说明")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 10).add(isNull(data.get("tzqksm")))
                .setHeight(140)
                .addStyle(codeArea);
        table.addCell(cell);

        cell = new Cell(1, 10).add("第二部分 整体规模")
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 10).add("一、基础投入规模")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 10).add("（一）海外仓面积")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(2, 2).add("现有面积")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add(isNull(month.get("xymj")) + "m2")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("其中：已投入使用面积")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(month.get("ytrsymj")) + "m2")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("在建面积")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(month.get("zjmj")) + "m2")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他说明")
                .setHeight(70)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeArea);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(data.get("qtsm3")))
                .setHeight(70)
                .addStyle(codeArea);
        table.addCell(cell);

        cell = new Cell(1, 10).add("（二）投资与设施")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 2).add("累计建设投资额")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(month.get("ljjstze")) + "万元")
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
        cell = new Cell(1, 5).add(isNull(month.get("sbgztr")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("仓库租赁投入")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 5).add(isNull(month.get("ckzltr")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("仓库改造投入")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 5).add(isNull(month.get("ckgztr")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("其他")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 5).add(isNull(month.get("ljjstzeqt")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("生产设备简要描述")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHeight(130)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(data.get("scsbjyms")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHeight(130)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("（三）人员配置")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 3).add("从业人员数")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 7).add(isNull(month.get("qmcyrs")) + "人")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 3).add("其中：境内工作人员")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 7).add(isNull(month.get("gnry")) + "人")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 3).add("境外工作人员")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 7).add(isNull(month.get("gwry")) + "人")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 3).add("其中：技术人员")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 7).add(isNull(month.get("jsry")) + "人")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        /*cell = new Cell(2, 2).add("按国籍分")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("境内工作人员")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 6).add(isNull(month.get("gnry")) + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("境外工作人员")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 6).add(isNull(month.get("gwry")) + "个")
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
        cell = new Cell(1, 6).add(isNull(month.get("jsry")) + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("管理人员")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 6).add(isNull(month.get("glry")) + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("营销人员")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 6).add(isNull(month.get("yxry")) + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("其他")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 6).add(isNull(month.get("qmcyryqt")) + "个")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);*/

        cell = new Cell(1, 2).add("其他说明")
                .setHeight(120)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(data.get("qtsm2")))
                .setHeight(120)
                .addStyle(codeArea);
        table.addCell(cell);

        cell = new Cell(1, 10).add("二、交易规模")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 10).add("（一）营收规模（2016年度）")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 2).add("年度出口值")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(data.get("ndckz")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("年度营业收入总额")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(data.get("ndyysrze")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其中：分销收入")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(month.get("xssr")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("自营销售收入")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(month.get("zysr")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        /*cell = new Cell(7, 1).add("其中")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(5, 2).add("服务性\n营业收入")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(5, 2).add(isNull(data.get("fwxyysr")))
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
        cell = new Cell(1, 3).add(isNull(data.get("tc")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("仓储")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(data.get("cc")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("配送")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(data.get("ps")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("其他")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(data.get("qt")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add("销售收入")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 2).add(isNull(data.get("xssr")))
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
        cell = new Cell(1, 3).add(isNull(data.get("zysr")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);*/

        cell = new Cell(1, 2).add("其他说明")
                .setHeight(190)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(data.get("qtsm4")))
                .setHeight(190)
                .addStyle(codeArea);
        table.addCell(cell);

        cell = new Cell(1, 10).add("（二）海外仓吞吐能力（2016年度）")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 5).add("入库订单量")
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
        cell = new Cell(1, 2).add(isNull(data.get("rkddlbhms")) + "批")
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
        cell = new Cell(1, 2).add(isNull(data.get("ckddlbhms")) + "批")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("集货模式")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(data.get("rkddlzyms")) + "票")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("集货模式")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(data.get("ckddlzyms")) + "票")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 5).add("货物周转重量")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 5).add("货物周转价值")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 3).add("其中：入库货物重量")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(data.get("rkhwzl")) + "吨")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("其中：入库货物值")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(data.get("byljrkz")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 3).add("其中：出库货物重量")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(data.get("ckhwzl")) + "吨")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add("其中：出库货物值")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add(isNull(data.get("byljckz")) + "万元")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("日均订单量\n最大处理能力")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(data.get("rddzdclnl")) + "单")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("实际日均订单量")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(data.get("sjrjddclnl")) + "单")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他说明")
                .setHeight(180)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(data.get("qtsm5")))
                .setHeight(180)
                .addStyle(codeArea);
        table.addCell(cell);

        cell = new Cell(1, 10).add("第三部分 服务能力与服务创新")
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 10).add("一、服务能力")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(2, 2).add("服务企业数量")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(2, 3).add(isNull(month.get("fwqysl")) + "家")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("杭州企业数")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(month.get("hzqy")) + "家")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 2).add("活跃企业数")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 3).add(isNull(month.get("hyqys")) + "家")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他说明")
                .setHeight(140)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(data.get("qtsm6")))
                .setHeight(140)
                .addStyle(codeArea);
        table.addCell(cell);

        cell = new Cell(1, 10).add("二、服务配套")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 2).add("物流服务")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(table2.get("wlfw")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("金融服务")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(table2.get("jrfw")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("代办服务")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(table2.get("dbyw")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("营销推广")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(table2.get("yxtg")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他服务")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHeight(120)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(table2.get("qt")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHeight(120)
                .addStyle(code);
        table.addCell(cell);

        cell = new Cell(1, 10).add("第四部分 仓储管理信息化建设")
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 10).add("一、已采用的管理系统")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 10).add(isNull(data.get("ycydglxt")))
                .setHeight(160)
                .addStyle(codeArea);
        table.addCell(cell);

        cell = new Cell(1, 10).add("二、系统对接情况")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(codeTitle);
        table.addCell(cell);

        cell = new Cell(1, 2).add("与电商平台的对接情况")
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(data.get("ydsptdjdqk")))
                .setHeight(140)
                .addStyle(codeArea);
        table.addCell(cell);

        cell = new Cell(1, 2).add("其他")
                .setHeight(140)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addStyle(code);
        table.addCell(cell);
        cell = new Cell(1, 8).add(isNull(data.get("xtqt")))
                .setHeight(140)
                .addStyle(codeArea);
        table.addCell(cell);

        doc.add(table);

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        doc.add(new Paragraph("附件 1 境内营业执照")
                .setFontSize(16)
                .setFont(font));
        String name;
        Image img;
        if(warehouse.get("yyzz") != null) {
            name = warehouse.getStr("yyzz");
            img = new Image(ImageDataFactory.create(PathKit.getWebRootPath() + File.separator +
                    "hwckimages" + File.separator + name.split("/")[name.split("/").length - 1]));
            img.setHeight(297 * 2);
            img.setWidth(210 * 2);
            doc.add(img);
        }

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        doc.add(new Paragraph("附件 2 仓库平面图")
                .setFontSize(16)
                .setFont(font));
        if(warehouse.get("pmt") != null) {
            name = warehouse.getStr("pmt");
            img = new Image(ImageDataFactory.create(PathKit.getWebRootPath() + File.separator +
                    "hwckimages" + File.separator + name.split("/")[name.split("/").length - 1]));
            img.setHeight(297 * 2);
            img.setWidth(210 * 2);
            doc.add(img);
        }

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        doc.add(new Paragraph("附件 3 自营出口权证书")
                .setFontSize(16)
                .setFont(font));
        if(warehouse.get("smj") != null) {
            name = warehouse.getStr("smj");
            img = new Image(ImageDataFactory.create(PathKit.getWebRootPath() + File.separator +
                    "hwckimages" + File.separator + name.split("/")[name.split("/").length - 1]));
            img.setHeight(297 * 2);
            img.setWidth(210 * 2);
            doc.add(img);
        }

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        doc.add(new Paragraph("附件 4 服务企业名录")
                .setFontSize(16)
                .setFont(font));
        table = new Table(16);

        cell= new Cell(1, 1).add("序号")
                .addStyle(code);
        table.addCell(cell);
        cell= new Cell(1, 3).add("服务企业名称")
                .addStyle(code);
        table.addCell(cell);
        cell= new Cell(1, 2).add("联系人")
                .addStyle(code);
        table.addCell(cell);
        cell= new Cell(1, 2).add("所在地")
                .addStyle(code);
        table.addCell(cell);
        cell= new Cell(1, 2).add("货物类型")
                .addStyle(code);
        table.addCell(cell);
        cell= new Cell(1, 2).add("服务期限")
                .addStyle(code);
        table.addCell(cell);
        cell= new Cell(1, 4).add("备注")
                .addStyle(code);
        table.addCell(cell);

        for(int i = 1;i <= serviceCompanys.size();++i){
            ServiceCompanys s = serviceCompanys.get(i - 1);
            cell= new Cell(1, 1).add(i + "")
                    .addStyle(code);
            table.addCell(cell);
            cell= new Cell(1, 3).add(isNull(s.get("qymc")))
                    .addStyle(code);
            table.addCell(cell);
            cell= new Cell(1, 2).add(isNull(s.get("lxr")))
                    .addStyle(code);
            table.addCell(cell);
            cell= new Cell(1, 2).add(isNull(s.get("szd")))
                    .addStyle(code);
            table.addCell(cell);
            cell= new Cell(1, 2).add(isNull(s.get("hwlx")))
                    .addStyle(code);
            table.addCell(cell);
            cell= new Cell(1, 2).add(isNull(s.get("fwqx")))
                    .addStyle(code);
            table.addCell(cell);
            cell= new Cell(1, 4).add(isNull(s.get("bz")))
                    .addStyle(code);
            table.addCell(cell);
        }
        doc.add(table);

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        doc.add(new Paragraph("附件 5 仓库租赁协议")
                .setFontSize(16)
                .setFont(font));
        doc.add(new Paragraph("附件 6 境外营业执照（或经营许可证）")
                .setFontSize(16)
                .setFont(font));
        doc.add(new Paragraph("附件 7 报关单位注册登记证书")
                .setFontSize(16)
                .setFont(font));
        doc.add(new Paragraph("附件 8 境外公司股权证明")
                .setFontSize(16)
                .setFont(font));
        doc.add(new Paragraph("附件 9 境外公司投资协议")
                .setFontSize(16)
                .setFont(font));
        doc.add(new Paragraph("附件 10 清关资质证明材料")
                .setFontSize(16)
                .setFont(font));

        int n = pdfDoc.getNumberOfPages();
        Paragraph footer;
        for (int page = 1; page <= n; page++) {
            if(page == 1 || page == 2)  continue;
            footer = new Paragraph(String.format("第 %s 页 共 %s 页", page - 2, n - 2)).addStyle(code);
            doc.showTextAligned(footer, 297.5f, 20, page,
                    TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
        }

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

    public static void main(String[] args) throws IOException{
        PdfDocument pdf = new PdfDocument(new PdfWriter("E:\\workspace\\hzhwck\\target\\hzhwck\\pdfs\\说明.pdf"));
        Document document = new Document(pdf);
        //document.setTextAlignment(TextAlignment.JUSTIFIED);
        //.setHyphenation(new HyphenationConfig("en", "uk", 3, 3));
        PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H", false);
        Style code = new Style();
        code.setFont(font)
                .setFontSize(18)
                .setFontColor(Color.BLACK);

        /*BufferedReader br = new BufferedReader(new FileReader(SRC));
        String line;
        while((line = br.readLine()) != null) {
            //System.out.print(line + "\n");
            Text text = new Text(line + "\n");
            document.add(new Paragraph().add(text));
        }
        br.close();*/
        /*pdfPage page = pdf.addNewPage();
        PdfCanvas pdfCanvas = new PdfCanvas(page);
        Rectangle rectangle = new Rectangle(36, 650, 100, 100);
        pdfCanvas.rectangle(rectangle);
        pdfCanvas.stroke();
        Canvas canvas = new Canvas(pdfCanvas, pdf, rectangle);
*/

        //bold加粗 italic斜体
        Text title = new Text("请先提交数据！").setFont(font)
                .setFontSize(23)
                //.setBold()
                //.setItalic()
                .setStrokeColor(Color.BLACK)
                .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.FILL_STROKE);
        Text author = new Text("管理员").addStyle(code).setBackgroundColor(Color.LIGHT_GRAY);
        Paragraph p = new Paragraph().add(title);
        document.add(p);
        document.add(new Paragraph().add(new Tab()).add(new Tab()).add(new Tab()).add(new Tab()).add(new Tab()).add(" by ").add(author));
        document.close();
       // canvas.add(p);
        //pdf.close();
    }
}
