package com.glcxw.gateway.core.filter.route;

import cn.hutool.core.util.StrUtil;
import com.glcxw.gateway.core.component.RibbonComponent;
import com.glcxw.gateway.core.enums.ResultErrorEnum;
import com.glcxw.gateway.core.util.ResponseResultUtil;
import com.glcxw.gateway.service.ZuulRouteService;
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
		String appId = request.getHeader("AppId");
		if (StrUtil.isBlank(appId)){
			return false;
		}

		//检查请求header中是否带有debug
		boolean isDebug = Boolean.parseBoolean(request.getHeader("IsDebug"));
		String requestURI = request.getRequestURI();
		return zuulRouteService.checkRouteMode(appId, requestURI, isDebug) && (boolean) context.get("isSuccess");
	}

	@Override
	public Object run() {

		log.info("filter:物理地址负载均衡过滤器");
		RequestContext context = getCurrentContext();
		HttpServletRequest request = context.getRequest();
		try {
			boolean isDebug = Boolean.parseBoolean(request.getHeader("IsDebug"));
			String appId = request.getHeader("AppId");
			String requestURI = request.getRequestURI();
			URL ribbonUrl = ribbonComponent.chooseServer(appId, requestURI, isDebug);
			if (ribbonUrl == null) {
				ResponseResultUtil.genErrorResult(context, ResultErrorEnum.ERROR_URI.getCode(), "还未配置物理地址");
				return null;
			}
			URL currentUrl = context.getRouteHost();
			String file = "";
			if (currentUrl != null) {
				file = currentUrl.getFile();
			}
			URL routeUrl = new URL(ribbonUrl.getProtocol(), ribbonUrl.getHost(), ribbonUrl.getPort(), file);

			context.setRouteHost(routeUrl);
			ResponseResultUtil.genSuccessResult(context, "加权轮询负载策略，当前路由地址:" + routeUrl.toString());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			ResponseResultUtil.genErrorResult(context, ResultErrorEnum.ERROR_URI.getCode(), "解析路由信息失败！!");
			return null;
		}
	}

}
