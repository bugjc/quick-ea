package com.bugjc.ea.qrcode.web.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author aoki
 * @date ${date}
 */
@Data
public class C2bBeiSaoRequest {

    @NotBlank(message = "C2B码不能为空",groups = {ConsumeProcessControlGroup.class,PayRecordGroup.class})
    private String qrNo;
    @NotBlank(message = "用户ID不能为空",groups = {ConsumeProcessControlGroup.class,PayRecordGroup.class})
    private String userId;

}
