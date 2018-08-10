package com.bugjc.ea.qrcode.dao.provider;

import com.bugjc.ea.qrcode.model.CouponInfo;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author aoki
 * @date ${date}
 */
public class CouponInfoProvider {

    /**
     * 插入优惠信息
     * 由Mapper传入的List在SQL构造类中将会包装在一个Map
     * @return
     */
    public String batchInsert(final Map map){
        //key就是Mapper中@Param注解配置的名称 
        List<CouponInfo> list = (List<CouponInfo>) map.get("list");
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO tbs_coupon_info ");
        sb.append("(order_no) ");
        sb.append("VALUES ");
        MessageFormat mf = new MessageFormat("(#'{'list[{0}].orderNo, jdbcType=VARCHAR})");
        for (int i = 0; i < list.size(); i++) {
            sb.append(mf.format(new Object[]{i+""}));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        
        return sb.toString();
    }

}
