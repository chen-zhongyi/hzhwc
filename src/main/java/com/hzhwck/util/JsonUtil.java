package com.hzhwck.util;

import com.google.gson.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/27.
 */
public class JsonUtil {
    public static Map<String, Object> jsonToMap(String jsonStr){
        JsonElement je = new JsonParser().parse(jsonStr);
        if(!(je instanceof JsonObject)) {
            return new HashMap<String, Object>();
        }
        Map<String, Object> result = new HashMap<String, Object>();
        JsonObject jo = je.getAsJsonObject();
        for(Map.Entry<String, JsonElement> temp : jo.entrySet()){
            JsonElement jsonElement = temp.getValue();
            if(jsonElement instanceof JsonNull) continue;
            if(jsonElement instanceof JsonObject){
                result.put(temp.getKey(), jsonToMap(jsonElement.toString()));
            }else if(jsonElement instanceof JsonArray){
                result.put(temp.getKey(), jsonToList(jsonElement.toString()));
            }else {
                result.put(temp.getKey(), temp.getValue().getAsString());
            }
        }
        return result;
    }

    public static List<Object> jsonToList(String jsonStr){
        JsonElement je = new JsonParser().parse(jsonStr);
        if(!(je instanceof JsonArray)){
            return new LinkedList<Object>();
        }
        List<Object> result = new LinkedList<Object>();
        JsonArray ja = je.getAsJsonArray();
        for(JsonElement temp : ja){
            if(temp instanceof JsonNull)    continue;
            if(temp instanceof JsonObject){
                result.add(jsonToMap(temp.toString()));
            }else if(temp instanceof JsonArray){
                result.add(jsonToList(temp.toString()));
            }else {
                result.add(temp.getAsString());
            }
        }
        return result;
    }

    public static boolean isJsonStr(String jsonStr){
        try{
            new JsonParser().parse(jsonStr);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
