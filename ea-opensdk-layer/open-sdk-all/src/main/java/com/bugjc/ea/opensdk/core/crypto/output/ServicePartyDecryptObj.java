package com.bugjc.ea.opensdk.core.crypto.output;

import lombok.Data;

import java.io.Serializable;

/**
 * 服务方解密参数对象
 */
@Data
public class ServicePartyDecryptObj implements Serializable {

    /**
     * 签名成功?true or false
     */
    private boolean signSuccessful;

    /**
     * 接口业务参数（明文）
     */
    private String body;

}
