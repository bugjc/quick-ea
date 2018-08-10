package com.bugjc.ea.qrcode.service;

import com.bugjc.ea.qrcode.Tester;
import com.bugjc.ea.qrcode.core.util.IdWorker;
import com.bugjc.ea.qrcode.dao.CouponInfoMapper;
import com.bugjc.ea.qrcode.dao.OrderMapper;
import com.bugjc.ea.qrcode.model.CouponInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuzh
 * @since 2017/9/2.
 */
@Slf4j
public class CouponServiceTest extends Tester {

    @Resource
    private CouponInfoMapper couponInfoMapper;

    @Test
    public void test() {
        List<CouponInfo> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CouponInfo couponInfo = new CouponInfo();
            couponInfo.setOrderNo(IdWorker.getNextId());
            list.add(couponInfo);
        }
        int count = couponInfoMapper.batchInsert(list);
        log.info("受影响行数："+count);
    }
}
