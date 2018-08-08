package com.bugjc.ea.qrcode.dao;

import com.bugjc.ea.qrcode.dao.provider.OrderProvider;
import com.bugjc.ea.qrcode.model.Order;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 *
 * @author aoki
 */
@Mapper
public interface OrderMapper {

    /**
     * 插入数据
     * @param order
     * @return
     */
    @InsertProvider(type = OrderProvider.class,method = "insert")
    @Options(keyColumn="id",useGeneratedKeys=true)
    int insert(Order order);
}