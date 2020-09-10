package com.bugjc.ea.auth.biz;


import cn.hutool.core.bean.BeanUtil;
import com.bugjc.ea.auth.core.enums.QueryTokenFailCode;
import com.bugjc.ea.auth.core.enums.VerifyTokenFailCode;
import com.bugjc.ea.auth.model.App;
import com.bugjc.ea.auth.model.AppToken;
import com.bugjc.ea.auth.service.AppService;
import com.bugjc.ea.auth.service.AppTokenService;
import com.bugjc.ea.auth.web.io.platform.auth.QueryTokenBody;
import com.bugjc.ea.auth.web.io.platform.auth.VerifyTokenBody;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author aoki
 * @create 2018/12/14.
 */
@Slf4j
@Service
public class TokenBiz {
    @Resource
    private AppTokenService tokenService;
    @Resource
    private AppService appService;

    /**
     * 获取token
     *
     * @param requestBody
     * @throws Exception
     */
    public Result getToken(QueryTokenBody.RequestBody requestBody) throws Exception {

        QueryTokenBody.ResponseBody responseBody = new QueryTokenBody.ResponseBody();
        responseBody.setFailCode(QueryTokenFailCode.SUCCESS.getCode());

        //查询运营商是否存在
        App app = appService.findByAppId(requestBody.getAppId());
        if (app == null) {
            //不存在运营商
            responseBody.setFailCode(QueryTokenFailCode.NoSuchCarrier.getCode());
            return Result.success(responseBody);
        }

        if (app.getAppSecret() == null || !app.getAppSecret().equals(requestBody.getAppSecret())) {
            //密钥不匹配
            return Result.failure(QueryTokenFailCode.KeyError);
        }

        try {
            AppToken token = tokenService.findToken(requestBody.getAppId());
            //设置业务成功状态
            responseBody.setSuccessStat(QueryTokenFailCode.SUCCESS.getCode());
            responseBody.setFailCode(QueryTokenFailCode.SUCCESS.getCode());
            BeanUtil.copyProperties(token, responseBody);

            return Result.success(responseBody);
        } catch (Exception e) {
            //业务失败
            log.error("生成 Token 失败,错误信息：{}", e.getMessage(), e);
            responseBody.setFailCode(QueryTokenFailCode.ERROR.getCode());
            return Result.failure(QueryTokenFailCode.ERROR);
        }
    }

    /**
     * 校验token
     *
     * @param requestBody
     * @return
     */
    public Result verifyToken(VerifyTokenBody.RequestBody requestBody) {
        VerifyTokenBody.ResponseBody responseBody = new VerifyTokenBody.ResponseBody();

        try {
            if (!tokenService.verifyToken(requestBody.getAccessToken())) {
                responseBody.setFailCode(VerifyTokenFailCode.ERROR.getCode());
                return Result.success(responseBody);
            }
        } catch (Exception ex) {
            log.error("校验 token 错误信息：{}", ex.getMessage());
            responseBody.setFailCode(VerifyTokenFailCode.ERROR.getCode());
        }

        return Result.success(responseBody);
    }
}
