package com.bugjc.ea.qrcode.model;

import java.io.Serializable;

public class ComInfo implements Serializable {
    /**
     * 订单流水号
     */
    private String orderNo;

    /**
     * 报文类型
     */
    private Integer f0;

    /**
     * 交易类型码
     */
    private Integer f3;

    /**
     * 服务点条件码
     */
    private Integer f25;

    /**
     * 检索参考号
     */
    private String f37;

    /**
     * 自定义域
     */
    private String f60;
}