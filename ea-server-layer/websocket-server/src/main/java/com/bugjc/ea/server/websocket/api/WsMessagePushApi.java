package com.bugjc.ea.server.websocket.api;

import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.dto.CommonResultCode;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.server.websocket.component.WsSessionComponent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * websocket 消息推送服务接口
 *
 * @author aoki
 * @date 2019/8/5 10:01
 **/
@Slf4j
@RestController
@RequestMapping
public class WsMessagePushApi {

    @Resource
    private WsSessionComponent wsSessionComponent;


    @GetMapping("/")
    public ModelAndView routeUserIndex() {
        return new ModelAndView("websocket");
    }

    /**
     * 消息推送请求应答对象
     *
     * @author aoki
     * @date 2019/9/23 15:57
     **/
    @Data
    public static class MessagePushBody implements Serializable {
        private String uuid;
        private String message;
    }

    /**
     * 发送消息到指定用户所在的页面（业务）
     *
     * @param requestBody
     * @return
     */
    @PostMapping("/ws/message/push")
    public Result messagePush(@Validated @RequestBody MessagePushBody requestBody) {
        log.info("接收到 Websocket 消息推送请求参数：{}", JSON.toJSONString(requestBody));
        try {
            wsSessionComponent.sendText(requestBody.getUuid(), requestBody.getMessage());
            return Result.success();
        } catch (Exception e) {
            return Result.failure(CommonResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
