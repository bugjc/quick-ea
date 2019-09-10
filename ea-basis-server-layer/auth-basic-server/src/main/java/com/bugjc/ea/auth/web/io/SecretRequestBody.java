package com.bugjc.ea.auth.web.io;

import lombok.Data;

import java.io.Serializable;

/**
 * 加密请求体
 * @author aoki
 * @date ${date}
 */
@Data
public class SecretRequestBody implements Serializable {

    //运营商标识
    private String operatorId;
    //请求参数
    private String data;
    //时间戳
    private String timeStamp;
    //序列号
    private String seq;
    //签名
    private String sig;
}
