package com.bugjc.ea.gateway.mapper;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.bugjc.ea.gateway.model.App;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
    @Cached(name = "app:", key = "#appId", expire = 120, cacheType = CacheType.BOTH)
    App selectByAppId(@Param("appId") String appId);

    @Select("select * from app where enabled = true")
    @Cached(name = "app:all", key = "#appId", expire = 120, cacheType = CacheType.BOTH)
    List<App> selectAll();

    /**
     * 添加APP
     * @param app
     * @return
     */
    @Insert("INSERT  INTO `app`(`app_id`,`rsa_public_key`,`rsa_private_key`,`enabled`,type,`description`,`create_time`) VALUES \n " +
            "(#{appId},#{rsaPublicKey},#{rsaPrivateKey},#{enabled},#{type},#{description},#{createTime})")
    void insert(App app);
}