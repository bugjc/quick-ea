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
     * @param userId
     * @param order
     */
    void tradeNoticeProcess(String userId,Order order);

    /**
     * 交易前通知处理
     * @param userId
     * @param map
     */
    void preTradeCondition(String userId,Map<String,String> map);

    /**
     * 获取流程结果
     * @param qrNo
     * @return
     */
    String findConsumeProcess(String qrNo);

    /**
     * 更新消费流程状态
     * @param qrNo
     * @param status
     */
    void updConsumeProcess(String qrNo,int status);

    /**
     * 消费码支付密码验证成功通知
     * @param qrNo
     * @param status
     */
    void verifySuccessNotify(String qrNo,int status);

    /**
     * 获取支付记录
     * @param qrNo
     * @return
     */
    Order findPayRecord(String qrNo);
}
