package com.bugjc.ea.gateway.core.filter.post;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.bugjc.ea.gateway.model.App;
import com.bugjc.ea.gateway.service.AppConfigService;
import com.bugjc.ea.gateway.service.AppService;
import com.bugjc.ea.gateway.core.util.ResponseResultUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

/**
 * @author aoki
 */
@Slf4j
@Component
public class AuthorizationResponseFilter extends ZuulFilter {

    @Resource
    private AppService appService;
    @Resource
    private AppConfigService appExcludeSecurityAuthenticationPathService;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_FORWARD_FILTER_ORDER + 2;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = getCurrentContext();
        return appExcludeSecurityAuthenticationPathService.excludeNeedSignaturePath(context.getRequest().getRequestURI());
    }

    @Override
    public Object run() throws ZuulException {
        try {
            log.info("filter:生成应答签名过滤器");
            RequestContext context = getCurrentContext();
            InputStream stream = context.getResponseDataStream();
            String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            if (StrUtil.isBlank(body)){
                ResponseResultUtil.genSuccessResult(context, "无需处理");
                return null;
            }
            log.info("应答结果：{}", body);
            context.setResponseBody(body);
            //生成签名
            String timestamp = Long.parseLong(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())) + "";
            String nonce = RandomUtil.randomNumbers(10);
            /**序列化参数**/
            HttpServletRequest request = context.getRequest();
            String appId = request.getHeader("AppId");
            App app = appService.findByAppId(appId);
            String sequence = request.getHeader("Sequence");
            String requestSign = "appid=" + appId + "&message=" + body + "&nonce=" + nonce + "&timestamp=" + timestamp;
            //log.info("待签名字符串:{}",requestSign);
            /**生成签名**/
            Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA, app.getRsaPrivateKey(), null);
            byte[] signed = sign.sign(requestSign.getBytes(CharsetUtil.CHARSET_UTF_8));
            HttpServletResponse response = context.getResponse();
            response.setHeader("Sequence",sequence);
            response.setHeader("Timestamp",timestamp);
            response.setHeader("Nonce",nonce);
            response.setHeader("Signature",Base64.encode(signed));
            log.info("生成应答签名成功！");
        } catch (IOException e) {
            rethrowRuntimeException(e);
        }
        return null;
    }
}
