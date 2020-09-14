package com.bugjc.ea.opensdk.http.core.dto;

/**
 * 应答码
 *
 * @author aoki
 * @date 2020/9/3
 **/
public interface ResultCode {
    /**
     * 应答码
     *
     * @return
     */
    int getCode();

    /**
     * 应答描述
     *
     * @return
     */
    String getMessage();
}
