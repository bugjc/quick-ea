package com.glcxw.wechat.web.reqbody;

import lombok.Data;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author aoki
 */
@Data
public class TemplateMessageDO {

    /**
     * 判断用户是否关注公众号
     * @author aoki
     */
    @Data
    public static class IsSubscribeDO {
        /**
         * 会员编号
         */
        @NotBlank(message = "会员编号不能为空")
        private String memberId;
    }


    /**
     * 发送模板消息
     * @author aoki
     */
    @Data
    public static class SendTemplateMsgDO {

        /**
         * 接收者id(会员编号、openid)
         */
        @NotBlank(message = "消息接收者会员编号不能为空")
        private String toUser;
        /**
         * 模板id
         */
        @NotBlank(message = "消息模板id不能为空")
        private String templateId;
        /**
         * 模板跳转链接.
         */
        private String url;

        /**
         * 跳小程序所需数据，不需跳小程序可不用传该数据.
         *
         * @see #url
         */
        private WxMpTemplateMessage.MiniProgram miniProgram;

        /**
         * 模板数据
         */
        @NotEmpty(message = "消息模板数据不能为空")
        private List<WxMpTemplateData> wxMpTemplateData;

    }

}
