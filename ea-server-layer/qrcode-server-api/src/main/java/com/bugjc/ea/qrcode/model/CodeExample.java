package com.bugjc.ea.qrcode.model;

import lombok.Data;

import java.util.Date;

/**
 * @author aoki
 */
@Data
public class CodeExample {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 码编号
     */
    private String codeNo;

    /**
     * 创建时间
     */
    private Date create;

}