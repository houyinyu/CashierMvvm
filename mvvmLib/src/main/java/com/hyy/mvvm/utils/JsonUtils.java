package com.hyy.mvvm.utils;

import com.google.gson.Gson;

public class JsonUtils {

    /**
     * json 2 对象
     *
     * @param jsonStr json字符串
     * @param clazz   对象
     * @return clazz的对象。
     */
    public static <T> T parseJson(String jsonStr, Class<T> clazz) {
        return new Gson().fromJson(jsonStr, clazz);
    }

    /**
     * 对象2json字符串
     *
     * @param obj
     * @return json字符串
     */
    public static String Object2Json(Object obj) {
        return new Gson().toJson(obj);
    }



}
