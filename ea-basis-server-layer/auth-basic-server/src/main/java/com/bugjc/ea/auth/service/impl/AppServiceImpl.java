package com.bugjc.ea.auth.service.impl;

import com.bugjc.ea.auth.mapper.AppMapper;
import com.bugjc.ea.auth.model.App;
import com.bugjc.ea.auth.service.AppService;
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
