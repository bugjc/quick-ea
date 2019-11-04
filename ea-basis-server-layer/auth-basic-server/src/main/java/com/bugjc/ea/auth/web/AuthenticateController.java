package com.bugjc.ea.auth.web;

import com.bugjc.ea.auth.biz.TokenBiz;
import com.bugjc.ea.auth.web.io.platform.auth.QueryTokenBody;
import com.bugjc.ea.auth.web.io.platform.auth.VerifyTokenBody;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 平台认证
 * @author aoki
 */
@Slf4j
@RestController
public class AuthenticateController {

    @Resource
    private TokenBiz tokenBiz;

    /**
     * 获取 token
     * @return
     */
    @PostMapping("query_token")
    public Result queryToken(@Validated @RequestBody QueryTokenBody.RequestBody requestBody) throws Exception {
        return tokenBiz.getToken(requestBody);
    }

    /**
     * 校验 token
     * @return
     */
    @PostMapping("verify_token")
    public Result verifyToken(@Validated @RequestBody VerifyTokenBody.RequestBody requestBody) throws Exception {
        return tokenBiz.verifyToken(requestBody);
    }


}
