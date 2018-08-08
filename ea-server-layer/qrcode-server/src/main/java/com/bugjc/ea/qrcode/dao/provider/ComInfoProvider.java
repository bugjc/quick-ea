package com.bugjc.ea.qrcode.dao.provider;

import com.bugjc.ea.qrcode.model.ComInfo;
import com.bugjc.ea.qrcode.model.Order;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author aoki
 * @date ${date}
 */
public class ComInfoProvider {

    /**
     * 插入银行对账流水信息表
     * @return
     */
    public String insert(){
        return new SQL(){
            {
                INSERT_INTO("tbs_com_info");
                VALUES("order_no", "#{orderNo}");
                VALUES("f0", "#{f0}");
                VALUES("f3", "#{f3}");
                VALUES("f25", "#{f25}");
                VALUES("f37", "#{f37}");
                VALUES("f60", "#{f60}");
            }
        }.toString();
    }

}
