package com.bugjc.ea.gateway.zuul.mapper;

import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.bugjc.ea.gateway.zuul.model.AppRoute;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
    @Cached(cacheType = CacheType.LOCAL, expire = 2, timeUnit = TimeUnit.MINUTES)
    @CacheRefresh(refresh = 2, timeUnit = TimeUnit.MINUTES)
    List<AppRoute> selectByAppId(@Param("appId") String appId);

    /**
     * 添加APP
     * @param appRoute
     * @return
     */
    @Insert("INSERT  INTO `app_route`(id,`app_id`,`route_id`,`is_debug`,enabled,create_time) VALUES (#{id},#{appId},#{routeId},#{isDebug},#{enabled},#{createTime})")
    void insert(AppRoute appRoute);
}