package com.bugjc.ea.member.service;

import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.member.web.http.body.memberinfo.GetBody;
import feign.Param;
import feign.RequestLine;

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
