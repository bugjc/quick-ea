package com.glcxw.gateway.core.api;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glcxw.gateway.core.dto.Result;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aoki
 */
@Slf4j
@Component
public class MemberApiClient {


    @Data
    @Configuration
    class MemberConfig{
        @Value("${member.server.address}")
        private String memberApiServerAddress;
    }

    class MemberContentPath{
        static final String CHECK_TOKEN_PATH = "/ext/api/checkUserLogined";
    }


    @Resource
    private
    MemberConfig memberConfig;

    /**
     * 校验token
     * @param memberId
     * @param token
     * @return
     */
    public Result checkToken(String memberId, String token) {
        String url = memberConfig.getMemberApiServerAddress() + MemberContentPath.CHECK_TOKEN_PATH;
        Map<String, Object> formMap = new HashMap<>();
        String datetime = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        String key = "gldb2017@ugiant1234abc!~";
        String submitSign = SecureUtil.md5("key="+key+"&datetime="+datetime);
        String signStr = "memberId="+memberId + "&token=" + token + "&datetime="+datetime;
        log.info(signStr);
        String sign = SecureUtil.md5(signStr);
        formMap.put("memberId",memberId);
        formMap.put("submitTime",datetime);
        formMap.put("submitSign",submitSign);
        formMap.put("sign",sign);
        formMap.put("datetime",datetime);
        log.info(JSON.toJSONString(formMap));
        String json = HttpRequest.get(url).form(formMap).execute().body();
        JSONObject result = JSON.parseObject(json);
        Result tokenResult = new Result();
        if (result.getBoolean("success")) {
            tokenResult.setCode(200);
        } else {
            tokenResult.setCode(result.getInteger("code"));
        }
        log.info(result.toJSONString());
        return tokenResult;
    }

}
