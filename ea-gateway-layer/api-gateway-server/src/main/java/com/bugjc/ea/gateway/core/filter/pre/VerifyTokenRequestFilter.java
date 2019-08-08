package com.bugjc.ea.gateway.core.filter.pre;

import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.gateway.core.api.JwtApiClient;
import com.bugjc.ea.gateway.core.dto.GatewayResultCode;
import com.bugjc.ea.gateway.service.AppSecurityConfigService;
import com.bugjc.ea.gateway.core.util.ResponseResultUtil;
import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.http.opensdk.core.dto.ResultCode;
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
		return !appExcludeSecurityAuthenticationPathService.excludeNeedAuthTokenPath(context.getRequest().getRequestURI()) && (boolean)context.get("isSuccess");
	}

	@Override
	public Object run() {
		log.info("filter:Token认证过滤器");
		RequestContext ctx = getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		try {

			String token = request.getHeader("Authorization");
			log.info("token:{}",token);
			if (StrUtil.isBlank(token)){
				ResponseResultUtil.genErrorResult(ctx, GatewayResultCode.TOKEN_MISSING.getCode(), "缺失Token参数！");
				return null;
			}

			String appId = request.getHeader("AppId");
			if (StrUtil.isBlank(appId)){
				ResponseResultUtil.genErrorResult(ctx, GatewayResultCode.APP_ID_MISSING.getCode(), "缺失AppId参数");
				return null;
			}

			//调用jwt 认证服务
			Result result = jwtApiClient.post(appId, JwtApiClient.ContentPath.VERIFY_TOKEN_PATH,null);
			if (result.getCode() == ResultCode.SUCCESS.getCode()) {
				ResponseResultUtil.genSuccessResult(ctx,"校验token成功!");
				return null;
			}

			ResponseResultUtil.genErrorResult(ctx, result.getCode(), result.getMessage());
			return null;

		}catch (Exception ex){
			log.error(ex.getMessage(),ex);
			ResponseResultUtil.genErrorResult(ctx, ResultCode.INTERNAL_SERVER_ERROR.getCode(), "服务器内部错误！");
			return null;
		}

	}
}
