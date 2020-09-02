package com.glcxw.wechat.dao.provider;

import org.apache.ibatis.jdbc.SQL;

/**
 * @author aoki
 * @date ${date}
 */
public class WxMsgRecordProvider {

    private final String TABLE_NAME = "tbs_wx_msg_record";

    public String insert(){
        return new SQL(){
            {
                INSERT_INTO(TABLE_NAME);
                VALUES("msg_id", "#{msgId}");
                VALUES("user_id","#{userId}");
                VALUES("status","#{status}");
                VALUES("msg_type","#{msgType}");
                VALUES("to_user_name", "#{toUserName}");
                VALUES("form_user_name", "#{formUserName}");
                VALUES("update_time", "#{updateTime}");
                VALUES("create_time", "#{createTime}");
            }
        }.toString();
    }

    public String selByMsgId(){
        return new SQL(){{
            SELECT_DISTINCT("msg_id,status,msg_type,to_user_name,form_user_name,update_time,create_time");
            FROM(TABLE_NAME);
            WHERE("msg_id=#{msgId}");
        }}.toString();
    }

    public String update(){
        return new SQL(){{
            UPDATE(TABLE_NAME);
            SET("update_time = now()");
            SET("status = #{status}");
            SET("msg_type = #{msgType}");
            SET("to_user_name = #{toUserName}");
            SET("form_user_name = #{formUserName}");
            WHERE("msg_id=#{msgId}");
        }}.toString();
    }

}
