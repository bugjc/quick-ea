package com.bugjc.ea.jwt.web;


import com.bugjc.ea.jwt.core.dto.Result;
import com.bugjc.ea.jwt.core.dto.ResultGenerator;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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
    public Result error(HttpServletResponse response) {
        return ResultGenerator.genFailResult(response.getStatus(),"error");
    }
}
