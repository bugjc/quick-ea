package com.bugjc.ea.gateway.service.impl;

import com.bugjc.ea.gateway.mapper.AppSecurityConfigMapper;
import com.bugjc.ea.gateway.model.AppSecurityConfig;
import com.bugjc.ea.gateway.service.AppSecurityConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author aoki
 */
@Slf4j
@Service
public class AppSecurityConfigServiceImpl implements AppSecurityConfigService {

    @Resource
    private AppSecurityConfigMapper appConfigMapper;

    @Override
    public int checkSignatureVersion(String path) {
        //TODO 检查接口版本
        return 1;
    }

    @Override
    public boolean excludeNeedSignaturePath(String path) {

        List<AppSecurityConfig> list = appConfigMapper.selectAllByIsSignature();
        if (list == null || list.isEmpty()){
            return true;
        }

        final AntPathMatcher pathMatcher = new AntPathMatcher();
        for (AppSecurityConfig appSecurityConfig : list) {
            if (pathMatcher.match(appSecurityConfig.getPath(),path) && !appSecurityConfig.isVerifySignature()){
                log.info("接口路径{},无需验证签名",path);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean excludeNeedAuthTokenPath(String path) {

        final AntPathMatcher pathMatcher = new AntPathMatcher();
        if (pathMatcher.match("/jwt/**",path)) {
            //授权认证服务不处理
            return true;
        }

        List<AppSecurityConfig> list = appConfigMapper.selectAllByIsVerifyToken();
        if (list == null || list.isEmpty()){
            return false;
        }

        for (AppSecurityConfig appSecurityConfig : list) {
            if (pathMatcher.match(appSecurityConfig.getPath(),path) && !appSecurityConfig.isVerifyToken()){
                log.info("接口路径{},无需验证Token",path);
                return true;
            }
        }
        return false;
    }
}
