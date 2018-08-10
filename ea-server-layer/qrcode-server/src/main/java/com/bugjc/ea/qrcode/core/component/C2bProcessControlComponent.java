package com.bugjc.ea.qrcode.core.component;


import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CreateCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 消费二维码生命流程控制
 * @author : qingyang
 */
@Component
public class C2bProcessControlComponent {

    @CreateCache
    @CachePenetrationProtect
    private Cache<String, ProcessController> processControlCache;

    @CreateCache
    @CachePenetrationProtect
    private Cache<String, Map<String,String>> reqReservedMessageCache;

    /** 流程状态 Key **/
    private static String CONSUME_QR_CODE_STATUS_KEY = "consume:qrcode:status:";
    /** 附加请求报文通知 Key **/
    private static String CONSUME_QR_CODE_MESSAGE_KEY = "consume:qrcode:message:";

    /**
     * 设置流程状态
     * @param qrNo
     * @param status
     * @param timeout 过期时间为码的生命时间
     */
    public void setProcessController(String qrNo,int status,long timeout){
        String key = CONSUME_QR_CODE_STATUS_KEY + qrNo;
        processControlCache.put(key,new ProcessController(status),timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取流程状态
     * @param qrNo
     * @return
     */
    public String getProcessController(String qrNo){
        String key = CONSUME_QR_CODE_STATUS_KEY + qrNo;
        ProcessController processController = processControlCache.get(key);
        if (processController == null){
            processController = new ProcessController(Status.EXPIRED.ordinal());
        }
        return JSON.toJSONString(processController);
    }

    /**
     * 设置附加请求报文
     * @param qrNo
     * @param message
     * @param timeout 过期时间为码的生命时间
     */
    public void setReqReservedMessage(String qrNo, Map<String,String> message, long timeout){
        String key = CONSUME_QR_CODE_MESSAGE_KEY + qrNo;
        reqReservedMessageCache.put(key,message,timeout,TimeUnit.SECONDS);
    }

    /**
     * 获取附加请求报文
     * @param qrNo
     * @return
     */
    public Map<String, String> getReqReservedMessage(String qrNo){
        String key = CONSUME_QR_CODE_MESSAGE_KEY + qrNo;
        Map<String, String> message = reqReservedMessageCache.get(key);
        if (message.isEmpty()){
           return null;
        }
        return message;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ProcessController implements Serializable {

        /**
         * 流程状态
         */
        private int status;
    }

    public enum Status{
        /**
         * 0：获取二维码
         */
        GET_QR_CODE,
        /**
         * 1：需验证支付密码
         */
        VERIFY_PWD,
        /**
         * 2：需提示已超最大支付额度
         */
        ALERT_MAX_AMT,
        /**
         * 3: 已校验支付密码
         */
        HAVE_VERIFY_PWD,
        /**
         * 4：支付成功
         */
        TRADE_SUCCESS,
        /**
         * 5：支付失败
         */
        TRADE_FAIL,
        /**
         * 6：已过期
         */
        EXPIRED
    }

}

