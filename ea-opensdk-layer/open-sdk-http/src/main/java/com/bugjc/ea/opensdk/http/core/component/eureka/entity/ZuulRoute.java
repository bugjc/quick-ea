package com.bugjc.ea.opensdk.http.core.component.eureka.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 路由信息
 * @author aoki
 * @date 2019/11/26
 * **/
@Data
public class ZuulRoute implements Serializable {

    /**
     * 服务ID
     */
    private String serviceId;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 标记以确定在转发之前是否应剥离此路由的前缀。例：/api/v1/member/1 --> /member/1
     */
    private boolean stripPrefix;
}
