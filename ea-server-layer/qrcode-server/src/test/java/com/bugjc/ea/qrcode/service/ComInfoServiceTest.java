package com.bugjc.ea.qrcode.service;

import com.bugjc.ea.qrcode.Tester;
import com.bugjc.ea.qrcode.core.util.IdWorker;
import com.bugjc.ea.qrcode.dao.ComInfoMapper;
import com.bugjc.ea.qrcode.dao.CouponInfoMapper;
import com.bugjc.ea.qrcode.model.ComInfo;
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
public class ComInfoServiceTest extends Tester {

    @Resource
    private ComInfoMapper comInfoMapper;

    @Test
    public void test() {
        ComInfo comInfo = new ComInfo();
        comInfo.setOrderNo(IdWorker.getNextId());
        int count = comInfoMapper.insert(comInfo);
        log.info("受影响行数："+count);
        log.info("主键ID值："+comInfo.getOrderNo());
    }
}
