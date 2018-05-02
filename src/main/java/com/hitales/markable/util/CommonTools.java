package com.hitales.markable.util;

import org.bson.Document;

/**
 * Created with IntelliJ IDEA
 * User:huangming
 * Date:2018/5/2
 * Time:下午6:30
 */
public class CommonTools {
    public  static String getJSonValue(Document jsonObject, String key)
    {
        if(jsonObject == null || key == null)return "";
        if(jsonObject.getString(key) ==null)
            return "";
        return jsonObject.getString(key);
    }
}
