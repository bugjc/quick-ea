package com.bugjc.ea.qrcode.dao.provider;

import com.bugjc.ea.qrcode.model.CouponInfo;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author aoki
 * @date ${date}
 */
public class CouponInfoProvider {

    /**
     * 插入优惠信息
     * @return
     */
    public String insert(final List<CouponInfo> list){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO tbs_coupon_info ");
        sb.append("(order_no) ");
        sb.append("VALUES ");
        MessageFormat mf = new MessageFormat("(#'{'userList[0].orderNo, jdbcType=VARCHAR})");
        for (int i = 0; i < list.size(); i++) {
            sb.append(mf.format(new Object[]{i+""}));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

}
