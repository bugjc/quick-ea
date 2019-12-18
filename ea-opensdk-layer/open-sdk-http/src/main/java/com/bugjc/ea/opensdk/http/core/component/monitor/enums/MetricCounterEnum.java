package com.bugjc.ea.opensdk.http.core.component.monitor.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 监控计数指标定义
 * @author aoki
 */
public enum MetricCounterEnum {
    /**
     * 指标
     */
    TotalRequests(0,"总请求数"),
    SuccessRequests(1,"成功总请求数");

    private final int type;
    private final String desc;

    MetricCounterEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(MetricCounterEnum.values()));

        String[] strArray = Arrays.toString(MetricCounterEnum.values()).split("[^\\w\\d]");
        List<String> list = new ArrayList<>();
        for (String str : strArray) {
            if (str != null && !str.isEmpty()) {
                list.add(str);
            }
        }
        System.out.println(list.toString());
    }
}
