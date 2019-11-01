package com.bugjc.ea.gateway.zuul.core.filter.pre;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.gateway.zuul.core.dto.ApiGatewayServerResultCode;
import com.bugjc.ea.gateway.zuul.core.util.IoUtils;
import com.bugjc.ea.gateway.zuul.core.util.MyHttpServletRequestWrapper;
import com.bugjc.ea.gateway.zuul.core.util.FilterChainReturnResultUtil;
import com.bugjc.ea.gateway.zuul.core.util.SequenceLimitUtil;
import com.bugjc.ea.gateway.zuul.model.App;
import com.bugjc.ea.gateway.zuul.service.AppSecurityConfigService;
import com.bugjc.ea.gateway.zuul.service.AppService;
import com.bugjc.ea.opensdk.http.core.constants.HttpHeaderKeyConstants;
import com.bugjc.ea.opensdk.http.core.crypto.CryptoProcessor;
import com.bugjc.ea.opensdk.http.core.crypto.input.ServicePartyDecryptParam;
import com.bugjc.ea.opensdk.http.core.crypto.output.ServicePartyDecryptObj;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * 授权认证
 * @author aoki
 */
@Slf4j
@Component
public class AuthorizationRequestFilter extends ZuulFilter {

    @Resource
    private SequenceLimitUtil sequenceLimitUtil;

	@Resource
	private AppService appService;

	@Resource
	private AppSecurityConfigService appExcludeSecurityAuthenticationPathService;

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext context = getCurrentContext();
		String requestUrl = context.getRequest().getRequestURI();
		return appExcludeSecurityAuthenticationPathService.excludeNeedSignaturePath(requestUrl)
				&& appExcludeSecurityAuthenticationPathService.checkSignatureVersion(requestUrl) == 1;
	}

	@Override
	public Object run() {

		log.info("filter:签名认证过滤器");
		RequestContext ctx = getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		String contentType = request.getHeader(HttpHeaderKeyConstants.CONTENT_TYPE);
		log.debug("接口请求的参数类型:{}",contentType);

		String appId = request.getHeader(HttpHeaderKeyConstants.APP_ID);
		if (StrUtil.isBlank(appId)){
			FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.APP_ID_MISSING.getCode(), "缺失AppId参数");
			return null;
		}

        String sequence = request.getHeader(HttpHeaderKeyConstants.SEQUENCE);
		if (StrUtil.isBlank(sequence)){
			FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.SEQUENCE_MISSING.getCode(), "缺失Sequence参数");
			return null;
		}

		String signValue = request.getHeader(HttpHeaderKeyConstants.SIGNATURE);
		if (StrUtil.isBlank(signValue)){
			FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.SIGNATURE_MISSING.getCode(), "缺失Signature参数");
			return null;
		}

        //重放控制
        if (sequenceLimitUtil.limit(sequence)) {
			FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.REQUEST_REPLAY_LIMIT.getCode(), "请求不能重放");
            return null;
        }

		String body = null;

        try {
			request.setCharacterEncoding(CharsetUtil.UTF_8);
			ServletRequest requestWrapper = new MyHttpServletRequestWrapper(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(requestWrapper.getInputStream()));
			body = IoUtils.read(reader);
			if (StrUtil.isBlank(body)){
				FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.BUSINESS_PARAM_MISSING.getCode(), "缺失业务参数，如没有业务参数请传空的JSON字符串");
				return null;
			}
		} catch (IOException e) {
			FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.IO_ERROR.getCode(), "读取流数据错误!");
			return null;
		}

		App app = appService.findByAppId(appId);
		if (app == null){
			FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.INVALID_APP_ID.getCode(), "不合法的AppId"+ appId);
			return null;
		}

		if (app.getRsaPublicKey() == null){
			FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.NOT_CONFIG_RSA.getCode(), "未配置RSA安全密钥对");
			return null;
		}

		//服务方安全解密
		ServicePartyDecryptParam servicePartyDecryptParam = new ServicePartyDecryptParam();
		servicePartyDecryptParam.setAppId(appId);
		servicePartyDecryptParam.setBody(body);
		servicePartyDecryptParam.setNonce(request.getHeader(HttpHeaderKeyConstants.NONCE));
		servicePartyDecryptParam.setRsaPublicKey(app.getRsaPublicKey());
		servicePartyDecryptParam.setSequence(sequence);
		servicePartyDecryptParam.setSignature(signValue);
		servicePartyDecryptParam.setTimestamp(request.getHeader(HttpHeaderKeyConstants.TIMESTAMP));

		ServicePartyDecryptObj servicePartyDecryptObj = new CryptoProcessor().servicePartyDecrypt(servicePartyDecryptParam);
		if (!servicePartyDecryptObj.isSignSuccessful()){
			FilterChainReturnResultUtil.genErrorResult(ctx, ApiGatewayServerResultCode.SIGNATURE_ERROR.getCode(), "验签失败!");
			return null;
		}

		FilterChainReturnResultUtil.genSuccessResult(ctx, "验签成功!");
		return null;

	}

}
