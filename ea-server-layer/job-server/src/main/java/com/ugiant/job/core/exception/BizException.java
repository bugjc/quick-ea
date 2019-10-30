package com.ugiant.job.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常基类
 * @author aoki
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -5875371379845226068L;

    /**
     * 异常信息
     */
    private String msg;

    /**
     * 具体异常码
     */
    private int code;

    public BizException(String msgFormat) {
        super(msgFormat);
        msg = msgFormat;
    }

    public BizException(int code, String msgFormat) {
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
    public BizException newInstance(String msgFormat) {
        return new BizException(code, msgFormat);
    }

}
