package com.bugjc.ea.jwt.web;


import com.bugjc.ea.jwt.core.dto.Result;
import com.bugjc.ea.jwt.core.dto.ResultGenerator;
import com.bugjc.ea.jwt.core.exception.AuthenticationException;
import com.bugjc.ea.jwt.core.exception.BusinessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public Result authenticationExceptionHandler(AuthenticationException exception) throws Exception {
        this.handleErrorInfo(exception);
        return ResultGenerator.genFailResult(exception.getCode(),exception.getMessage());
    }


    private void handleErrorInfo(BusinessException ex) {
        //TODO，记录异常到数据库或日志
    }
}
