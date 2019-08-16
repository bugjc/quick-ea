package com.bugjc.ea.gateway.core.constants;

/**
 * 充电业务常量
 * @author aoki
 */
public class AppTokenConstants {

    /**
     * token前缀
     */
    public static final String BEARER = "Bearer ";

    /**
     * 已销毁token生存时间
     */
    public static final int DESTROYED_TOKEN_AVAILABLE_TIME = -1;

    /**
     * 刷新token，旧token存活时间
     */
    public final static int TOKEN_TO_BE_DESTROYED_AVAILABLE_TIME = 600;

    /**
     * 当前最新token的存活时间
     */
    public final static int CURRENT_LATEST_TOKEN_AVAILABLE_TIME = 7200;

}
