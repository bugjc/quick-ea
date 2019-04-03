package com.bugjc.ea.gateway.mapper;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.bugjc.ea.gateway.model.App;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author aoki
 */
@Mapper
public interface AppMapper {

    /**
     * 根据appId 查询
     * @param appId
     * @return
     */
    @Select("select * from app where enabled = true and app_id = #{appId}")
    @Cached(name = "app:", key = "#appId", expire = 120, cacheType = CacheType.REMOTE)
    App selectByAppId(@Param("appId") String appId);
}