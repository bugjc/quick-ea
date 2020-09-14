package com.bugjc.ea.code.generator.core.exceptions;

/**
 * DAO 异常类
 * @author aoki
 * @date 2020/9/3
 * **/
public class DaoException extends RuntimeException {
    /**
     * 异常状态码
     */
    private int code;

    /**
     * 异常信息
     */
    private String message;


    public DaoException(String message) {
        super(message);
        this.message = message;
    }
}
