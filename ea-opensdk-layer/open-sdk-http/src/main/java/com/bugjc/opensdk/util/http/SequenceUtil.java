package com.bugjc.opensdk.util.http;

import cn.hutool.core.util.RandomUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单号生成工具，生成非重复订单号，理论上限1毫秒1000个，可扩展
 * @author aoki
 */
public class SequenceUtil {

    /** 
     * 锁对象，可以为任意对象 
     */  
    private final static Object LOCK_OBJ = "lockerOrder";

    /**
     * 生成序列
     * @return
     */
    static String genUnionPaySequence() {
        try {  
            // 最终生成的订单号  
            String finOrderNum = "";  
            synchronized (LOCK_OBJ) {
                // 取系统当前时间作为订单号变量前半部分，精确到毫秒
                String format = "yyyyMMddhhmmss";
                long nowLong = Long.parseLong(new SimpleDateFormat(format).format(new Date()));
                finOrderNum = nowLong+ RandomUtil.randomNumbers(10);
                return finOrderNum;
            }  
        } catch (Exception e) {
           return null;
        }  
    }

}  
