package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.interceptor.LoginInterceptor;
import com.hzhwck.model.hwc.Area;
import com.hzhwck.model.hwc.Yuanqu;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.JsonKit;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/17.
 */
@Clear(LoginInterceptor.class)
public class AreaController extends BaseController{

    @ActionKey("/api/hwc/areas")
    public void test(){
        boolean success = false;
        if(getRequest().getMethod().equals("POST")){
            System.out.println("[POST] add");
            forwardAction("/api/hwc/areas/register");
            success = true;
        }else if(getRequest().getMethod().equals("GET")){
            String userName = getPara(0);
            if(userName != null){
                System.out.println("[GET] show userName -- " + userName);
                //success = true;
            }else {
                System.out.println("[GET] show list");
                forwardAction("/api/hwc/areas/search");
                success = true;
            }
        }else if(getRequest().getMethod().equals("PUT")){
            String userName = getPara(0);
            if(userName != null){
                System.out.println("[PUT] update userName -- " + userName);
                forwardAction("/api/hwc/areas/modify/" + userName);
                success = true;
            }
        }else if(getRequest().getMethod().equals("DELETE")){
            String userName = getPara(0);
            if(userName != null){
                System.out.println("[DELETE] userName -- " + userName);
                forwardAction("/api/hwc/areas/delete/" + userName);
                success = true;
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
     * 获取海外仓区域
     */
    @ActionKey("/api/hwc/areas/search")
    public void getArea(){
        List<Area> data = Area.getAreas();
        for(Area area : data){
            area.put("yuanqu", Yuanqu.getYuanquByAreaCode(area.getStr("code")));
        }
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "获取海外仓区域", data));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "获取海外仓区域", data));
        }
    }
}
