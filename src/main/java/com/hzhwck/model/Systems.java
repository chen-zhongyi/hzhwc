package com.hzhwck.model;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/5/23.
 */
public class Systems extends Model<Systems> {
    public static final Systems dao = new Systems().dao();

    public static List<Systems> getSystems(){
        return dao.find("select * from systems");
    }
}
