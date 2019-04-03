package com.bugjc.ea.gateway.core.filter.post;

import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.gateway.core.enums.ResultErrorEnum;
import com.bugjc.ea.gateway.core.util.ResponseResultUtil;
import com.bugjc.ea.gateway.core.util.ThreeDESUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.charset.Charset;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * 加密消息
 * @author aoki
 */
@Slf4j
@Component
public class EncryptResponseFilter extends ZuulFilter {

	@Override
	public String filterType() {
		return FilterConstants.POST_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.SEND_FORWARD_FILTER_ORDER + 1;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext context = getCurrentContext();
		String requestURI = context.getRequest().getRequestURI();
		return requestURI.startsWith("/sass/co/bank/card/info/11");
	}

	@Override
	public Object run() {
		log.info("filter:应答消息体加密过滤器");
		RequestContext ctx = getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		String random = (String) ctx.get("RandomKey");
		if (StrUtil.isBlank(random)){
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.ParamHeaderError.getCode(), "加密随机码不能为空");
			return null;
		}

		String requestURI = request.getRequestURI();

		try {
			InputStream stream = ctx.getResponseDataStream();
			String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
			if (StrUtil.isBlank(body)){
				ResponseResultUtil.genSuccessResult(ctx, "无需处理");
				return null;
			}
			String encryptStr = ThreeDESUtil.encryptThreeDESECB(random,body);
			ResponseResultUtil.genSuccessResult(ctx, "加密成功", encryptStr);
            return null;
		} catch (Exception e) {
			log.info(e.getMessage(),e);
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.DECRYPT_ERROR.getCode(), "解密失败:" + requestURI);
			return null;
		}
	}

}
