package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.Samples;
import com.hzhwck.model.hwc.ServiceCompanys;
import com.hzhwck.model.hwc.User;
import com.hzhwck.model.hwc.Warehouses;
import com.hzhwck.model.system.Account;
import com.hzhwck.myEnum.CodeType;
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
            //forwardAction("/api/hwc/servicecompanys/add");
            redirect("/api/hwc/servicecompanys/add" + "?" + getParam());
            success = true;
        }else if(getRequest().getMethod().equals("GET")){
            String userName = getPara(0);
            if(userName != null){
                System.out.println("[GET] show userName -- " + userName);
                forwardAction("/api/hwc/servicecompanys/id/" + userName);
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
                //forwardAction("/api/hwc/servicecompanys/modify/" + userName);
                redirect("/api/hwc/servicecompanys/modify/" + userName + "?" + getParam());
                success = true;
            }
        }else if(getRequest().getMethod().equals("DELETE")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[DELETE] id -- " + id);
                forwardAction("/api/hwc/servicecompanys/delete/" + id);
                success = true;
            }
        }
        if(success == false){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.paramError, "url出错或者请求方法出错", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.paramError, "url出错或者请求方法出错", null));
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
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加服务企业失败，数据库异常，请联系管理员", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加服务企业失败，数据库异常，请联系管理员", null));
            }
            return ;
        }
        String warehouseId = sc.get("warehouseId").toString();
        Warehouses w = Warehouses.dao.findById(warehouseId);
        String ids = w.getStr("serviceCompanyIds");
        w.set("serviceCompanyIds", ids == null ? sc.get("id") : ids + "," + sc.get("id"));
        if(w.update() == false){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加服务企业失败，数据库异常，请联系管理员", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加服务企业失败，数据库异常，请联系管理员", null));
            }
            sc.delete();
            return ;
        }
        ServiceCompanys serviceCompany = ServiceCompanys.dao.findById(sc.get("id"));
        serviceCompany.put("warehouse", w);
        serviceCompany.put("sample", Samples.dao.findById(w.get("sampleId")));
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "添加服务企业成功", serviceCompany));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "添加服务企业成功", serviceCompany));
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
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "修改服务企业失败，数据库异常，请联系管理员", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "修改服务企业失败，数据库异常，请联系管理员", null));
            }
            return ;
        }
        ServiceCompanys serviceCompany = ServiceCompanys.dao.findById(sc.get("id"));
        Warehouses w = Warehouses.dao.findById(serviceCompany.get("warehouseId"));
        serviceCompany.put("warehouse", w);
        serviceCompany.put("sample", Samples.dao.findById(w.get("sampleId")));
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "修改服务企业成功", serviceCompany));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "修改服务企业成功", serviceCompany));
        }
    }

    /**
     * 根据id查询海外仓服务企业信息
     */
    @ActionKey("/api/hwc/servicecompanys/id")
    public void getServiceCompanyById(){
        String id = getPara(0);
        ServiceCompanys serviceCompany = ServiceCompanys.dao.findById(id);
        Warehouses w = Warehouses.dao.findById(serviceCompany.get("warehouseId"));
        serviceCompany.put("warehouse", w);
        serviceCompany.put("sample", Samples.dao.findById(w.get("sampleId")));
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "根据id查询海外仓服务企业信息成功", serviceCompany));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "根据id查询海外仓服务企业信息成功", serviceCompany));
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
        if(warehouseId != null){
            Warehouses warehouse = Warehouses.dao.findById(warehouseId);
            String sIds = warehouse.getStr("serviceCompanyIds");
            if(sIds != null){
                ids = sIds;
            }else {
                ids = "-1";
            }
        }
        if(ids.equals(""))  ids = "-1";
        Page<ServiceCompanys> s = ServiceCompanys.dao.paginate(getParaToInt("pageNumber", 1),
                getParaToInt("pageSize", 20),
                "select * ", "from hwc_servicecompanys where id in (" + ids + ")");
        for(ServiceCompanys serviceCompany : s.getList()){
            Warehouses w = Warehouses.dao.findById(serviceCompany.get("warehouseId"));
            serviceCompany.put("warehouse", w);
            serviceCompany.put("sample", Samples.dao.findById(w.get("sampleId")));
        }
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "海外仓服务企业信息成功", s));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "海外仓服务企业信息成功", s));
        }
    }

    /**
     * 删除海外仓服务企业
     */
    @ActionKey("/api/hwc/servicecompanys/delete")
    public void delete(){
        String id = getPara(0);
        ServiceCompanys serviceCompany = ServiceCompanys.dao.findById(id);
        Warehouses warehouse = Warehouses.dao.findById(serviceCompany.get("warehouseId"));
        if(ServiceCompanys.dao.deleteById(id) == false){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "删除海外仓服务企业失败，数据库异常，请联系管理员", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "删除海外仓服务企业失败，数据库异常，请联系管理员", null));
            }
            return ;
        }
        String[] ids = warehouse.getStr("serviceCompanyIds").split(",");
        String res = "";
        for(int i =  0, j = 0;i < ids.length;++i){
            if(ids[i].equals(id))   continue;
            res += ids[i];
            ++j;
            if(j != ids.length - 1) res += ",";
        }
        warehouse.set("serviceCompanyIds", res.equals("") ? null : res);
        warehouse.update();
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "删除海外仓服务企业成功", null));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "删除海外仓服务企业成功", null));
        }
    }
}
