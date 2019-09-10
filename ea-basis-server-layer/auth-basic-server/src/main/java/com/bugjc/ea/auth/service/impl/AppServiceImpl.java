package com.bugjc.ea.auth.service.impl;

import com.bugjc.ea.auth.core.exception.BizException;
import com.bugjc.ea.auth.mapper.AppMapper;
import com.bugjc.ea.auth.model.App;
import com.bugjc.ea.auth.service.AppService;
import com.bugjc.ea.http.opensdk.core.util.SequenceUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
