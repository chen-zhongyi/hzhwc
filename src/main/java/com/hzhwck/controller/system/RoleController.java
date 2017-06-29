package com.hzhwck.controller.system;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.system.Role;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.kit.JsonKit;

/**
 * Created by 陈忠意 on 2017/6/8.
 */
public class RoleController extends BaseController{

    @ActionKey("/api/system/roles")
    public void test(){
        boolean success = false;
        if(getRequest().getMethod().equals("POST")){
            System.out.println("[POST] add");
            //forwardAction("/api/system/roles/add");
            // success = true;
        }else if(getRequest().getMethod().equals("GET")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[GET] show id -- " + id);
                //forwardAction("/api/system/menus/id/" + id);
                //success = true;
            }else {
                System.out.println("[GET] show list");
                forwardAction("/api/system/roles/search");
                success = true;
            }
        }else if(getRequest().getMethod().equals("PUT")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[PUT] update id -- " + id);
                //forwardAction("/api/system/roles/modify/" + id);
                //success = true;
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
     * 获取系统所有角色
     */
    @Before(GET.class)
    @ActionKey("/api/system/roles/search")
    public void getRoles(){
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "获取系统角色成功", Role.getRoles()));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "获取系统角色成功", Role.getRoles()));
        }
    }

    /**
     * 根据id获取系统角色
     */
    @ActionKey("/api/system/roles/id")
    public void getRoleById(){
        String id = getPara(0);
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "获取系统角色成功", Role.dao.findById(id)));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "获取系统角色成功", Role.dao.findById(id)));
        }
    }

}
