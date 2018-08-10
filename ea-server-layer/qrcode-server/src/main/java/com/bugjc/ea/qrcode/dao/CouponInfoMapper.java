package com.bugjc.ea.qrcode.dao;

import com.bugjc.ea.qrcode.dao.provider.CouponInfoProvider;
import com.bugjc.ea.qrcode.model.CouponInfo;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠信息
 * @author Administrator
 */
@Mapper
public interface CouponInfoMapper{

    /**
     * 插入数据
     * @param list
     * @return
     */
    @InsertProvider(type = CouponInfoProvider.class,method = "batchInsert")
    @Options(keyColumn="id",useGeneratedKeys=true)
    int batchInsert(@Param("list") List<CouponInfo> list);
}