package com.bugjc.ea.gateway.service.impl;

import com.bugjc.ea.gateway.service.AppService;
import com.bugjc.ea.gateway.mapper.AppMapper;
import com.bugjc.ea.gateway.model.App;
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
