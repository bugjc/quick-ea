package com.bugjc.ea.gateway.zuul.core.filter.route;

import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.gateway.zuul.core.component.RibbonComponent;
import com.bugjc.ea.gateway.zuul.core.constants.ApiGatewayKeyConstants;
import com.bugjc.ea.gateway.zuul.service.ZuulRouteService;
import com.bugjc.ea.gateway.zuul.core.enums.ResultErrorEnum;
import com.bugjc.ea.gateway.zuul.core.util.FilterChainReturnResultUtil;
import com.bugjc.ea.opensdk.http.core.constants.HttpHeaderKeyConstants;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URL;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * 物理地址负载均衡过滤器
 * @author aoki
 */
@Slf4j
@Component
public class RibbonRouteFilter extends ZuulFilter {

	@Resource
	private ZuulRouteService zuulRouteService;

	@Resource
	private RibbonComponent ribbonComponent;

	@Override
	public String filterType() {
		return FilterConstants.ROUTE_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.RIBBON_ROUTING_FILTER_ORDER + 2;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext context = getCurrentContext();
		HttpServletRequest request = context.getRequest();
		String appId = request.getHeader(HttpHeaderKeyConstants.APP_ID);
		if (StrUtil.isBlank(appId)){
			return false;
		}

		//检查请求header中是否带有debug
		boolean isDebug = Boolean.parseBoolean(request.getHeader(HttpHeaderKeyConstants.IS_DEBUG));
		String uri = request.getRequestURI();
		return zuulRouteService.checkRouteMode(appId, uri, isDebug) && (boolean) context.get(ApiGatewayKeyConstants.IS_SUCCESS);
	}

	@Override
	public Object run() {

		log.info("filter:物理地址负载均衡过滤器");
		RequestContext context = getCurrentContext();
		//标记为自定义路由
		context.set(ApiGatewayKeyConstants.CUSTOM_ROUTE_TAG,true);
		HttpServletRequest request = context.getRequest();
		try {
			boolean isDebug = Boolean.parseBoolean(request.getHeader(HttpHeaderKeyConstants.IS_DEBUG));
			String appId = request.getHeader(HttpHeaderKeyConstants.APP_ID);
			String uri = request.getRequestURI();
			URL ribbonUrl = ribbonComponent.chooseServer(appId, uri, isDebug);
			if (ribbonUrl == null) {
				FilterChainReturnResultUtil.genErrorResult(context, ResultErrorEnum.ERROR_URI.getCode(), "还未配置物理地址");
				return null;
			}
			URL currentUrl = context.getRouteHost();
			String file = "";
			if (currentUrl != null) {
				file = currentUrl.getFile();
			}
			URL routeUrl = new URL(ribbonUrl.getProtocol(), ribbonUrl.getHost(), ribbonUrl.getPort(), file);

			context.setRouteHost(routeUrl);
			FilterChainReturnResultUtil.genSuccessResult(context, "加权轮询负载策略，当前路由地址:" + routeUrl.toString());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			FilterChainReturnResultUtil.genErrorResult(context, ResultErrorEnum.ERROR_URI.getCode(), "解析路由信息失败！!");
			return null;
		}
	}

}
