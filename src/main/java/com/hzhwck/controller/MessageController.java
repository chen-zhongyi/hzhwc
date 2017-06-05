package com.hzhwck.controller;

import com.hzhwck.model.Message;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/5/17.
 */
public class MessageController extends Controller {
    private Map<String, Object> res = new HashMap<String, Object>();

    @ActionKey("/messages")
    public void getMessages(){
        res.put("code", "00");
        res.put("msg", "获取站内信息列表成功");
        res.put("data", Message.getMessagesByFuzzyQuery(getPara("title"), getPara("content"), getPara("count")));
        renderJson(res);
    }

    @ActionKey("/messages/paginate")
    public void getPaginateMessages(){
        res.put("code", "00");
        res.put("msg", "获取站内信息分页列表成功");
        res.put("data", Message.getPaginateMessages(getParaToInt("pageNumber", 1),
                                                    getParaToInt("pageSize", 20),
                                                    getPara("title"),
                                                    getPara("content")));
        renderJson(res);
    }

    @ActionKey("/messages/add")
    public void addMessage(){
        Message message = new Message();
        message.set("title", isNull(getPara("title")));
        message.set("content", isNull(getPara("content")));
        SimpleDateFormat sdp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        message.set("createAt", sdp.format(new Date(System.currentTimeMillis())));
        //int createBy = ((Record)getSessionAttr("user")).get("id");
        message = Message.save(message);
        if(message == null) {
            res.put("code", "01");
            res.put("msg", "站内信息创建失败");
            res.put("data", null);
            renderJson(res);
        }else {
            res.put("code", "00");
            res.put("msg", "站内信息创建成功");
            res.put("data", message);
            renderJson(res);
        }
    }

    @ActionKey("/messages/update")
    public void updateMessage(){
        Message message = new Message();
        if(getPara("id") == null)   {
            renderJson("null");
            return ;
        }
        message.set("id", getPara("id"));
        if(getPara("title") != null) message.set("title", getPara("title"));
        if(getPara("content") != null) message.set("content", getPara("content"));
        SimpleDateFormat sdp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        message.set("updateAt", sdp.format(new Date(System.currentTimeMillis())));
        //int updateBy = ((Record)getSessionAttr("user")).get("id");
        message = Message.update(message);
        if(message == null) {
            res.put("code", "01");
            res.put("msg", "站内信息更新失败");
            res.put("data", null);
            renderJson(res);
        }else {
            res.put("code", "00");
            res.put("msg", "站内信息更新成功");
            res.put("data", message);
            renderJson(res);
        }
    }

    @ActionKey("/messages/delete")
    public void deleteMessage(){
        String[] ids = getParaValues("ids");
        if(ids == null) {
            res.put("code", "00");
            res.put("msg", "站内信息删除成功");
            res.put("data", null);
            renderJson(res);
            return ;
        }
        List<String> failed = new LinkedList<String>();
        for(String id : ids){
            if(Message.delete(id) == false) failed.add(id);
        }
        if(failed.size() == 0){
            res.put("code", "00");
            res.put("msg", "站内信息删除成功");
            res.put("data", null);
            renderJson(res);
        }else {
            res.put("code", "01");
            res.put("msg", "站内信息部分删除失败，返回失败ids");
            res.put("data", failed);
            renderJson(res);
        }
    }

    private String isNull(Object str){
        if(str == null )    return "";
        return str.toString();
    }
}
