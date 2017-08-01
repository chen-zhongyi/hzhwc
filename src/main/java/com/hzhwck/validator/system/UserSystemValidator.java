package com.hzhwck.validator.system;

import com.hzhwck.model.system.Account;
import com.hzhwck.myEnum.CodeType;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/7/3.
 */
public class UserSystemValidator extends Validator{

    protected void validate(Controller c){
        //validateRequiredString("userId", "userId", "用户名必须");
        String userId = c.getPara("0");
        if(userId != null) {
            if (Account.dao.findById(userId) == null) {
                addError("userId", "用户不存在");
            }
        }
    }

    protected void handleError(Controller c){
        Map<String, Object> data = new HashMap<String, Object>();
        Enumeration<String> enumeration = c.getAttrNames();
        while(enumeration.hasMoreElements()){
            String temp = enumeration.nextElement();
            data.put(temp, c.getAttr(temp));
        }
        c.renderJson(ResponseUtil.setRes(CodeType.paramError, "参数出错", data));
    }
}
