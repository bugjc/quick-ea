package com.glcxw.gateway.core.filter.pre;

import com.glcxw.gateway.core.enums.ResultErrorEnum;
import com.glcxw.gateway.core.util.ResponseResultUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import java.time.LocalTime;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * 暂不启用,后续做成配置的形式，且针对具体的应用设置维护时间表
 * @author aoki
 */
public class AccessTimeFilter extends ZuulFilter{
    /**
     * 当前时间
     */
    private static final LocalTime NOW = LocalTime.now();

    /**
     * 零点
     */
    private static final LocalTime ZERO_CLOCK = LocalTime.of(0, 0);

    /**
     * 二十点
     */
    private static final LocalTime TWENTY_CLOCK = LocalTime.of(20, 0);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 5;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        if (NOW.isAfter(ZERO_CLOCK) && NOW.isBefore(TWENTY_CLOCK)) {
            // 如果用户在0-20点之间访问了系统
            RequestContext ctx = getCurrentContext();
            ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.ParamHeaderError.getCode(), "系统维护中...");
            return null;
        }
        return null;
    }
}
