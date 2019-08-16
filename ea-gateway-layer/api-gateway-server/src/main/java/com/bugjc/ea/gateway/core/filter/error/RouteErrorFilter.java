package com.bugjc.ea.gateway.core.filter.error;

import com.bugjc.ea.gateway.core.component.RibbonComponent;
import com.bugjc.ea.gateway.core.constants.ApiGatewayKeyConstants;
import com.bugjc.ea.gateway.core.dto.ApiGatewayServerResultCode;
import com.bugjc.ea.gateway.core.util.FilterChainReturnResultUtil;
import com.bugjc.ea.http.opensdk.core.constants.HttpHeaderKeyConstants;
import com.bugjc.ea.http.opensdk.core.dto.ResultCode;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class RouteErrorFilter extends SendErrorFilter {

    @Resource
    private RibbonComponent ribbonComponent;

    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_ERROR_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        // 判断：仅处理来自route过滤器引起的异常
        RequestContext ctx = RequestContext.getCurrentContext();
        ZuulFilter failedFilter = (ZuulFilter) ctx.get(ApiGatewayKeyConstants.FAILED_FILTER);
        return failedFilter != null && failedFilter.filterType().equals(FilterConstants.ROUTE_TYPE) ;
                //&& ctx.getBoolean(ApiGatewayKeyConstants.CUSTOM_ROUTE_TAG);
    }

    @Override
    public Object run() {
        log.info("filter:路由失败处理过滤器");
        RequestContext ctx = RequestContext.getCurrentContext();
        try{
            HttpServletRequest request = ctx.getRequest();
            //直接复用异常处理类
            if (ctx.getThrowable().getCause().getCause().getCause() instanceof HttpHostConnectException) {
                //生成唯一服务key
                String appId = request.getHeader(HttpHeaderKeyConstants.APP_ID);
                String uri = request.getRequestURI();
                String url = String.valueOf(ctx.get(ApiGatewayKeyConstants.PHYSICAL_ROUTING_ADDRESS));
                //移除无效的服务
                ribbonComponent.markServerDown(appId, uri, url);
            }

            FilterChainReturnResultUtil.genErrorResult(ctx, ResultCode.INTERNAL_SERVER_ERROR.getCode(), ctx.getThrowable().getMessage());
            return null;
        }catch (Exception ex) {
            FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.ROUTE_FAILURE.getCode(), ApiGatewayServerResultCode.ROUTE_FAILURE.getMessage()+":"+ex.getMessage());
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }
}
