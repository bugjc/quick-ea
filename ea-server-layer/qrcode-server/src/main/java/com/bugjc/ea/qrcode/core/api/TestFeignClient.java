package com.bugjc.ea.qrcode.core.api;

import com.bugjc.ea.qrcode.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 依赖会员服务接口
 * @author qingyang
 * @date 2018/7/29 16:24
 */
@FeignClient(name = "member-server",configuration = FeignClientConfig.class)
public interface TestFeignClient{

}
