package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.ReportPlans;
import com.hzhwck.util.ResponseUtil;
import com.hzhwck.util.TimeUtil;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.JsonKit;

import java.util.Date;
import java.util.Map;


/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class ReportPlansController extends BaseController{

    @ActionKey("/api/hwc/reportplans")
    public void test(){
        boolean success = false;
        if(getRequest().getMethod().equals("POST")){
            System.out.println("[POST] add");
            forwardAction("/api/hwc/reportplans/add");
            success = true;
        }else if(getRequest().getMethod().equals("GET")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[GET] show id -- " + id);
                forwardAction("/api/hwc/reportplans/id/" + id);
                success = true;
            }else {
                System.out.println("[GET] show list");
                forwardAction("/api/hwc/reportplans/search");
                success = true;
            }
        }else if(getRequest().getMethod().equals("PUT")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[PUT] update id -- " + id);
                forwardAction("/api/hwc/reportplans/modify/" + id);
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

    /**
     * 添加报表计划
     * @Param name 计划名称
     * @Param startAt 开始时间
     * @Param endAt 结束时间
     * @Param groupId 表格分组id
     */
    @ActionKey("/api/hwc/reportplans/add")
    public void add(){
        Date startAt = getParaToDate("startAt");
        Date endAt = getParaToDate("endAt");
        Date round = getParaToDate("round");
        String groupId = getPara("tableGroupId");
        int status = getParaToInt("status");
        ReportPlans reportPlans = new ReportPlans().set("status", status)
                .set("startAt", startAt)
                .set("endAt", endAt)
                .set("tableGroupId", groupId)
                .set("createAt", TimeUtil.getNowTime())
                .set("round", round)
                .set("createBy", ((Map<String, Object>)getSessionAttr("user")).get("hwcUserId"));
        if(reportPlans.save() == false){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("01", "添加报表计划失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("01", "添加报表计划失败，数据库异常", null));
            }
            return ;
        }
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "添加报表计划成功", reportPlans));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "添加报表计划成功", reportPlans));
        }
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
        String id = getPara(0);
        Date startAt = getParaToDate("startAt");
        Date endAt = getParaToDate("endAt");
        Date round = getParaToDate("round");
        String status = getPara("status");
        ReportPlans reportPlans = ReportPlans.dao.findById(id);
        if(startAt != null) reportPlans.set("startAt", startAt);
        if(endAt != null)   reportPlans.set("endAt", endAt);
        if(round != null)   reportPlans.set("round", round);
        if(status != null)  reportPlans.set("status", status);
        reportPlans.set("modifyAt", TimeUtil.getNowTime());
        reportPlans.set("modifyBy", ((Map<String, Object>)getSessionAttr("user")).get("hwcUserId"));
        if(reportPlans.update() == false){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("01", "修改报表计划失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("01", "修改报表计划失败，数据库异常", null));
            }
            return ;
        }
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "修改报表计划成功", reportPlans));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "修改报表计划成功", reportPlans));
        }
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
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "获取分页报表计划成功",
                    ReportPlans.getPage(pageNumber, pageSize, oderBy, oder, filter)));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "获取分页报表计划成功",
                    ReportPlans.getPage(pageNumber, pageSize, oderBy, oder, filter)));
        }
    }

    /**
     * 根据id获取报表计划
     */
    @ActionKey("/api/hwc/reportplans/id")
    public void getReportplansById(){
        String id = getPara(0);
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "根据id获取报表计划成功", ReportPlans.dao.findById(id)));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "根据id获取报表计划成功", ReportPlans.dao.findById(id)));
        }
    }
}
