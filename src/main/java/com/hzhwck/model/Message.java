package com.hzhwck.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/5/17.
 */
public class Message extends Model<Message> {
    public static final Message dao = new Message().dao();

    public static Message save(Message message){
        if(message.save())  {
            return message;
        }
        return null;
    }

    public static Message update(Message message){
        if(message.update()){
            return message;
        }
        return null;
    }

    public static boolean delete(String id){
        return dao.deleteById(id);
    }

    /**
     * 模糊搜索，并且限定返回个数
     */
    public static List<Message> getMessagesByFuzzyQuery(String title, String content, String count){
        StringBuffer sql = new StringBuffer("select * from messages ");
        if(title != null || content != null || count != null){
            sql.append("where ");
            boolean first = false;
            if(title != null)   {
                sql.append("title like '%").append(title).append("%' ");
                first = true;
            }
            if(content != null){
                if(first)   sql.append("and ");
                sql.append("content like '%").append(content).append("%' ");
            }
            if(count != null)   sql.append("limit 0, " + count);
        }
        return dao.find(sql.toString());
    }

    /**
     * 分页列表，并且支持模糊查询
     */
    public static Page<Message> getPaginateMessages(int pageNumber, int pageSize, String title, String content){
        StringBuffer sql = new StringBuffer("from messages ");
        if(title != null || content != null){
            sql.append("where ");
            boolean first = false;
            if(title != null)   {
                sql.append("title like '%").append(title).append("%' ");
                first = true;
            }
            if(content != null){
                if(first)   sql.append("and ");
                sql.append("content like '%").append(content).append("%' ");
            }
        }
        return dao.paginate(pageNumber, pageSize, "select *", sql.toString());
    }

}
