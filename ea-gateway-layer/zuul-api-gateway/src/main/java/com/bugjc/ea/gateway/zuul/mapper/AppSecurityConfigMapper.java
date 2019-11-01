package com.bugjc.ea.gateway.zuul.mapper;

import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.bugjc.ea.gateway.zuul.model.AppSecurityConfig;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author aoki
 */
@Mapper
public interface AppSecurityConfigMapper {

    /**
     * 根据appId 查询
     * @return
     */
    @Select("select * from app_security_config where enabled = true ORDER BY is_verify_signature ASC")
    @Cached(cacheType = CacheType.LOCAL, expire = 2, timeUnit = TimeUnit.MINUTES)
    @CacheRefresh(refresh = 2, timeUnit = TimeUnit.MINUTES)
    List<AppSecurityConfig> selectAllByIsSignature();


    /**
     * 根据appId 查询
     * @return
     */
    @Select("select * from app_security_config where enabled = true ORDER BY is_verify_token ASC")
    @Cached(cacheType = CacheType.LOCAL, expire = 2, timeUnit = TimeUnit.MINUTES)
    @CacheRefresh(refresh = 2, timeUnit = TimeUnit.MINUTES)
    List<AppSecurityConfig> selectAllByIsVerifyToken();

    /**
     * 添加APP
     * @param appSecurityConfig
     * @return
     */
    @Insert("INSERT  INTO `app_security_config`(id,`app_id`,`path`,`is_verify_signature`,`is_verify_token`,`enabled`,`description`,`create_time`) VALUES \n " +
            "(#{id},#{appId},#{path},#{isVerifySignature},#{isVerifyToken},#{enabled},#{description},#{createTime});")
    void insert(AppSecurityConfig appSecurityConfig);

}