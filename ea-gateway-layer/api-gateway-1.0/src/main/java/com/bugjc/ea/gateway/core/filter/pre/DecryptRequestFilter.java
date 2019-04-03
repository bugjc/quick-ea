package com.bugjc.ea.gateway.core.filter.pre;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.gateway.core.enums.ResultErrorEnum;
import com.bugjc.ea.gateway.core.util.*;
import com.glcxw.gateway.core.util.*;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * 解密消息
 * @author aoki
 */
@Slf4j
@Component
public class DecryptRequestFilter extends ZuulFilter {

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.PRE_DECORATION_FILTER_ORDER + 10;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext context = getCurrentContext();
		String requestURI = context.getRequest().getRequestURI();
		return requestURI.startsWith("/sass/card/apply") && (boolean) context.get("isSuccess");
	}

	@Override
	public Object run() {
		log.info("filter:请求消息体解密过滤器");
		RequestContext ctx = getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		String random = (String) ctx.get("RandomKey");
		if (StrUtil.isBlank(random)){
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.ParamHeaderError.getCode(), "加密随机码不能为空");
			return null;
		}

		String requestURI = request.getRequestURI();

		try {
			ServletRequest requestWrapper = new MyHttpServletRequestWrapper(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(requestWrapper.getInputStream()));
			String body = IoUtils.read(reader);
			String decryptStr = ThreeDESUtil.decryptThreeDESECB(random,Base64.decodeStr(body));
            ctx.setRequest(new OverwriteHttpServletRequestWrapper(decryptStr.getBytes(StandardCharsets.UTF_8),request));
			ResponseResultUtil.genSuccessResult(ctx, "解密成功,明文:" + decryptStr);
            return null;
		} catch (Exception e) {
			log.info(e.getMessage(),e);
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.DECRYPT_ERROR.getCode(), "解密失败:" + requestURI);
			return null;
		}
	}

}
