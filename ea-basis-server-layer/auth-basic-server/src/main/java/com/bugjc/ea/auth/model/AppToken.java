package com.bugjc.ea.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;


/**
 * @author yangqing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppToken implements Serializable {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 运营商 ID
     */
    private String appId;

    /**
     * 全局唯一凭证
     */
    private String accessToken;

    /**
     * 凭证有效期，单位秒
     */
    private int tokenAvailableTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;
}