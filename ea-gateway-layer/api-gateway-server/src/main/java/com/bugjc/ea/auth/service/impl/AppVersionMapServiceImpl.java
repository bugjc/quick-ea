package com.bugjc.ea.auth.service.impl;

import com.bugjc.ea.auth.service.AppVersionMapService;
import com.bugjc.ea.auth.mapper.AppVersionMapMapper;
import com.bugjc.ea.auth.model.AppVersionMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author aoki
 */
@Service
public class AppVersionMapServiceImpl implements AppVersionMapService {

    @Resource
    private AppVersionMapMapper appVersionMapMapper;

    @Override
    public List<AppVersionMap> findByAppIdAndVersionNo(String appId, String versionNo) {
        //查询应用版本配置
        List<AppVersionMap> list = appVersionMapMapper.selectByAppIdAndVersion(appId);
        if (list == null || list.isEmpty()){
            return null;
        }

        List<AppVersionMap> appVersionMaps = new ArrayList<>();
        for (AppVersionMap appVersionMap : list) {

            if ("/".equals(appVersionMap.getPath())){
                continue;
            }

            if (appVersionMap.getVersionNo().trim().equals(versionNo.trim())){
                appVersionMaps.add(appVersionMap);
            }
        }
        return appVersionMaps;
    }
}
