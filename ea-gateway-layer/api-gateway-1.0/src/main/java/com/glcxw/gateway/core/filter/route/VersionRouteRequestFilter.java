//package com.glcxw.gateway.core.filter.route;
//
//import cn.hutool.core.util.HashUtil;
//import cn.hutool.core.util.StrUtil;
//import com.glcxw.gateway.core.util.ResponseResultUtil;
//import com.glcxw.gateway.model.AppVersionMap;
//import com.glcxw.gateway.service.AppVersionMapService;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//import static com.netflix.zuul.context.RequestContext.getCurrentContext;
//
///**
// * 授权认证
// * @author aoki
// */
//@Slf4j
//@Component
//public class VersionRouteRequestFilter extends ZuulFilter {
//
//
//	@Resource
//	private AppVersionMapService appVersionMapService;
//
//	@Override
//	public String filterType() {
//		return FilterConstants.ROUTE_TYPE;
//	}
//
//	@Override
//	public int filterOrder() {
//		return FilterConstants.RIBBON_ROUTING_FILTER_ORDER + 1;
//	}
//
//	@Override
//	public boolean shouldFilter() {
//		RequestContext context = getCurrentContext();
//		String version = context.getRequest().getHeader("Version");
//		if (StrUtil.isBlank(version)){
//			return false;
//		}
//		//版本2.0以上的执行路由策略
//		return HashUtil.bkdrHash(version) >= 864124 && (boolean)context.get("isSuccess");
//	}
//
//	@Override
//	public Object run() {
//		log.info("filter:版本路由过滤器");
//		RequestContext ctx = getCurrentContext();
//		HttpServletRequest request = ctx.getRequest();
//		String version = request.getHeader("Version");
//		String appId = request.getHeader("AppId");
//		String requestURI = request.getRequestURI();
//		List<AppVersionMap> appVersionMaps = appVersionMapService.findByAppIdAndVersionNo(appId,version);
//		if (appVersionMaps == null || appVersionMaps.isEmpty()){
//			ResponseResultUtil.genSuccessResult(ctx,"该路径还没有配置新版本或未配置");
//			return null;
//		}
//
//		for (AppVersionMap appVersionMap : appVersionMaps) {
//			if (requestURI.startsWith(appVersionMap.getPath())){
//				requestURI = requestURI.replace(appVersionMap.getPath(),appVersionMap.getMapPath());
//				ctx.set("requestURI", requestURI);
//				ResponseResultUtil.genSuccessResult(ctx,"已重写的路由路径:"+requestURI);
//				return null;
//			}
//		}
//
//		ResponseResultUtil.genSuccessResult(ctx,"该路径未配置,无需重写路由:"+requestURI);
//		return null;
//	}
//
//}
