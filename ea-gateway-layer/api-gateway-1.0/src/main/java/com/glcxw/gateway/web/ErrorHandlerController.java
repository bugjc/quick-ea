package com.glcxw.gateway.web;

import com.glcxw.gateway.core.dto.Result;
import com.glcxw.gateway.core.dto.ResultGenerator;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author aoki
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
    public Result error(HttpServletRequest request) {
        Integer code = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        String message = "服务器内部错误。";
        if (exception instanceof ZuulException) {
            message = exception.getMessage();
        }
        return ResultGenerator.genFailResult(code, message);
    }
}
