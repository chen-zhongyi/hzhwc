package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.*;
import com.hzhwck.util.ResponseUtil;
import com.hzhwck.util.TimeUtil;
import com.jfinal.core.ActionKey;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class ReportPlansController extends BaseController{

    /**
     * 添加报表计划
     * @Param name 计划名称
     * @Param startAt 开始时间
     * @Param endAt 结束时间
     * @Param groupId 表格分组id
     */
    @ActionKey("/api/hwc/reportplans/add")
    public void add(){
        Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                String name = getPara("name");
                Date startAt = getParaToDate("startAt");
                Date endAt = getParaToDate("endAt");
                String groupId = getPara("groupId");
                ReportPlans reportPlans = new ReportPlans().set("name", name)
                        .set("startAt", startAt)
                        .set("endAt", endAt)
                        .set("groupId", groupId)
                        .set("createAt", TimeUtil.getNowTime())
                        .set("createBy", ((Map<String, Object>)getSessionAttr("user")).get("hwcUserId"));
                if(reportPlans.save() == false){
                    renderJson(ResponseUtil.setRes("01", "添加报表计划失败，数据库异常", null));
                    return false;
                }
                List<User> users = User.getAllUser();
                List<Tables> tables = Tables.getTablesByGroupId(groupId);
                for(Tables table : tables){
                    for(User user : users){
                        for(String warehouseId : user.getStr("warehouseIds").split(",")) {
                            Reports report = new Reports().set("planId", reportPlans.get("id"))
                                    .set("sampleId", user.get("sampleId"))
                                    .set("warehouseId", warehouseId)
                                    .set("tableId", table.get("id"))
                                    .set("createAt", TimeUtil.getNowTime())
                                    .set("createBy", ((Map<String, Object>)getSessionAttr("user")).get("hwcUserId"));
                            if(report.save() == false){
                                renderJson(ResponseUtil.setRes("01", "添加报表计划失败，数据库异常", null));
                                return false;
                            }
                        }
                    }
                }
                renderJson(ResponseUtil.setRes("00", "添加报表计划成功", reportPlans));
                return true;
            }
        });
    }

    /**
     * 修改报表计划
     * @Param name 计划名称
     * @Param startAt 开始时间
     * @Param endAt 结束时间
     * @Param status 状态
     */
    @ActionKey("/api/hwc/reportplans/modify")
    public void modify(){
        String name = getPara("name");
        Date startAt = getParaToDate("startAt");
        Date endAt = getParaToDate("endAt");
        String status = getPara("status");
        ReportPlans reportPlans = ReportPlans.getReportPlanByName(name);
        reportPlans.set("startAt", startAt)
                .set("endAt", endAt)
                .set("status", status);
        if(reportPlans.save() == false){
            renderJson(ResponseUtil.setRes("01", "修改报表计划失败，数据库异常", null));
            return ;
        }
        renderJson(ResponseUtil.setRes("00", "修改报表计划成功", reportPlans));
    }

    /**
     * 获取分页报表计划
     * @Param pageNumber 第几页,默认值1
     * @Param pageSize 分页大小，默认值20
     * @Param orderBy 排序字段，默认id
     * @Param oder desc, asc
     * @Param filter 过滤信息，字符串【过滤字段:过滤信息,过滤字段:过滤信息..】,自动过滤不合法字段
     */
    @ActionKey("/api/hwc/reportplans/search")
    public void getPage(){
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);
        String oderBy = getPara("oderBy", "id");
        String oder = getPara("oder", "asc");
        String filter = getPara("filter", "");
        renderJson(ResponseUtil.setRes("00", "获取分页报表计划成功",
                ReportPlans.getPage(pageNumber, pageSize, oderBy, oder, filter)));
    }
}
