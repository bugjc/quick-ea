package com.bugjc.ea.server.websocket.api;

import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.server.websocket.component.AtomicCounterComponent;
import com.bugjc.ea.server.websocket.component.WsSessionComponent;
import com.bugjc.ea.server.websocket.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;


/**
 * websocket 服务端点
 *
 * @author aoki
 * @date 2019/8/5 10:01
 **/
@Slf4j
@ServerEndpoint("/ws/{uuid}")
@RestController
public class WsEndpoint {

    @OnOpen
    public void onOpen(Session session, @PathParam("uuid") String uuid) {
        try {
            log.info("建立 websocket 连接，session id={}", session.getId());
            if (StrUtil.isBlank(uuid)) {
                log.error("建立 websocket 连接失败，uuid 不能为空！");
                return;
            }

            WsSessionComponent wsSessionComponent = SpringContextUtil.getBean(WsSessionComponent.class);
            wsSessionComponent.onOpen(uuid, session);
            log.info("建立 websocket 连接通道成功！当前连接总数={}", AtomicCounterComponent.get());
        } catch (Exception ex) {
            log.error("建立 websocket 连接失败！错误信息={}", ex.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        WsSessionComponent wsSessionComponent = SpringContextUtil.getBean(WsSessionComponent.class);
        wsSessionComponent.onClose(session);
        log.info("关闭 websocket 连接通道成功，当前连接总数={}", AtomicCounterComponent.get());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("连接出现失败!错误信息:{}", throwable.getMessage());
        this.onClose(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("接收到来自客户端的消息={}", message);
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息错误！{}", e.getMessage());
        }
    }

}
