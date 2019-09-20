package com.bugjc.ea.server.message.core.constants;

import java.math.BigDecimal;

/**
 * 订单业务常量
 * @author aoki
 */
public class OrderConstants {

    /**
     * 账户金额阈值
     */
    public final static double DEFAULT_AMOUNT = 0;

    /**
     * 账户金额阈值
     */
    public final static BigDecimal BOUNDARY_AMOUNT = new BigDecimal(0);

    /**
     * 账户金额阈值
     */
    public final static BigDecimal ACCOUNT_AMOUNT_THRESHOLD2 = new BigDecimal(10);

    /**
     * 账户金额阈值
     */
    public final static BigDecimal ACCOUNT_AMOUNT_THRESHOLD = new BigDecimal(5);

    /**
     * 操作成功
     */
    public final static int SUCCESSFUL_OPERATION = 0;

    /**
     * 操作失败
     */
    public final static int OPERATION_FAILED = 1;

}
