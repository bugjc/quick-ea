package com.bugjc.ea.gateway.core.filter.pre;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.bugjc.ea.gateway.core.dto.GatewayResultCode;
import com.bugjc.ea.gateway.core.util.*;
import com.bugjc.ea.gateway.model.App;
import com.bugjc.ea.gateway.service.AppSecurityConfigService;
import com.bugjc.ea.gateway.service.AppService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
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

		String contentType = request.getHeader("Content-Type");
		log.debug("接口请求的参数类型:{}",contentType);

		String appId = request.getHeader("AppId");
		if (StrUtil.isBlank(appId)){
			ResponseResultUtil.genErrorResult(ctx, GatewayResultCode.APP_ID_MISSING.getCode(), "缺失AppId参数");
			return null;
		}

        String sequence = request.getHeader("Sequence");
		if (StrUtil.isBlank(sequence)){
			ResponseResultUtil.genErrorResult(ctx, GatewayResultCode.SEQUENCE_MISSING.getCode(), "缺失Sequence参数");
			return null;
		}

		String signValue = request.getHeader("Signature");
		if (StrUtil.isBlank(signValue)){
			ResponseResultUtil.genErrorResult(ctx, GatewayResultCode.SIGNATURE_MISSING.getCode(), "缺失Signature参数");
			return null;
		}

        //重放控制
        if (sequenceLimitUtil.limit(sequence)) {
			ResponseResultUtil.genErrorResult(ctx, GatewayResultCode.REQUEST_REPLAY_LIMIT.getCode(), "请求不能重放");
            return null;
        }

		String body = null;
		Sign signed = null;
		boolean verify = false;

		try {
			request.setCharacterEncoding("UTF-8");
			ServletRequest requestWrapper = new MyHttpServletRequestWrapper(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(requestWrapper.getInputStream()));
			body = IoUtils.read(reader);
			if (StrUtil.isBlank(body)){
				ResponseResultUtil.genErrorResult(ctx, GatewayResultCode.BUSINESS_PARAM_MISSING.getCode(), "缺失业务参数，如没有业务参数请传空的JSON字符串");
				return null;
			}

			App app = appService.findByAppId(appId);
			if (app == null){
				ResponseResultUtil.genErrorResult(ctx, GatewayResultCode.INVALID_APP_ID.getCode(), "不合法的AppId"+ appId);
				return null;
			}

			if (app.getRsaPublicKey() == null){
				ResponseResultUtil.genErrorResult(ctx, GatewayResultCode.NOT_CONFIG_RSA.getCode(), "未配置RSA安全密钥对");
				return null;
			}

			signed = SecureUtil.sign(SignAlgorithm.SHA1withRSA,null,app.getRsaPublicKey());
			String nonce = request.getHeader("Nonce");
			String timestampStr = request.getHeader("Timestamp");
			String responseSign = "appid="+appId+"&message="+ StrSortUtil.sortString(body)+"&nonce="+nonce+"&timestamp="+timestampStr+"&Sequence="+sequence;
			log.info("待签名字符串:{}",responseSign);
			verify = signed.verify(responseSign.getBytes(CharsetUtil.CHARSET_UTF_8), Base64.decode(signValue));
			if (!verify){
				throw new Exception("签名验证失败！");
			}

		}catch (Exception ex){
			log.error(ex.getMessage(),ex);
			try {
				assert body != null;
				assert signed != null;
				verify = signed.verify(body.getBytes(CharsetUtil.CHARSET_UTF_8), HexUtil.decodeHex(signValue));
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		if (verify){
			ResponseResultUtil.genSuccessResult(ctx, "验签成功!");
			return null;
		}else {
			ResponseResultUtil.genErrorResult(ctx, GatewayResultCode.SIGNATURE_ERROR.getCode(), "验签失败！");
			return null;
		}
	}

}
