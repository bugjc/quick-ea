package com.glcxw.wechat.web;

import com.glcxw.wechat.core.dto.Result;
import com.glcxw.wechat.core.dto.ResultGenerator;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result error() {
        return ResultGenerator.genFailResult("error");
    }
}
