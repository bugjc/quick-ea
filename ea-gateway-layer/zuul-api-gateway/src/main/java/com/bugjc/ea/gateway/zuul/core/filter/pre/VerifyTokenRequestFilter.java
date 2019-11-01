package com.bugjc.ea.gateway.zuul.core.filter.pre;

import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.gateway.zuul.core.api.JwtApiClient;
import com.bugjc.ea.gateway.zuul.core.dto.ApiGatewayServerResultCode;
import com.bugjc.ea.gateway.zuul.service.AppSecurityConfigService;
import com.bugjc.ea.gateway.zuul.core.util.FilterChainReturnResultUtil;
import com.bugjc.ea.opensdk.http.core.constants.HttpHeaderKeyConstants;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.ResultCode;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * 核查会员token
 * @author aoki
 */
@Slf4j
@Component
public class VerifyTokenRequestFilter extends ZuulFilter {

	@Resource
	private JwtApiClient jwtApiClient;

	@Resource
	private AppSecurityConfigService appExcludeSecurityAuthenticationPathService;

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.PRE_DECORATION_FILTER_ORDER + 6;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext context = getCurrentContext();
		//TODO 用户TOKEN认证
		//return !appExcludeSecurityAuthenticationPathService.excludeNeedAuthTokenPath(context.getRequest().getRequestURI()) && (boolean)context.get(ApiGatewayKeyConstants.IS_SUCCESS);
		return false;
	}

	@Override
	public Object run() {
		log.info("filter:Token认证过滤器");
		RequestContext ctx = getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		try {

			String token = request.getHeader(HttpHeaderKeyConstants.AUTHORIZATION);
			log.info("token:{}",token);
			if (StrUtil.isBlank(token)){
				FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.TOKEN_MISSING.getCode(), "缺失Token参数！");
				return null;
			}

			String appId = request.getHeader(HttpHeaderKeyConstants.APP_ID);
			if (StrUtil.isBlank(appId)){
				FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.APP_ID_MISSING.getCode(), "缺失AppId参数");
				return null;
			}

			//调用jwt 认证服务
			Result result = jwtApiClient.post(appId, JwtApiClient.ContentPath.VERIFY_TOKEN_PATH,token,null);
			if (result.getCode() == ResultCode.SUCCESS.getCode()) {
				FilterChainReturnResultUtil.genSuccessResult(ctx,"校验token成功!");
				return null;
			}

			FilterChainReturnResultUtil.genErrorResult(ctx, result.getCode(), result.getMessage());
			return null;

		}catch (Exception ex){
			log.error(ex.getMessage(),ex);
			FilterChainReturnResultUtil.genErrorResult(ctx, ResultCode.INTERNAL_SERVER_ERROR.getCode(), "Token认证服务器内部错误！");
			return null;
		}

	}
}
