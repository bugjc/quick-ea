package com.bugjc.ea.gateway.zuul.core.filter.pre;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.gateway.zuul.core.api.AuthApiClient;
import com.bugjc.ea.gateway.zuul.core.constants.ApiGatewayKeyConstants;
import com.bugjc.ea.gateway.zuul.core.dto.ApiGatewayServerResultCode;
import com.bugjc.ea.gateway.zuul.core.enums.ResultErrorEnum;
import com.bugjc.ea.gateway.zuul.core.util.FilterChainReturnResultUtil;
import com.bugjc.ea.gateway.zuul.service.AppSecurityConfigService;
import com.bugjc.ea.opensdk.http.core.constants.HttpHeaderKeyConstants;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.CommonResultCode;
import com.bugjc.ea.opensdk.http.model.auth.VerifyTokenBody;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * 平台接口授权认证
 * @author aoki
 */
@Slf4j
@Component
public class VerifyTokenRequestFilter extends ZuulFilter {

	@Resource
	private AuthApiClient authApiClient;

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
		return !appExcludeSecurityAuthenticationPathService.excludeNeedAuthTokenPath(context.getRequest().getRequestURI()) && (boolean)context.get(ApiGatewayKeyConstants.IS_SUCCESS);
	}

	@Override
	public Object run() {
		log.info("filter: 平台接口授权 Token认证过滤器");
		RequestContext ctx = getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		try {

			String token = request.getHeader(HttpHeaderKeyConstants.AUTHORIZATION);
			if (StrUtil.isBlank(token)){
				FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.TOKEN_MISSING.getCode(), "缺失Token参数！");
				return null;
			}

			String appId = request.getHeader(HttpHeaderKeyConstants.APP_ID);
			if (StrUtil.isBlank(appId)){
				FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.APP_ID_MISSING.getCode(), "缺失AppId参数");
				return null;
			}

			//调用授权认证服务
			Result result = authApiClient.verifyToken(appId,token);
			if (result.getCode() != CommonResultCode.SUCCESS.getCode()) {
				FilterChainReturnResultUtil.genErrorResult(ctx, ResultErrorEnum.VerifyTokenError.getCode(), result.getMessage());
				return null;
			}

			VerifyTokenBody.ResponseBody responseBody = ((JSONObject)result.getData()).toJavaObject(VerifyTokenBody.ResponseBody.class);
			if (responseBody.getFailCode() != 0){
				FilterChainReturnResultUtil.genErrorResult(ctx, ResultErrorEnum.VerifyTokenError.getCode(), "校验 token 失败");
				return null;
			}

			FilterChainReturnResultUtil.genSuccessResult(ctx, "校验 token 成功");
			return null;

		}catch (Exception ex){
			log.error(ex.getMessage(),ex);
			FilterChainReturnResultUtil.genErrorResult(ctx, CommonResultCode.INTERNAL_SERVER_ERROR.getCode(), "Token认证服务器内部错误！");
			return null;
		}

	}
}
