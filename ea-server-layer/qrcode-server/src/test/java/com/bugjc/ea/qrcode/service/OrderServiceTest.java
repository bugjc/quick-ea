package com.bugjc.ea.qrcode.service;

import com.bugjc.ea.qrcode.Tester;
import com.bugjc.ea.qrcode.core.util.IdWorker;
import com.bugjc.ea.qrcode.dao.CouponInfoMapper;
import com.bugjc.ea.qrcode.dao.OrderMapper;
import com.bugjc.ea.qrcode.model.CodeExample;
import com.bugjc.ea.qrcode.model.CouponInfo;
import com.bugjc.ea.qrcode.model.Order;
import com.bugjc.ea.qrcode.service.CodeExampleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liuzh
 * @since 2017/9/2.
 */
@Slf4j
public class OrderServiceTest extends Tester {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private CouponInfoMapper couponInfoMapper;

    @Test
    public void test() {
//        Order order = new Order();
//        order.setTxnNo("00001");
//        int count = orderMapper.insert(order);
//        log.info("受影响行数："+count);
//        log.info("ID主键值："+order.getId());
        List<CouponInfo> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CouponInfo couponInfo = new CouponInfo();
            couponInfo.setOrderNo(IdWorker.getNextId());
            list.add(couponInfo);
        }
        couponInfoMapper.insert(list);
    }
}
