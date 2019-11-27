package com.bugjc.ea.opensdk.http.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元素未找到或未注入异常
 * @author aoki
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElementNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5875371379845226068L;

    /**
     * 异常信息
     */
    private String msg;

    /**
     * 具体异常码
     */
    protected int code;

    public ElementNotFoundException(String msgFormat) {
        super(msgFormat);
        msg = msgFormat;
    }

    public ElementNotFoundException(int code, String msgFormat) {
        super(msgFormat);
        this.code = code;
        msg = msgFormat;
    }
    /**
     * 实例化异常
     *
     * @param msgFormat
     * @return
     */
    public ElementNotFoundException newInstance(String msgFormat) {
        return new ElementNotFoundException(code, msgFormat);
    }

}
