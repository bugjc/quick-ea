package com.bugjc.ea.gateway.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 路由类
 * @author aoki
 */
@Data
public class CustomZuulRoute implements Serializable {

    private static final long serialVersionUID = 2508535502571015009L;
    /**
     * 路由的ID（默认情况下与其映射键相同）。
     */
    private String id;

    /**
     * 路由的路径, 例： /foo/**.
     */
    private String path;

    /**
     * 要映射到此路由的服务ID，配合服务发现使用。如果没有可以指定物理URL或服务，但不能同时指定两者。
     */
    private String serviceId;

    /**
     * 要映射到路由的完整物理URL。, 另一种方法是使用服务ID 和服务发现来查找物理地址。
     */
    private String url;

    /**
     * 负载均衡的物理地址，多个用逗号分隔，未配置则默认使用url的地址。
     */
    private String ribbonUrl;

    /**
     * 标记以确定在转发之前是否应剥离此路由的前缀。例：/api/v1/member/1 --> /member/1
     */
    private boolean stripPrefix = true;

    /**
     * 用于指示此路由应该可重试的标志（如果支持）。 通常，重试需要服务ID和功能区。
     */
    private Boolean retryable;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否启用（1:启用，0禁用）
     */
    private Boolean enabled;


}
