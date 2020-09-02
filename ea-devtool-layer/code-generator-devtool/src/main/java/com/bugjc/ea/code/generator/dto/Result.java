package com.bugjc.ea.code.generator.dto;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一API响应结果封装
 *
 * @author : aoki
 */
@Data
@Builder
public class Result<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public static Result success() {
        return new ResultBuilder()
                .code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .build();
    }

    public static <T> Result<T> success(T obj) {
        return new ResultBuilder<T>()
                .data(obj)
                .code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .build();
    }

    public static Result failure(ResultCode resultCode) {
        return new ResultBuilder()
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .build();
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
