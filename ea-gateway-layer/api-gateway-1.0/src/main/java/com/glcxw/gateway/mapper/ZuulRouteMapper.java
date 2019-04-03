package com.glcxw.gateway.mapper;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.glcxw.gateway.model.CustomZuulRoute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    @Cached(name = "zuul:route:", expire = 120, cacheType = CacheType.REMOTE)
    List<CustomZuulRoute> selectAll();

    /**
     * 查询全部
     *
     * @return list
     */
    @Select("SELECT * FROM route WHERE enabled = TRUE AND id = #{id}")
    @Cached(name = "zuul:route:id:", key = "#id", expire = 120, cacheType = CacheType.REMOTE)
    CustomZuulRoute selectById(@Param("id") String id);
}