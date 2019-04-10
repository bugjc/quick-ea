package com.bugjc.ea.gateway.mapper;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.bugjc.ea.gateway.model.App;
import com.bugjc.ea.gateway.model.AppSecurityConfig;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
    @Cached(name = "app:security_config:signature", expire = 120, cacheType = CacheType.REMOTE)
    List<AppSecurityConfig> selectAllByIsSignature();


    /**
     * 根据appId 查询
     * @return
     */
    @Select("select * from app_security_config where enabled = true ORDER BY is_verify_token ASC")
    @Cached(name = "app:security_config:token", expire = 120, cacheType = CacheType.REMOTE)
    List<AppSecurityConfig> selectAllByIsVerifyToken();

    /**
     * 添加APP
     * @param appSecurityConfig
     * @return
     */
    @Insert("INSERT  INTO `app_security_config`(`app_id`,`path`,`is_verify_signature`,`is_verify_token`,`enabled`,`description`,`create_time`) VALUES \n " +
            "(#{appId},#{path},#{isVerifySignature},#{isVerifyToken},#{enabled},#{description},#{createTime});")
    void insert(AppSecurityConfig appSecurityConfig);

}