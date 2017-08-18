package com.hzhwck.controller.system;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.Systems;
import com.hzhwck.model.system.UserSystem;
import com.hzhwck.myEnum.CodeType;
import com.hzhwck.util.ResponseUtil;
import com.hzhwck.validator.system.UserSystemValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.JsonKit;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/7/2.
 */
@Before(UserSystemValidator.class)
public class UserSystemController extends BaseController{

    @ActionKey("/api/system/usersystems")
    public void test(){
        boolean success = false;
        if(getRequest().getMethod().equals("POST")){
            System.out.println("[POST] add");
            forwardAction("/api/system/usersystems/add");
            success = true;
        }else if(getRequest().getMethod().equals("GET")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[GET] show id -- " + id);
                forwardAction("/api/system/usersystems/id/" + id);
                success = true;
            }else {
                System.out.println("[GET] show list");
                //forwardAction("/api/system/usersystems/search");
                //success = true;
            }
        }else if(getRequest().getMethod().equals("PUT")){
            //String id = getPara(0);
            //if(id != null){
                System.out.println("[PUT] update id -- ");
                redirect("/api/system/usersystems/modify/" + getPara(0) + "?" + getParam());
                success = true;
            //}
        }else if(getRequest().getMethod().equals("DELETE")){
            String userName = getPara(0);
            if(userName != null){
                System.out.println("[DELETE] userName -- " + userName);
                //forwardAction("/api/system/usersystems/delete/" + userName);
                //success = true;
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
     * 添加管理员权限
     * @Param userId 用户id
     * @Param system 权限
     */
    @ActionKey("/api/system/usersystems/add")
    public void add(){
        String accountId = getPara("userId");
        String systems = getPara("system");
        List<Systems> s = Systems.findAll();
        boolean success = true;
        for(Systems temp : s){
            UserSystem us = new UserSystem()
                    .set("system", temp.get("code"))
                    .set("accountId", accountId);
            if(us.save() == false)  {
                success = false;
                break;
            }
        }
        for(String sys : systems.split(",")){
            String[] temp = sys.split(":");
            if(temp.length == 1)    continue;
            if(!(temp[1].trim().equals("edit") || temp[1].trim().equals("view")))   continue;
            UserSystem us = UserSystem.getBySystemAndAccountId(temp[0].trim(), accountId);
            if(us == null)  continue;
            us.set("right", temp[1].trim());
            if(us.update() == false){
                success = false;
                break;
            }
        }
        if(success == false){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加管理员权限失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加管理员权限失败，数据库异常", null));
            }
        }else {
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "添加管理员权限成功",  UserSystem.getByAccountId(accountId)));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.success, "添加管理员权限成功",  UserSystem.getByAccountId(accountId)));
            }
        }
    }

    /**
     * 修改管理员权限
     * @Param userId 用户id
     * @Param system 权限
     */
    public void modify(){
        //String accountId = getPara("userId");
        String accountId = getPara(0);
        String systems = getPara("system");
        boolean success = true;
        for(String sys : systems.split(",")){
            String[] temp = sys.split(":");
            UserSystem us = UserSystem.getBySystemAndAccountId(temp[0].trim(), accountId);
            if(us == null)  {
                if(Systems.getSystemsByCode(temp[0].trim()) == null || temp.length == 1) continue;
                UserSystem userSystem = new UserSystem()
                        .set("system", temp[0].trim())
                        .set("accountId", accountId)
                        .set("right", temp[1].trim());
                userSystem.save();
                continue;
            }
            if(temp.length == 1)
                us.set("right", null);
            else {
                if (!(temp[1].trim().equals("edit") || temp[1].trim().equals("view"))) continue;
                us.set("right", temp[1].trim());
            }
            if(us.update() == false){
                success = false;
                break;
            }
        }
        if(success == false){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "修改管理员权限失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "修改管理员权限失败，数据库异常", null));
            }
        }else {
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "修改管理员权限成功",  UserSystem.getByAccountId(accountId)));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.success, "修改管理员权限成功",  UserSystem.getByAccountId(accountId)));
            }
        }
    }

    /**
     * 根据userId获取用户权限
     */
    @ActionKey("/api/system/usersystems/search")
    public void getUserSystemByAccoundId(){
        String accountId = getPara("userId");
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "根据userId获取用户权限成功", UserSystem.getByAccountId(accountId)));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "根据userId获取用户权限成功",  UserSystem.getByAccountId(accountId)));
        }
    }
}
