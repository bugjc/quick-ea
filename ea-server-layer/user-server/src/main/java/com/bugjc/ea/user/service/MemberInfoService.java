package com.bugjc.ea.user.service;

import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.user.web.io.info.GetBody;
import feign.Param;

/**
 * zipkin service test
 * @author qingyang
 */
public interface MemberInfoService {

    /**
     * 获取会员信息
     * @param requestBody
     * @return
     */
    Result get(GetBody.RequestBody requestBody);

    /**
     * 测试第一步
     * @param userId
     */
    String testZipkin(@Param("userId") String userId);

    /**
     * 测试断路器
     * @param userId
     */
    String testHystrix(@Param("userId") String userId);

}
