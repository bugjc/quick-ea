package com.bugjc.ea.gateway.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.gateway.service.AppService;
import com.bugjc.ea.gateway.mapper.AppMapper;
import com.bugjc.ea.gateway.model.App;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @author aoki
 */
@Service
public class AppServiceImpl implements AppService {

    @Resource
    private AppMapper appMapper;

    @Override
    public boolean checkRoutePermission(String appId, String path) {
        //TODO 检查路由权限
        return false;
    }

    @Override
    public App findByAppId(String appId) {
        return appMapper.selectByAppId(appId);
    }

    @Override
    public void add(App app) {
        Date date = new Date();
        app.setCreateTime(date);
        appMapper.insert(app);
    }
}
