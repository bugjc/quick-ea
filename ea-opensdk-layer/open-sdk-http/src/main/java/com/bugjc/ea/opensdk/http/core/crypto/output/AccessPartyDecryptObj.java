package com.bugjc.ea.opensdk.http.core.crypto.output;

import lombok.Data;

import java.io.Serializable;

/**
 * 接入方加密参数对象
 */
@Data
public class AccessPartyDecryptObj implements Serializable {

    /**
     * 接口业务参数（加密）
     */
    private String body;

    /**
     * 签名成功?true or false
     */
    private boolean signSuccessful;

}
