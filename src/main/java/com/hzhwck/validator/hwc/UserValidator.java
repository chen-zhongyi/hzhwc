package com.hzhwck.validator.hwc;


import com.hzhwck.model.hwc.Area;
import com.hzhwck.model.hwc.Yuanqu;
import com.hzhwck.model.system.Account;
import com.hzhwck.myEnum.CodeType;
import com.hzhwck.myEnum.HwcUserType;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/5.
 */
public class UserValidator extends Validator {

    protected void validate(Controller c){
        if(getActionKey().equals("/api/hwc/users/register")){
            validateString("userName", 4, 20, "userName", "用户名长度在4-20个字符之间");
            if(c.getPara("userName") != null){
                if(Account.findByUserName(c.getPara("userName")) != null){
                    addError("userName", "用户名已存在");
                }
            }
            validateString("pwd", 6, 20, "pwd", "密码长度在6-20字符之间");
            validateString("realName", 2, 10, "realName", "姓名长度在2-10个字符之间");
            String type = c.getPara("type");
            System.out.println("type = " + type);
            if(type.equals(HwcUserType.qxAdmin)) {
                if (c.getPara("areaCode") == null) {
                    addError("areaCode", "区县所属代码必须填写");
                } else {
                    //验证areaCode在数据库中是否存在
                    if (Area.findByAreaCode(c.getPara("areaCode")) == null) {
                        addError("areaCode", "区县所属代码不存在");
                    }
                }
            }else if(type.equals(HwcUserType.yqadmin)){
                if (c.getPara("yqCode") == null) {
                    addError("yqCode", "区县所属代码必须填写");
                } else {
                    //验证areaCode在数据库中是否存在
                    if (Yuanqu.getYuanquByCode(c.getPara("yqCode")) == null) {
                        addError("yqCode", "园区所属代码不存在");
                    }
                }
            }
        }else if(getActionKey().equals("/api/hwc/users/modify")){
            String accountId = c.getPara(0);
            if(accountId != null){
                if(Account.dao.findById(accountId) == null)    addError("id", "用户名不存在");
            }else
                addError("id","id不能为空");
            if(c.getPara("status") != null){
                validateInteger("status", 0, 1, "status", "用户状态码出错");
            }
            if(c.getPara("realName") != null){
                validateString("realName", 2, 20, "realName", "姓名长度在2-10个字符之间");
            }
            if(c.getPara("areaCode") != null){
                //验证areaCode在数据库中是否存在
                if(Area.findByAreaCode(c.getPara("areaCode")) == null){
                    addError("areaCode", "区县所属代码不存在");
                }
            }
        }else if(getActionKey().equals("/api/hwc/users/delete")){
            String userName = c.getPara(0);
            if(userName != null){
                if(Account.findByUserName(userName) == null)    addError("userName", "用户名不存在");
            }else
                addError("uerName","用户名不能为空");
        }else if(getActionKey().equals("/api/hwc/users/login")){
            String userName = c.getPara("userName");
            if(userName != null){
                if(Account.findByUserName(userName) == null)    addError("userName", "用户名不存在");
            }else
                addError("uerName","用户名不能为空");
            String pwd = c.getPara("pwd");
            if(pwd == null)
                addError("pwd","密码不能为空");
        }else if(getActionKey().equals("/api/hwc/users/logout")){
            if(!((Map<String, Object>)c.getSessionAttr("user")).get("userName").equals(c.getPara("userName"))){
                addError("userName", "用户名出错");
            }
        }else if(getActionKey().equals("/api/hwc/users/search")){
            if(c.getPara("pageNumber") != null)
                validateInteger("pageNumber", "pageNumber", "第几页必须为数字");
            if(c.getPara("pageSize") != null)
                validateInteger("pageSize", "pageSize", "分页大小必须为数字");
            if(c.getPara("oder") != null ){
                if(!c.getPara("oder").equals("desc") && !c.getPara("oder").equals("asc")){
                    addError("oder", "为desc或asc");
                }
            }
        }
    }

    protected void handleError(Controller c){
        Map<String, Object> data = new HashMap<String, Object>();
        Enumeration<String> enumeration = c.getAttrNames();
        String msg = "";
        while(enumeration.hasMoreElements()){
            String temp = enumeration.nextElement();
            data.put(temp, c.getAttr(temp));
            msg += c.getAttr(temp) + " ";
        }
        c.renderJson(ResponseUtil.setRes(CodeType.paramError, "参数出错, " + msg, data));
    }
}
