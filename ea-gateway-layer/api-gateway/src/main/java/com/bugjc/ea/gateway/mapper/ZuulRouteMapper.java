package com.bugjc.ea.gateway.mapper;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.bugjc.ea.gateway.model.CustomZuulRoute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
    @Cached(name = "zuul:route:api_gateway", cacheType = CacheType.BOTH)
    List<CustomZuulRoute> selectAll();
}