package com.hzhwck.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Created by 陈忠意 on 2017/6/2.
 */
public class UserValidator extends Validator {
    protected void validate(Controller c){
        validateInteger("id", "idMsg", "请输入数字");
    }

    protected void handleError(Controller c){
        c.renderJson();
    }
}
