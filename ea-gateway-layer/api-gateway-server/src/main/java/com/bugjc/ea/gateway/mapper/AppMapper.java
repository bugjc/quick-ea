package com.bugjc.ea.gateway.mapper;

import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.bugjc.ea.gateway.model.App;
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
public interface AppMapper {

    /**
     * 根据appId 查询
     * @param appId
     * @return
     */
    @Select("select * from app where enabled = true and app_id = #{appId}")
    @Cached(cacheType = CacheType.LOCAL, expire = 2, timeUnit = TimeUnit.MINUTES)
    @CacheRefresh(refresh = 2, timeUnit = TimeUnit.MINUTES)
    App selectByAppId(@Param("appId") String appId);

    /**
     * 查询所有应用信息
     * @return
     */
    @Select("select * from app where enabled = true")
    @Cached(cacheType = CacheType.LOCAL, expire = 2, timeUnit = TimeUnit.MINUTES)
    @CacheRefresh(refresh = 2, timeUnit = TimeUnit.MINUTES)
    List<App> selectAll();

    /**
     * 添加APP
     * @param app
     * @return
     */
    @Insert("INSERT  INTO `app`(`app_id`,app_secret,`rsa_public_key`,`rsa_private_key`,`enabled`,type,`description`,`create_time`) VALUES \n " +
            "(#{appId},#{appSecret},#{rsaPublicKey},#{rsaPrivateKey},#{enabled},#{type},#{description},#{createTime})")
    void insert(App app);
}