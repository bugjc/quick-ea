package com.bugjc.ea.gateway.mapper;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.bugjc.ea.gateway.model.AppVersionMap;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author aoki
 */
@Mapper
public interface AppVersionMapMapper {

    /**
     * 根据appid和版本查询路径映射表
     * @param appId
     * @return
     */
    @Select("SELECT * FROM `app_version_map` a WHERE a.`app_id` = #{appId}")
    @Cached(name = "app:version_map:", key = "#appId", expire = 120, cacheType = CacheType.REMOTE)
    List<AppVersionMap> selectByAppIdAndVersion(@Param("appId") String appId);

    /**
     * 添加APP
     * @param appVersionMap
     * @return
     */
    @Insert("INSERT  INTO `app_version_map`(`app_id`,`version_no`,`path`,`map_path`,`description`,`create_time`) VALUES \n " +
            "(#{appId},#{versionNo},#{path},#{mapPath},#{description},#{createTime});")
    void insert(AppVersionMap appVersionMap);
}