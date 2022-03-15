package com.bugjc.ea.server.websocket.component;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket session 管理
 *
 * @author aoki
 * @date 2019/9/23 13:45
 **/
@Lazy
@Component
public class WsSessionComponent {

    /**
     * 创建session 和 业务key 的映射缓存对象
     */
    private static Cache<String, String> sessionMapBusinessKeyCache = CacheUtil.newLRUCache(200);

    /**
     * websocket session 对象集合
     */
    private static ConcurrentHashMap<String, Session> websocketSessionMap = new ConcurrentHashMap<>();


    /**
     * 发送文本消息到指定用户的位置(页面)
     *
     * @param uuid
     * @param message
     */
    public void sendText(String uuid, String message) throws Exception {
        Session session = websocketSessionMap.get(uuid);
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText(message);
        }
        throw new Exception("send message failure!");
    }

    /**
     * 添加 websocket session 对象
     *
     * @param uuid
     * @param session
     */
    public void onOpen(String uuid, Session session) {
        synchronized (this) {
            if (websocketSessionMap.containsKey(uuid)) {
                return;
            }

            AtomicCounterComponent.increment();
            //保存 websocket session
            websocketSessionMap.put(uuid, session);
            sessionMapBusinessKeyCache.put(String.valueOf(session.getId()), uuid);
        }

    }

    /**
     * 关闭 websocket session
     *
     * @param session
     */
    public void onClose(Session session) {
        synchronized (this) {
            if (session.getId() == null) {
                return;
            }
            String key = sessionMapBusinessKeyCache.get(session.getId());
            if (StrUtil.isBlank(key)) {
                return;
            }

            AtomicCounterComponent.decrement();
            websocketSessionMap.remove(key);
        }
    }

}
