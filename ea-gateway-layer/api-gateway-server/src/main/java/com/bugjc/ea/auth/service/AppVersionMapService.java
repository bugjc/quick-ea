package com.bugjc.ea.auth.service;

import com.bugjc.ea.auth.model.AppVersionMap;

import java.util.List;

/**
 * @author aoki
 */
public interface AppVersionMapService {

    /**
     * 根据应用id 和版本号获取版本映射信息
     * @param appId
     * @param versionNo
     * @return
     */
    List<AppVersionMap> findByAppIdAndVersionNo(String appId, String versionNo);
}
