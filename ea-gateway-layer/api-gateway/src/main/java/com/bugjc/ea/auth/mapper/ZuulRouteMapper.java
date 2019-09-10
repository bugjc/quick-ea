package com.bugjc.ea.auth.mapper;

import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.bugjc.ea.auth.model.CustomZuulRoute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author aoki
 */
@Mapper
public interface ZuulRouteMapper {

    /**
     * 查询全部
     * @return list
     */
    @Select("select * from route where enabled = true")
    @Cached(cacheType = CacheType.LOCAL, expire = 2, timeUnit = TimeUnit.MINUTES)
    @CacheRefresh(refresh = 2, timeUnit = TimeUnit.MINUTES)
    List<CustomZuulRoute> selectAll();
}