package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.TableCols;
import com.hzhwck.util.ResponseUtil;
import com.hzhwck.validator.hwc.TableColsValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class TableColsController extends BaseController{

    /**
     * 获取表格结构
     * @Param tableId 表格id{1,2,3}
     */
    @Before(TableColsValidator.class)
    @ActionKey("/api/hwc/tablecols")
    public void getTableCols(){
        String tableId = getPara("tableId");
        List<TableCols> tableCols = TableCols.getColsByTableId(tableId);
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("id", 0);
        List<Map<String, Object>> queue = new LinkedList<Map<String, Object>>();
        queue.add(root);
        while(true){
            Map<String, Object> temp = queue.remove(0);
            for(TableCols tableCol : tableCols){
                if(tableCol.getInt("pid") == (Integer)root.get("id")){
                    if(!temp.containsKey("child")){
                        temp.put("child", new LinkedList<Map<String, Object>>());
                    }
                    Map<String, Object> x = tableCol.toRecord().getColumns();
                    ((List)temp.get("child")).add(x);
                    queue.add(x);
                }
            }
            if(queue.size() == 0)   break;
        }
        renderJson(ResponseUtil.setRes("00", "获取表格结构成功", root));
    }
}
