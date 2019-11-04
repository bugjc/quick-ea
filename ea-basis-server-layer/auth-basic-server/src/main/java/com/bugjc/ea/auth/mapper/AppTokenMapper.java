package com.bugjc.ea.auth.mapper;

import com.bugjc.ea.auth.model.AppToken;
import org.apache.ibatis.annotations.*;


/**
 * @author aoki
 */
@Mapper
public interface AppTokenMapper {

    /**
     * 查询运营商token
     * @param appId
     * @return
     */
    @Select("SELECT * from app_token " +
            "where app_id = #{appId,jdbcType=VARCHAR} " +
            "order by create_time desc " +
            "limit 1;")
    AppToken selLastByAppId(@Param("appId") String appId);

    /**
     * 查询token是否存在
     * @param token
     * @return
     */
    @Select("select * from app_token where access_token = #{token,jdbcType=VARCHAR}")
    AppToken selByAccessToken(@Param("token") String token);

    /**
     * 更新token
     * @param token
     */
    @Update("UPDATE `app_token` " +
            "SET `app_id` = #{appId}, `access_token` = #{accessToken}, `token_available_time` = #{tokenAvailableTime}, `status` = #{status}, `create_time` = #{createTime} " +
            "WHERE `id` = #{id};")
    void updateByPrimaryKey(AppToken token);

    /**
     * 插入token
     * @param newToken
     */
    @Insert("INSERT INTO `app_token` (`app_id`,`access_token`,`token_available_time`,`status`,`create_time`) " +
            "VALUES (#{appId}, #{accessToken}, #{tokenAvailableTime}, #{status}, #{createTime});")
    void insert(AppToken newToken);
}