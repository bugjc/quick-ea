package com.bugjc.ea.gateway.core.constants;

/**
 * @author aoki
 */
public interface ApiGatewayKeyConstants {
    /**
     * 自定义路由标记
     */
    String CUSTOM_ROUTE_TAG = "custom_route_tag";

    /**
     * 物理路由地址
     */
    String PHYSICAL_ROUTING_ADDRESS = "physical_routing_address";

    /**
     * 过滤链是否执行成功
     */
    String IS_SUCCESS =  "isSuccess";

    /**
     * 执行错误的过滤器
     */
    String FAILED_FILTER = "failed.filter";
}
