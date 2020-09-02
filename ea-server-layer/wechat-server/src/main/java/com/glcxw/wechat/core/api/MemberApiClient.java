package com.glcxw.wechat.core.api;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.glcxw.wechat.core.dto.Result;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aoki
 */
@Component
public class MemberApiClient {

    @Data
    @Configuration
    static
    class MemberConfig {
        @Value("${member-api.server.address}")
        private String memberApiServerAddress;
    }

    static class MemberContentPath {
        static final String FIND_BY_MEMBER_ID = "/api/new/findByMemberId";
        static final String FIND_BY_PHONE = "/api/new/findByPhone";
    }

    @Resource
    private MemberConfig memberConfig;

    public Result findByMemberId(String memberId) {
        //查询会员信息
        String url = memberConfig.getMemberApiServerAddress().concat(MemberContentPath.FIND_BY_MEMBER_ID);
        Map<String, Object> param = new HashMap<>();
        param.put("memberId", memberId);
        String resultJson = HttpRequest.post(url).form(param).execute().body();
        return JSONObject.parseObject(resultJson, Result.class);
    }

    public Result findByPhone(String phone) {
        //查询会员信息
        String url = memberConfig.getMemberApiServerAddress().concat(MemberContentPath.FIND_BY_PHONE);
        Map<String, Object> param = new HashMap<>();
        param.put("phone", phone);
        String resultJson = HttpRequest.post(url).form(param).execute().body();
        return JSONObject.parseObject(resultJson, Result.class);
    }
}
