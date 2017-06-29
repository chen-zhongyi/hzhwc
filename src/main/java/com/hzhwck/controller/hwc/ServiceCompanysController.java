package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.Samples;
import com.hzhwck.model.hwc.ServiceCompanys;
import com.hzhwck.model.hwc.User;
import com.hzhwck.model.hwc.Warehouses;
import com.hzhwck.model.system.Account;
import com.hzhwck.myEnum.HwcUserType;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;

import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/16.
 */
public class ServiceCompanysController extends BaseController {

    @ActionKey("/api/hwc/servicecompanys")
    public void test(){
        boolean success = false;
        if(getRequest().getMethod().equals("POST")){
            System.out.println("[POST] add");
            forwardAction("/api/hwc/servicecompanys/register");
            success = true;
        }else if(getRequest().getMethod().equals("GET")){
            String userName = getPara(0);
            if(userName != null){
                System.out.println("[GET] show userName -- " + userName);
                success = true;
            }else {
                System.out.println("[GET] show list");
                forwardAction("/api/hwc/servicecompanys/search");
                success = true;
            }
        }else if(getRequest().getMethod().equals("PUT")){
            String userName = getPara(0);
            if(userName != null){
                System.out.println("[PUT] update userName -- " + userName);
                forwardAction("/api/hwc/servicecompanys/modify/" + userName);
                success = true;
            }
        }else if(getRequest().getMethod().equals("DELETE")){
            String userName = getPara(0);
            if(userName != null){
                System.out.println("[DELETE] userName -- " + userName);
                //forwardAction("/api/hwc/servicecompanys/delete/" + userName);
                //success = true;
            }
        }
        if(success == false){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("02", "url出错或者请求方法出错", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("02", "url出错或者请求方法出错", null));
            }
        }
    }

    /**
     * 添加服务企业
     */
    @ActionKey("/api/hwc/servicecompanys/add")
    public void add(){
        ServiceCompanys sc = getModel(ServiceCompanys.class, "servicecompany");
        if(sc.save() == false){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("01", "添加服务企业失败，数据库异常，请联系管理员", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("01", "添加服务企业失败，数据库异常，请联系管理员", null));
            }
            return ;
        }
        String warehouseId = sc.getStr("warehouseId");
        Warehouses w = Warehouses.dao.findById(warehouseId);
        String ids = w.getStr("serviceCompanyIds");
        w.set("serviceCompanyIds", ids == null ? sc.get("id") : ids + "," + sc.get("id"));
        if(w.update() == false){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("01", "添加服务企业失败，数据库异常，请联系管理员", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("01", "添加服务企业失败，数据库异常，请联系管理员", null));
            }
            sc.delete();
            return ;
        }
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "添加服务企业成功", ServiceCompanys.dao.findById(sc.get("id"))));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes("00", "添加服务企业成功", ServiceCompanys.dao.findById(sc.get("id"))));
        }
    }

    /**
     * 修改服务企业
     */
    @ActionKey("/api/hwc/servicecompanys/modify")
    public void modify(){
        String id = getPara(0);
        ServiceCompanys sc = getModel(ServiceCompanys.class, "servicecompany");
        sc.set("id", id);
        if(sc.update() == false){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("01", "修改服务企业失败，数据库异常，请联系管理员", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("01", "修改服务企业失败，数据库异常，请联系管理员", null));
            }
            return ;
        }
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "修改服务企业成功", ServiceCompanys.dao.findById(sc.get("id"))));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes("00", "修改服务企业成功", ServiceCompanys.dao.findById(sc.get("id"))));
        }
    }

    /**
     * 根据id查询海外仓服务企业信息
     */
    @ActionKey("/api/hwc/servicecompanys/id")
    public void getServiceCompanyById(){
        String id = getPara(0);
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "根据id查询海外仓服务企业信息成功", ServiceCompanys.dao.findById(id)));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes("00", "根据id查询海外仓服务企业信息成功", ServiceCompanys.dao.findById(id)));
        }
    }

    /**
     * 根据用户id，样本企业id，海外仓id查询海外仓服务企业
     */
    @ActionKey("/api/hwc/servicecompanys/search")
    public void search(){
        String userId = "";
        String accountId = getPara("userId");
        Map<String, Object> loginUser = getSessionAttr("user");
        if(loginUser.get("type").toString().equals(HwcUserType.sample)){
            accountId = loginUser.get("accountId").toString();
        }
        if(accountId != null){
            Account account = Account.dao.findById(accountId);
            userId = account.getStr("user").split(":")[1];
        }
        String sampleId = getPara("sampleId");
        if(userId.equals("") && sampleId != null){
            Samples sample = Samples.dao.findById(sampleId);
            userId = sample.getStr("userId");
        }
        String ids = "";
        if(!userId.equals("")){
            String warehouseIds = User.dao.findById(userId).getStr("warehouseIds");
            if(warehouseIds != null){
                for(String warehouseId : warehouseIds.split(",")) {
                    Warehouses warehouse = Warehouses.dao.findById(warehouseId);
                    String sIds = warehouse.getStr("serviceCompanyIds");
                    if(sIds != null){
                        if (ids.equals("")) ids = sIds;
                        else
                            ids += "," + sIds;
                    }
                }
            }
        }
        String warehouseId = getPara("warehouseId");
        if(ids.equals("") && warehouseId != null){
            Warehouses warehouse = Warehouses.dao.findById(warehouseId);
            String sIds = warehouse.getStr("serviceCompanyIds");
            if(sIds != null){
                if (ids.equals("")) ids = sIds;
                else
                    ids += "," + sIds;
            }
        }
        if(ids.equals(""))  ids = "-1";
        Page<ServiceCompanys> s = ServiceCompanys.dao.paginate(getParaToInt("pageNumber", 1),
                getParaToInt("pageSize", 20),
                "select * ", "from hwc_servicecompanys where id in (" + ids + ")");
    }
}
