package com.bugjc.ea.qrcode.dao;

import com.bugjc.ea.qrcode.dao.provider.ComInfoProvider;
import com.bugjc.ea.qrcode.model.ComInfo;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * @author aoki
 */
@Mapper
public interface ComInfoMapper{

    /**
     * 插入数据
     * @param comInfo
     * @return
     */
    @InsertProvider(type = ComInfoProvider.class,method = "insert")
    @Options(keyColumn="order_no",useGeneratedKeys=true)
    int insert(ComInfo comInfo);
}