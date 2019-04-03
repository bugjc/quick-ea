package com.bugjc.ea.gateway.mapper;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.bugjc.ea.gateway.model.AppConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author aoki
 */
@Mapper
public interface AppConfigMapper {

    /**
     * 根据appId 查询
     * @return
     */
    @Select("select * from app_exclude_security_authentication_path where enabled = true ORDER BY is_signature ASC")
    @Cached(name = "app:exclude:path", expire = 120, cacheType = CacheType.REMOTE)
    List<AppConfig> selectAllByIsSignature();


    /**
     * 根据appId 查询
     * @return
     */
    @Select("select * from app_exclude_security_authentication_path where enabled = true ORDER BY is_verify_token ASC")
    @Cached(name = "app:exclude:path", expire = 120, cacheType = CacheType.REMOTE)
    List<AppConfig> selectAllByIsVerifyToken();

}