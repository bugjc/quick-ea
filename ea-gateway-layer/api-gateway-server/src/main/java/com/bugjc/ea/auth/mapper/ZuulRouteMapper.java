package com.bugjc.ea.auth.mapper;

import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.bugjc.ea.auth.model.CustomZuulRoute;
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
public interface ZuulRouteMapper {

    /**
     * 查询全部
     * @return list
     */
    @Select("select * from route where enabled = true")
    @Cached(cacheType = CacheType.LOCAL, expire = 2, timeUnit = TimeUnit.MINUTES)
    @CacheRefresh(refresh = 2, timeUnit = TimeUnit.MINUTES)
    List<CustomZuulRoute> selectAll();

    /**
     * 查询全部
     *
     * @return list
     */
    @Select("SELECT * FROM route WHERE enabled = TRUE AND id = #{id}")
    @Cached(cacheType = CacheType.LOCAL, expire = 2, timeUnit = TimeUnit.MINUTES)
    @CacheRefresh(refresh = 2, timeUnit = TimeUnit.MINUTES)
    CustomZuulRoute selectById(@Param("id") String id);

    /**
     * 添加APP
     * @param customZuulRoute
     * @return
     */
    @Insert("INSERT  INTO `route`(`id`,`path`,`service_id`,`url`,`ribbon_url`,`retryable`,`strip_prefix`,`enabled`,`description`) VALUES \n " +
            "(#{id},#{path},#{serviceId},#{url},#{ribbonUrl},#{retryable},#{stripPrefix},#{enabled},#{description})")
    void insert(CustomZuulRoute customZuulRoute);
}