package com.bugjc.ea.gateway.biz;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.gateway.core.enums.AppTokenStatus;
import com.bugjc.ea.gateway.model.App;
import com.bugjc.ea.gateway.model.AppToken;
import com.bugjc.ea.gateway.service.AppService;
import com.bugjc.ea.gateway.service.AppTokenService;
import com.bugjc.ea.gateway.web.io.platform.auth.QueryTokenBody;
import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.http.opensdk.core.dto.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
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
     * @param requestBody
     * @throws Exception
     */
    public Result getToken(QueryTokenBody.RequestBody requestBody) throws Exception {

        QueryTokenBody.ResponseBody responseBody = new QueryTokenBody.ResponseBody();
        responseBody.setFailReason(AppTokenStatus.SUCCESS.getStatus());

        //查询运营商是否存在
        App app = appService.findByAppId(requestBody.getAppId());
        if (app == null){
            //不存在运营商
            responseBody.setFailReason(AppTokenStatus.NoSuchCarrier.getStatus());
            return ResultGenerator.genSuccessResult(responseBody);
        }

        if (!app.getAppSecret().equals(requestBody.getAppSecret())){
            //密钥不匹配
            responseBody.setFailReason(AppTokenStatus.KeyError.getStatus());
            return ResultGenerator.genFailResult(AppTokenStatus.KeyError.getDesc());
        }

        try {
            AppToken token = tokenService.findToken(requestBody.getAppId());
            //设置业务成功状态
            responseBody.setSuccessStat(AppTokenStatus.SUCCESS.getStatus());
            responseBody.setFailReason(AppTokenStatus.SUCCESS.getStatus());
            BeanUtil.copyProperties(token,responseBody);

            return ResultGenerator.genSuccessResult(responseBody);
        } catch (Exception e) {
            //业务失败
            log.error("生成 Token 失败,错误信息：{}",e.getMessage(),e);
            responseBody.setFailReason(AppTokenStatus.ERROR.getStatus());
            return ResultGenerator.genFailResult(AppTokenStatus.ERROR.getDesc());
        }
    }

    /**
     * 校验token
     * @param token
     * @return
     */
    public boolean verifyToken(String token){
        if (StrUtil.isBlank(token)){
            return false;
        }

        return tokenService.verifyToken(token);
    }
}
