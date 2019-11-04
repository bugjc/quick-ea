package com.bugjc.ea.auth.service.impl;

import cn.hutool.core.date.DateUtil;
import com.bugjc.ea.auth.core.constants.AppTokenConstants;
import com.bugjc.ea.auth.core.enums.business.AppTokenStatus;
import com.bugjc.ea.auth.core.util.IdWorker;
import com.bugjc.ea.auth.mapper.AppTokenMapper;
import com.bugjc.ea.auth.model.AppToken;
import com.bugjc.ea.auth.service.AppTokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
  * 令牌服务
  * @author yangqing
  * @date 2019/7/4
  **/
@Service
public class AppTokenServiceImpl implements AppTokenService {

    @Resource
    private AppTokenMapper appTokenMapper;

    @Override
    public AppToken findToken(String appId) {

        //token生命期内再次获取token会让旧和新token并存一小段段时间
        Date date = new Date();
        AppToken token = appTokenMapper.selLastByAppId(appId);
        if (token != null){
            //旧的token过期时间小于等于当前时间，则标记token即将过期状态并设置较低的存活时间
            if (new Date(DateUtil.offsetSecond(token.getCreateTime(),token.getTokenAvailableTime()).getTime()).compareTo(date) >= 0){
                token.setStatus(AppTokenStatus.TokenToBeDestroyed.getStatus());
                token.setTokenAvailableTime(AppTokenConstants.TOKEN_TO_BE_DESTROYED_AVAILABLE_TIME);
            }else{
                token.setStatus(AppTokenStatus.DestroyedToken.getStatus());
                token.setTokenAvailableTime(AppTokenConstants.DESTROYED_TOKEN_AVAILABLE_TIME);
            }

            appTokenMapper.updateByPrimaryKey(token);
        }

        //生成新的token
        AppToken newToken = new AppToken();
        newToken.setAccessToken(IdWorker.getNextId());
        newToken.setAppId(appId);
        newToken.setTokenAvailableTime(AppTokenConstants.CURRENT_LATEST_TOKEN_AVAILABLE_TIME);
        newToken.setCreateTime(date);
        newToken.setStatus(AppTokenStatus.CurrentLatestToken.getStatus());
        appTokenMapper.insert(newToken);
        return newToken;
    }

    @Override
    public boolean verifyToken(String accessToken) {
        accessToken = accessToken.replace(AppTokenConstants.BEARER,"");
        AppToken token = appTokenMapper.selByAccessToken(accessToken);
        if (token == null){
            return false;
        }

        if (token.getStatus() == AppTokenStatus.DestroyedToken.getStatus()){
            return false;
        }

        return new Date(DateUtil.offsetSecond(token.getCreateTime(), token.getTokenAvailableTime()).getTime()).compareTo(new Date()) >= 0;
    }
}
