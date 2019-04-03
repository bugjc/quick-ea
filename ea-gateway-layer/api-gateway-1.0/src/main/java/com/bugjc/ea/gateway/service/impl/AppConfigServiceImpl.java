package com.bugjc.ea.gateway.service.impl;

import com.bugjc.ea.gateway.mapper.AppConfigMapper;
import com.bugjc.ea.gateway.model.AppConfig;
import com.bugjc.ea.gateway.service.AppConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author aoki
 */
@Slf4j
@Service
public class AppConfigServiceImpl implements AppConfigService {

    @Resource
    private AppConfigMapper appConfigMapper;

    @Override
    public int checkSignatureVersion(String path) {
        if (path.startsWith("/memberapi") || path.startsWith("/ewallet") || path.startsWith("/member-api")) {
            return 2;
        }
        return 1;
    }

    @Override
    public boolean excludeNeedSignaturePath(String path) {

        List<AppConfig> list = appConfigMapper.selectAllByIsSignature();
        if (list == null || list.isEmpty()){
            return true;
        }
        for (AppConfig appExcludeSignaturePath : list) {
            if (path.startsWith(appExcludeSignaturePath.getExcludePath()) && !appExcludeSignaturePath.getIsSignature()){
                log.info("接口路径{},无需验证签名",path);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean excludeNeedAuthTokenPath(String path) {

        if (path.startsWith("/memberapi/api/new")
                || path.startsWith("/memberapi/api/member")
                || path.startsWith("/memberapi/common/sms")
                || path.startsWith("/memberapi/ext/api")) {
            //登录不处理
            return true;
        }

        List<AppConfig> list = appConfigMapper.selectAllByIsVerifyToken();
        if (list == null || list.isEmpty()){
            return false;
        }
        for (AppConfig appExcludeSignaturePath : list) {
            if (path.startsWith(appExcludeSignaturePath.getExcludePath()) && !appExcludeSignaturePath.getIsVerifyToken()){
                log.info("接口路径{},无需验证Token",path);
                return true;
            }
        }
        return false;
    }
}
