package com.bugjc.ea.gateway.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.gateway.config.GlobalProperty;
import com.bugjc.ea.gateway.core.dto.ResultGenerator;
import com.bugjc.ea.gateway.core.enums.ResultErrorEnum;
import com.bugjc.ea.gateway.core.util.IoUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Component
public class AuthorizationRequestFilter extends ZuulFilter {

	public final static Logger logger = LoggerFactory.getLogger(AuthorizationRequestFilter.class);

	@Resource
	private GlobalProperty globalProperty;

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		String httpMethod = "GET";
        if (httpMethod.equals(request.getMethod().toUpperCase())){
            //GET请求不进行签名校验
            return null;
        }

		String contentType = request.getHeader("Content-Type");
		if (contentType == null){
			genResult(ctx,401,"请正确设置编码类型为application/json");
			return null;
		}

		String contentTypeValue = "application/json";
		if (!contentType.startsWith(contentTypeValue)){
			genResult(ctx,401,"请正确设置编码类型为application/json");
			return null;
		}

		String sign = request.getHeader("Signature");
		logger.debug(sign);
		if (StrUtil.isEmpty(sign)){
			genResult(ctx,ResultErrorEnum.ParamError.getCode(),"签名参数不能为空");
			return null;
		}

		String body = null;
		Sign signed = SecureUtil.sign(SignAlgorithm.SHA1withRSA,null,globalProperty.rsaPublicKey);;
		boolean verify = false;

		try {
			request.setCharacterEncoding("UTF-8");
			ServletRequest requestWrapper = new MyHttpServletRequestWrapper(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(requestWrapper.getInputStream()));
			body = IoUtils.read(reader);
			if (StrUtil.isBlank(body)){
				genResult(ctx,ResultErrorEnum.ParamError.getCode(),"参数不能为空");
				return null;
			}

			logger.debug("请求路径为：" + request.getRequestURI());
			logger.debug("请求参数："+body);
			verify = signed.verify(body.getBytes(CharsetUtil.CHARSET_UTF_8), Base64.decode(sign));
		}catch (Exception e) {
			try {
                assert body != null;
                verify = signed.verify(body.getBytes(CharsetUtil.CHARSET_UTF_8), hexStringToByteArray(sign));
			}catch (Exception ex){
                e.printStackTrace();
			}
		}

		if (verify){
			logger.debug("验签成功！");
			return null;
		}else {
			genResult(ctx, ResultErrorEnum.ParamError.getCode(),"签名验证失败！");
			return null;
		}
	}

	private static byte[] hexStringToByteArray(String data) {
		int k = 0;
		byte[] results = new byte[data.length() / 2];
		int z = 2;
		for (int i = 0; i + 1 < data.length(); i += z, k++) {
			results[k] = (byte) (Character.digit(data.charAt(i), 16) << 4);
			results[k] += (byte) (Character.digit(data.charAt(i + 1), 16));
		}
		return results;
	}


	private static void genResult(RequestContext ctx,int status,String message){
		ctx.setSendZuulResponse(false);
		ctx.setResponseStatusCode(status);
		ctx.setResponseBody(JSON.toJSONString(ResultGenerator.genUnauthorizedResult(message)));
		ctx.getResponse().setContentType("application/json;charset=UTF-8");
	}

}
