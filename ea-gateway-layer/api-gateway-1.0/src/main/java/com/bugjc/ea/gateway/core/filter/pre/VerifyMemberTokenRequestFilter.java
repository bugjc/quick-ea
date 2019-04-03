package com.bugjc.ea.gateway.core.filter.pre;

import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.gateway.core.enums.ResultErrorEnum;
import com.bugjc.ea.gateway.core.util.ThreeDESUtil;
import com.bugjc.ea.gateway.service.AppConfigService;
import com.bugjc.ea.gateway.core.api.MemberApiClient;
import com.bugjc.ea.gateway.core.dto.Result;
import com.bugjc.ea.gateway.core.dto.ResultCode;
import com.bugjc.ea.gateway.core.util.ResponseResultUtil;
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
public class VerifyMemberTokenRequestFilter extends ZuulFilter {

	@Resource
	private MemberApiClient memberApiClient;

	@Resource
	private AppConfigService appExcludeSecurityAuthenticationPathService;

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

		String token = request.getHeader("Token");
		log.info("token:{}",token);
		if (StrUtil.isBlank(token)){
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.ParamHeaderError.getCode(), "请求头需要传递Token参数！");
			return null;
		}


		String random = String.valueOf(ctx.get("RandomKey"));
		if (StrUtil.isBlank(random)){
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.ParamHeaderError.getCode(), "加密随机码不能为空");
			return null;
		}

		try {
			token = ThreeDESUtil.decryptThreeDESECB(random,token);
            String[] memberTokenParam = token.split(",");
			ctx.addZuulRequestHeader("MemberID",memberTokenParam[0]);
			Result result = memberApiClient.checkToken(memberTokenParam[0], memberTokenParam[1]);
			if (result.getCode() == ResultCode.SUCCESS.getCode()) {
				log.info("校验token成功!");
				return null;
			}


			if (result.getCode() == ResultCode.TOKEN_EXPIRE.getCode()) {
				log.error("Token已过期！");
				ResponseResultUtil.genErrorResult(ctx, result.getCode(), "Token已过期！");
				return null;
			}

			log.error("校验token失败！");
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.VerifyTokenError.getCode(), "校验token失败！");
			return null;

		}catch (Exception ex){
			log.error(ex.getMessage(),ex);
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.VerifyTokenError.getCode(), "校验token失败！");
			return null;
		}

	}
}
