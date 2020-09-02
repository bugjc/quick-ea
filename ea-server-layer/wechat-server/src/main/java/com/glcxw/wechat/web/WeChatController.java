package com.glcxw.wechat.web;

import cn.hutool.core.util.CharsetUtil;
import com.glcxw.wechat.core.util.CheckoutUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author aoki
 */
@RequestMapping
@Controller
@Slf4j
public class WeChatController {

    @Resource
    private WxMpService wxMpService;

    /**
     * 微信接口配置安全认证
     *
     * @param request
     * @param response
     */
    @GetMapping("/tokenVerify")
    public void tokenVerify(HttpServletRequest request, HttpServletResponse response) {
        boolean isGet = "get".equals(request.getMethod().toLowerCase());
        PrintWriter print;
        if (isGet) {
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echoStr = request.getParameter("echostr");
            //token
            String token = "Aoki";
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (signature != null && CheckoutUtil.checkSignature(token, signature, timestamp, nonce)) {
                try {
                    print = response.getWriter();
                    print.write(echoStr);
                    print.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 网页授权接口
     *
     * @param returnUrl
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) throws UnsupportedEncodingException {
        String url = "http://wechat.ngrok.bugjc.com/wechat/userInfo";
        String redirectURL = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl, CharsetUtil.UTF_8));
        log.info("【微信网页授权】获取code,redirectURL={}", redirectURL);
        return "redirect:" + redirectURL;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) throws Exception {

        WxMpOAuth2AccessToken wxMpOAuth2AccessToken;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.info("【微信网页授权】{}", e);
            throw new Exception(e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("【微信网页授权】openId={}", openId);
        return "redirect:" + returnUrl + "?openid=" + openId;
    }
}
