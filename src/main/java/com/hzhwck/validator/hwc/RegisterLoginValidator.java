package com.hzhwck.validator.hwc;

import com.hzhwck.model.system.Account;
import com.hzhwck.myEnum.CodeType;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/7/2.
 */
public class RegisterLoginValidator extends Validator{

    protected void validate(Controller c){
        if(getActionKey().equals("/api/hwc/register")){
            validateString("userName", 4, 20, "userName", "用户名长度在4-20个字符之间");
            if(c.getPara("userName") != null){
                if(Account.findByUserName(c.getPara("userName")) != null){
                    addError("userName", "用户名已存在");
                }
            }
            validateString("pwd", 6, 20, "pwd", "密码长度在6-20字符之间");
            validateString("realName", 2, 10, "realName", "姓名长度在2-10个字符之间");
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
