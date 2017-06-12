package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.Message;
import com.hzhwck.model.hwc.Samples;
import com.hzhwck.util.ResponseUtil;
import com.hzhwck.util.TimeUtil;
import com.jfinal.core.ActionKey;

import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/12.
 */
public class MessageController extends BaseController {

    /**
     * 发送站内信息
     * @Param title 标题
     * @Param content 内容
     * @Param sampleIds 样本企业ids
     */
    @ActionKey("/api/hwc/messages/send")
    public void send(){
        String title = getPara("title");
        String content = getPara("content");
        String[] sampleIds = getParaValues("sampleIds");
        Map<String, Object> loginUser = getSessionAttr("user");
        boolean success = true;
        StringBuffer error = new StringBuffer("以下用户发送失败：");
        for(String sampleId : sampleIds){
            Message message = new Message();
            message.set("title", title);
            message.set("content", content);
            message.set("status", 0);
            message.set("sendBy", loginUser.get("hwcUserId"));
            message.set("sendAt", TimeUtil.getNowTime());
            Samples samples = Samples.dao.findById(sampleId);
            message.set("getBy", samples.get("userId"));
            if(message.save() == false){
                success = false;
                error.append(sampleId + " ");
            }
        }
        if(success){
            renderJson(ResponseUtil.setRes("00", "发送站内信息成功", null));
        }else {
            renderJson(ResponseUtil.setRes("01", "发送站内信息失败，数据库异常，请联系管理员", error.toString()));
        }
    }

    /**
     * 查看站内信息
     * @Param id
     */
    @ActionKey("/api/hwc/messages/view")
    public void view(){
        String id = getPara("id");
        Message message = Message.dao.findById(id);
        message.set("getAt", TimeUtil.getNowTime());
        message.set("status", 1);
        if(message.update() == false){
            renderJson(ResponseUtil.setRes("01", "查看站内信息失败，数据库异常，请联系管理员", null));
            return ;
        }
        renderJson(ResponseUtil.setRes("00", "查看站内信息成功", null));
    }

    /**
     * 分页查询站内信息
     * @Param pageNumber 第几页,默认值1
     * @Param pageSize 分页大小，默认值20
     * @Param orderBy 排序字段，默认id
     * @Param oder desc, asc
     * @Param filter 过滤信息，字符串【过滤字段:过滤信息,过滤字段:过滤信息..】,自动过滤不合法字段
     */
    @ActionKey("/api/hwc/messages/search")
    public void getPage(){
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);
        String oderBy = getPara("oderBy", "id");
        String oder = getPara("oder", "asc");
        String filter = getPara("filter", "");
        renderJson(ResponseUtil.setRes("00", "获取站内信息信息成功",
                Message.getPage(pageNumber, pageSize, oderBy, oder, filter)));
    }

    /**
     * 获取已发信息
     * @Param pageNumber 第几页,默认值1
     * @Param pageSize 分页大小，默认值20
     * @Param filter 过滤信息，字符串【过滤字段:过滤信息,过滤字段:过滤信息..】,自动过滤不合法字段
     */
    @ActionKey("/api/hwc/messages/sended")
    public void getSended(){
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);
        String filter = getPara("filter", "");
        Map<String, Object> loginUser = getSessionAttr("user");
        if(filter.equals(""))
            filter = "sendBy:" + loginUser.get("hwcUserId");
        else
            filter += ",sendBy:" + loginUser.get("hwcUserId");
        renderJson(ResponseUtil.setRes("00", "获取站内信息信息成功",
                Message.getPage(pageNumber, pageSize, "id", "asc", filter)));
    }

    /**
     * 获取已发信息
     * @Param pageNumber 第几页,默认值1
     * @Param pageSize 分页大小，默认值20
     * @Param filter 过滤信息，字符串【过滤字段:过滤信息,过滤字段:过滤信息..】,自动过滤不合法字段
     */
    @ActionKey("/api/hwc/messages/get")
    public void getMessages(){
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);
        String filter = getPara("filter", "");
        Map<String, Object> loginUser = getSessionAttr("user");
        if(filter.equals(""))
            filter = "getBy:" + loginUser.get("hwcUserId");
        else
            filter += ",getBy:" + loginUser.get("hwcUserId");
        renderJson(ResponseUtil.setRes("00", "获取站内信息信息成功",
                Message.getPage(pageNumber, pageSize, "id", "asc", filter)));
    }
}
