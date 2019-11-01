package com.bugjc.ea.gateway.zuul.core.filter.post;

import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.gateway.zuul.core.constants.ApiGatewayKeyConstants;
import com.bugjc.ea.gateway.zuul.core.util.FilterChainReturnResultUtil;
import com.bugjc.ea.gateway.zuul.model.App;
import com.bugjc.ea.gateway.zuul.service.AppSecurityConfigService;
import com.bugjc.ea.gateway.zuul.service.AppService;
import com.bugjc.ea.opensdk.http.core.constants.HttpHeaderKeyConstants;
import com.bugjc.ea.opensdk.http.core.crypto.CryptoProcessor;
import com.bugjc.ea.opensdk.http.core.crypto.input.ServicePartyEncryptParam;
import com.bugjc.ea.opensdk.http.core.crypto.output.ServicePartyEncryptObj;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
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
    private AppSecurityConfigService appExcludeSecurityAuthenticationPathService;

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
    public Object run() {
        try {
            log.info("filter:生成应答签名过滤器");
            RequestContext context = getCurrentContext();
            HttpServletRequest request = context.getRequest();

            String appId = request.getHeader(HttpHeaderKeyConstants.APP_ID);
            if (StrUtil.isBlank(appId)){
                //针对回调类请求
                FilterChainReturnResultUtil.genSuccessResult(context, "无需签名");
                return null;
            }

            InputStream stream = context.getResponseDataStream();
            String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            if (StrUtil.isBlank(body) && !(boolean)context.get(ApiGatewayKeyConstants.IS_SUCCESS)){
                body = context.getResponseBody();
                log.info("过滤器应答结果：{}", body);
            }else{
                log.info("服务应答结果：{}", body);
            }


            App app = appService.findByAppId(appId);
            ServicePartyEncryptParam servicePartyEncryptParam = new ServicePartyEncryptParam();
            servicePartyEncryptParam.setAppId(appId);
            servicePartyEncryptParam.setBody(body);
            servicePartyEncryptParam.setRsaPrivateKey(app.getRsaPrivateKey());
            servicePartyEncryptParam.setSequence(request.getHeader(HttpHeaderKeyConstants.SEQUENCE));
            ServicePartyEncryptObj servicePartyEncryptObj = new CryptoProcessor().servicePartyEncrypt(servicePartyEncryptParam);
            //设置加密body
            context.setResponseBody(servicePartyEncryptObj.getBody());

            HttpServletResponse response = context.getResponse();
            response.setHeader(HttpHeaderKeyConstants.SEQUENCE, servicePartyEncryptObj.getSequence());
            response.setHeader(HttpHeaderKeyConstants.TIMESTAMP, servicePartyEncryptObj.getTimestamp());
            response.setHeader(HttpHeaderKeyConstants.NONCE, servicePartyEncryptObj.getNonce());
            response.setHeader(HttpHeaderKeyConstants.SIGNATURE, servicePartyEncryptObj.getSignature());
            context.setResponse(response);
            log.info("生成应答签名成功！");
        } catch (IOException e) {
            rethrowRuntimeException(e);
        }
        return null;
    }
}
