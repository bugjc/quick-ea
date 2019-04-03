package com.glcxw.gateway.service.impl;

import com.glcxw.gateway.mapper.AppMapper;
import com.glcxw.gateway.model.App;
import com.glcxw.gateway.service.AppService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author aoki
 */
@Service
public class AppServiceImpl implements AppService {

    @Resource
    private AppMapper appMapper;

    @Override
    public App findByAppId(String appId) {
        return appMapper.selectByAppId(appId);
    }
}
