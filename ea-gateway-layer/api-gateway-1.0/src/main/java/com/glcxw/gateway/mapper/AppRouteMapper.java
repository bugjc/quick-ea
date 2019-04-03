package com.glcxw.gateway.mapper;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.glcxw.gateway.model.AppRoute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author aoki
 */
@Mapper
public interface AppRouteMapper {

    /**
     * 根据appId 查询
     * @param appId
     * @return
     */
    @Select("select * from app_route where enabled = true and app_id = #{appId}")
    @Cached(name = "app_route:", key = "#appId", expire = 120, cacheType = CacheType.REMOTE)
    List<AppRoute> selectByAppId(@Param("appId") String appId);
}