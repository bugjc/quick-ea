package com.bugjc.ea.opensdk.http.core.component.monitor.util;

import java.util.ArrayList;
import java.util.List;

public class StrUtil {

    /**
     * 数组格式字符串转成集合
     * @param arrStr   -- 数组格式的字符串，例如：[TotalRequests, SuccessRequests]
     * @return
     */
    public static List<String> arrStrToList(String arrStr){
        String[] strArray = arrStr.split("[^\\w\\d]");
        List<String> list = new ArrayList<>();
        for (String str : strArray) {
            if (str != null && !str.isEmpty()) {
                list.add(str);
            }
        }
        return list;
    }
}
