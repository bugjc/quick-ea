package com.bugjc.ea.qrcode.dao;

import com.bugjc.ea.qrcode.dao.provider.CouponInfoProvider;
import com.bugjc.ea.qrcode.model.CouponInfo;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface CouponInfoMapper{

    /**
     * 插入数据
     * @param list
     * @return
     */
    @InsertProvider(type = CouponInfoProvider.class,method = "insert")
    void insert(List<CouponInfo> list);
}