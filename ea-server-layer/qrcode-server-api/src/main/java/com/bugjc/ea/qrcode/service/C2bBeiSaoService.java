package com.bugjc.ea.qrcode.service;

import com.bugjc.ea.qrcode.model.Order;

import java.util.Map;

/**
 * c2b 被扫
 * @author aoki
 * @date ${date}
 */
public interface C2bBeiSaoService {

    /**
     * 申请码数据
     * @param userId
     * @return
     * @throws Exception
     */
    Map<String, String> applyCode(String userId) throws Exception;

    /**
     * 交易通知处理
     * @param order
     * @return
     */
    String tradeNoticeProcess(String version,Order order);

    /**
     * 交易前通知处理
     * @param order
     * @return
     */
    String preTradeCondition(Order order);

    void insert(Order order);

}
