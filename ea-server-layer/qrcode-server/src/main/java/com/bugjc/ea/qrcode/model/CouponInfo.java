package com.bugjc.ea.qrcode.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CouponInfo implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 订单流水号
     */
    private String orderNo;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 出资方
     */
    private String spnsrId;

    /**
     * 抵消交易金额
     */
    private Double offstAmt;

    /**
     * 项目编号
     */
    private String projectId;

    /**
     * 项目简称
     */
    private String desc;

    /**
     * 附加信息
     */
    private String addnInfo;

}