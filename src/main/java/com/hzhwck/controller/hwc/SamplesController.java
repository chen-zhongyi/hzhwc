package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.Samples;
import com.hzhwck.model.hwc.User;
import com.hzhwck.myEnum.HwcUserType;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.JsonKit;

import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/12.
 */
public class SamplesController extends BaseController {

    @ActionKey("/api/hwc/samples")
    public void test(){
        boolean success = false;
        if(getRequest().getMethod().equals("POST")){
            if(getPara(0) == null) {
                System.out.println("[POST] add");
                forwardAction("/api/hwc/samples/add");
                //redirect("/api/hwc/samples/add" + "?" + getParam());
                success = true;
            }
        }else if(getRequest().getMethod().equals("GET")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[GET] show id -- " + id);
                forwardAction("/api/hwc/samples/id/" + id);
                success = true;
            }else {
                System.out.println("[GET] show list");
                forwardAction("/api/hwc/samples/search");
                success = true;
            }
        }else if(getRequest().getMethod().equals("PUT")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[PUT] update id -- " + id);
                //forwardAction("/api/hwc/samples/modify/" + id);
                redirect("/api/hwc/samples/modify/" + id + "?" + getParam());
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
     * 添加样本企业信息
     */
    @ActionKey("/api/hwc/samples/add")
    public void add(){
        Samples samples = getModel(Samples.class, "sample");
        Map<String, Object> loginUser = getSessionAttr("user");
        User user = User.dao.findById(loginUser.get("hwcUserId"));
        samples.set("userId", loginUser.get("hwcUserId"));
        if(samples.save() == false){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("01", "添加海外仓样本企业失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("01", "添加海外仓样本企业失败，数据库异常", null));
            }
            return ;
        }
        user.set("sampleId", samples.get("id"));
        if(User.modify(user) == null){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("01", "添加海外仓样本企业失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("01", "添加海外仓样本企业失败，数据库异常", null));
            }
            samples.delete();
            return ;
        }
        loginUser.put("sample", samples);
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "添加海外仓样本企业成功", Samples.dao.findById(samples.get("id"))));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "添加海外仓样本企业成功", Samples.dao.findById(samples.get("id"))));
        }
    }

    /**
     * 修改样本企业信息
     */
    @ActionKey("/api/hwc/samples/modify")
    public void modify(){
        Map<String, Object> loginUser = getSessionAttr("user");
        Samples samples = getModel(Samples.class, "sample");
        samples.set("id", getPara(0));
        if(samples.update() == false){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("01", "添加海外仓样本企业失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("01", "添加海外仓样本企业失败，数据库异常", null));
            }
            return ;
        }
        Samples s = Samples.dao.findById(samples.get("id"));
        loginUser.put("sample", s);
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "修改海外仓样本企业成功", s));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "修改海外仓样本企业成功", s));
        }
    }

    /**
     * 分页查询样本企业信息
     * @Param pageNumber 第几页,默认值1
     * @Param pageSize 分页大小，默认值20
     * @Param orderBy 排序字段，默认id
     * @Param oder desc, asc
     * @Param filter 过滤信息，字符串【过滤字段:过滤信息,过滤字段:过滤信息..】,自动过滤不合法字段
     */
    @ActionKey("/api/hwc/samples/search")
    public void getPage(){
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);
        String oderBy = getPara("oderBy", "id");
        String oder = getPara("oder", "asc");
        String filter = "";
        Map<String, Object> loginUser = getSessionAttr("user");
        if(loginUser.get("type").toString().equals(HwcUserType.sample)){
            Samples sample = (Samples) loginUser.get("sample");
            String id = sample == null ? "-1" : sample.get("id").toString();
            if(filter.equals(""))   filter = " where id=" + id + " ";
            else
                filter += " and id=" + id + " " ;
        }else if(loginUser.get("type").toString().equals(HwcUserType.qxAdmin)){
            filter = " where ssqx = '" + loginUser.get("areaCode") + "' ";
        }else if(loginUser.get("type").toString().equals(HwcUserType.admin)){
            String ssqx = getPara("areaCode");
            if(ssqx != null){
                filter = " where ssqx='" + ssqx + "' ";
            }
        }

        String shtyxydm = getPara("shtyxydm");
        if(shtyxydm != null){
            if(filter.equals(""))   filter = " where shtyxydm like '%" + shtyxydm + "%'";
            else
                filter += " and shtyxydm like '%" + shtyxydm + "%'";
        }
        String jsdwmc = getPara("jsdwmc");
        if(jsdwmc != null){
            if(filter.equals(""))   filter = " where jsdwmc like '%" + jsdwmc + "%'";
            else
                filter += " and jsdwmc like '%" + jsdwmc + "%'";
        }

        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "获取海外仓企业信息成功",
                    Samples.getPage(pageNumber, pageSize, oderBy, oder, filter)));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "获取海外仓企业信息成功",
                    Samples.getPage(pageNumber, pageSize, oderBy, oder, filter)));
        }
    }

    /**
     * 根据id查询样本企业
     * @Param id
     */
    @ActionKey("/api/hwc/samples/id")
    public void getSampleById(){
        String id = getPara(0);
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "获取样本企业信息成功", Samples.dao.findById(id)));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "获取样本企业信息成功", Samples.dao.findById(id)));
        }
    }
}
