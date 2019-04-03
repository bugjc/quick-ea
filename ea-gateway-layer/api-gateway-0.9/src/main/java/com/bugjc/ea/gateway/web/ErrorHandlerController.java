package com.bugjc.ea.gateway.web;

import com.bugjc.ea.gateway.core.dto.ResultCode;
import com.bugjc.ea.gateway.core.dto.ResultGenerator;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author aoki
 * @date ${date}
 */
@RestController
public class ErrorHandlerController implements ErrorController {
    /**
     * 出异常后进入该方法，交由下面的方法处理
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public Object error(HttpServletRequest request, HttpServletResponse response) {

        Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
        int notFound = 404;
        if (status == notFound) {
            return ResultGenerator.genFailResult("访问地址不存在");
        }

        RequestContext ctx = RequestContext.getCurrentContext();
        ZuulException exception = (ZuulException)ctx.getThrowable();
        System.out.println(exception.getMessage());
        System.out.println(exception.getMessage());

        return ResultGenerator.genFailResult(exception.nStatusCode,"内部服务器错误,正在处理");
    }
}