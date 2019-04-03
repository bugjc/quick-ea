package com.glcxw.gateway.core.filter.pre;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.glcxw.gateway.core.enums.ResultErrorEnum;
import com.glcxw.gateway.core.util.IoUtils;
import com.glcxw.gateway.core.util.MyHttpServletRequestWrapper;
import com.glcxw.gateway.core.util.ResponseResultUtil;
import com.glcxw.gateway.core.util.SequenceLimitUtil;
import com.glcxw.gateway.model.App;
import com.glcxw.gateway.service.AppExcludeSecurityAuthenticationPathService;
import com.glcxw.gateway.service.AppService;
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
	private AppExcludeSecurityAuthenticationPathService appExcludeSecurityAuthenticationPathService;

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
		if (contentType == null){
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.ParamHeaderError.getCode(), "①Content-Type需设置为application/json");
			return null;
		}

		String contentTypeValue = "application/json";
		if (!contentType.trim().startsWith(contentTypeValue)){
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.ParamHeaderError.getCode(), "②Content-Type需设置为application/json");
			return null;
		}

        //获取请求流水号
        String sequence = request.getHeader("Sequence");
		if (StrUtil.isBlank(sequence)){
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.ParamHeaderError.getCode(), "请求流水号参数不能为空");
			return null;
		}

		//加密随机码
		String random = request.getHeader("Random");
		if (StrUtil.isBlank(random)){
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.ParamHeaderError.getCode(), "加密随机码不能为空");
			return null;
		}

        //重放限制
        if (sequenceLimitUtil.limit(sequence)) {
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.LOGIN_REPLAY_COUNT_OVER_LIMIT.getCode(), ResultErrorEnum.LOGIN_REPLAY_COUNT_OVER_LIMIT.getMsg());
            return null;
        }

		String signValue = request.getHeader("Signature");
		if (StrUtil.isEmpty(signValue)){
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.ParamHeaderError.getCode(), "签名参数不能为空");
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
				ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.ParamBodyError.getCode(), "参数不能为空");
				return null;
			}

			/**验签**/
			String appId = request.getHeader("AppId");
			App app = appService.findByAppId(appId);
			if (app == null){
				log.info("无效的appId：{}",appId);
				ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.ParamHeaderError.getCode(), "无效的appId" + appId);
				return null;
			}

			if (app.getRsaPublicKey() == null){
				log.info("appId：{},还未配置公钥",appId);
				ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.ParamHeaderError.getCode(), "appId：" + appId + ",还未配置公钥");
				return null;
			}

			//解密随机key
			RSA rsa = SecureUtil.rsa(app.getRsaPrivateKey(),null);
			byte[] decrypt = rsa.decrypt(Base64.decode(random,CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
			String randomKey = StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
			ctx.set("RandomKey",randomKey);
			ctx.addZuulRequestHeader("Random",randomKey);

			signed = SecureUtil.sign(SignAlgorithm.SHA1withRSA,null,app.getRsaPublicKey());
			String nonce = request.getHeader("Nonce");
			String timestampStr = request.getHeader("Timestamp");
			String responseSign = "appid="+appId+"&message="+body+"&nonce="+nonce+"&timestamp="+timestampStr+"&Sequence="+sequence+"&Random="+random;
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
			log.info("请求内容验签成功！");
			ResponseResultUtil.genSuccessResult(ctx, "签名成功!");
			return null;
		}else {
			log.info("请求内容验签失败！");
			ResponseResultUtil.genErrorResult(ctx, ResultErrorEnum.VerifySignError.getCode(), "签名验证失败！");
			return null;
		}
	}

}
