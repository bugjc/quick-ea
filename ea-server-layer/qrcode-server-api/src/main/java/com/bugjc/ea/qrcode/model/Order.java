package com.bugjc.ea.qrcode.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单
 * @author aoki
 */
@Data
@Table(name = "tbs_order")
public class Order implements Serializable{
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易类型
     */
    @Column(name = "req_type")
    private String reqType;

    /**
     * 原交易类型
     */
    @Column(name = "orig_req_type")
    private String origReqType;

    /**
     * 交易序列号
     */
    @Column(name = "txn_no")
    private String txnNo;

    /**
     * C2B码
     */
    @Column(name = "qr_no")
    private String qrNo;

    /**
     * 交易币种
     */
    @Column(name = "currency_code")
    private String currencyCode;

    /**
     * 交易金额
     */
    @Column(name = "txn_amt")
    private Double txnAmt;

    /**
     * 商户代码
     */
    @Column(name = "mer_id")
    private String merId;

    /**
     * 商户类别
     */
    @Column(name = "mer_cat_code")
    private String merCatCode;

    /**
     * 商户名称
     */
    @Column(name = "mer_name")
    private String merName;

    /**
     * 终端号
     */
    @Column(name = "term_id")
    private String termId;

    /**
     * 付款凭证号
     */
    @Column(name = "voucher_num")
    private String voucherNum;

    /**
     * 清算主键
     */
    @Column(name = "settle_key")
    private String settleKey;

    /**
     * 清算日期
     */
    @Column(name = "settle_data")
    private String settleData;

    /**
     * 银行对账流水信息
     */
    @Column(name = "com_info")
    private String comInfo;

    /**
     * 请求方自定义域
     */
    @Column(name = "req_reserved")
    private String reqReserved;

    /**
     * 优惠信息
     */
    @Column(name = "coupon_info")
    private String couponInfo;

    /**
     * 初始交易金额
     */
    @Column(name = "orig_txn_amt")
    private Double origTxnAmt;

    /**
     * 扣账币种
     */
    @Column(name = "cdhd_curr_cd")
    private String cdhdCurrCd;

    /**
     * 扣账汇率
     */
    @Column(name = "cdhd_conv_rt")
    private String cdhdConvRt;

    /**
     * 扣账金额
     */
    @Column(name = "cdhd_amt")
    private Double cdhdAmt;

    /**
     * 清算币种
     */
    @Column(name = "settle_curr_code")
    private String settleCurrCode;

    /**
     * 清算汇率
     */
    @Column(name = "settle_conv_rate")
    private String settleConvRate;

    /**
     * 清算金额
     */
    @Column(name = "settle_amt")
    private Double settleAmt;

    /**
     * 兑换日期
     */
    @Column(name = "conv_date")
    private Date convDate;

    /**
     * EMV 二维码
     */
    @Column(name = "emv_qr_code")
    private String emvQrCode;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 授权标识应答码
     */
    @Column(name = "auth_id_resp_cd")
    private String authIdRespCd;

    /**
     * 收款方附加数据
     */
    @Column(name = "acp_addn_data")
    private String acpAddnData;
}