package com.bugjc.ea.gateway.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.gateway.core.exception.BizException;
import com.bugjc.ea.gateway.service.AppService;
import com.bugjc.ea.gateway.mapper.AppMapper;
import com.bugjc.ea.gateway.model.App;
import com.bugjc.ea.opensdk.core.util.SequenceUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
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

        //检查APP ID hash 值是否冲突
        List<App> apps = appMapper.selectAll();
        String appIdHashVal = SequenceUtil.getAppPrefixCode(app.getAppId());
        if (apps != null && !apps.isEmpty()){
            for (App app1 : apps) {
                String hashVal = SequenceUtil.getAppPrefixCode(app1.getAppId());
                if (appIdHashVal.equals(hashVal)){
                    throw new BizException("APP_ID的HASH值冲突！");
                }
            }
        }

        Date date = new Date();
        app.setCreateTime(date);
        appMapper.insert(app);
    }
}
