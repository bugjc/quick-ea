package com.bugjc.ea.auth.core.component;


import cn.hutool.core.util.HashUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.bugjc.ea.auth.core.constants.ApiGatewayKeyConstants;
import com.bugjc.ea.auth.model.CustomZuulRoute;
import com.bugjc.ea.auth.service.ZuulRouteService;
import com.bugjc.ea.auth.core.util.RuleUtil;
import com.bugjc.ea.auth.model.Server;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * 被扫消费二维码生命流程控制
 * @author : qingyang
 */
@Slf4j
@Component
public class RibbonComponent {

    /**
     * @CreateCache 创建缓存实例
     * @CacheRefresh refresh + timeUnit（默认单位是秒） = 60秒刷新，stopRefreshAfterLastAccess = 如果key长时间未被访问，则相关的刷新任务就会被自动移除。
     * @CachePenetrationProtect 表示在多线程环境中同步加载数据
     */
    @CreateCache(name = "ribbon:servers:",expire = 60, cacheType = CacheType.BOTH)
    @CachePenetrationProtect
    private Cache<String, JSONObject> serverStatusCache;

    @Resource
    private ZuulRouteService zuulRouteService;

    /**
     * 获取当前应用路由唯一key
     * @param appId
     * @param uri
     * @return
     */
    private static String getAppRouteKey(String appId, String uri){
        //生成唯一服务key
        return String.valueOf(HashUtil.bkdrHash(appId.concat("/").concat(uri.split("/")[1])));
    }

    /**
     * 获取具体路由的物理地址key
     * @param url
     * @return
     */
    private static String getAppRouteUrlKey(String url){
        return String.valueOf(HashUtil.bkdrHash(url));
    }

    /**
     * 标记服务为正常状态
     * @param appId
     * @param path
     * @param url
     */
    private void markServerUp(String appId, String path, String url){
        String markKey = getAppRouteKey(appId,path);
        String urlKey = getAppRouteUrlKey(url);
        JSONObject map = serverStatusCache.get(markKey);
        if (map == null || map.isEmpty()){
            map = new JSONObject();
            map.put(urlKey,false);
            serverStatusCache.put(markKey,map);
            return;
        }
        if (!map.containsKey(urlKey)){
            //标记服务为可用状态
            map.put(urlKey,false);
            serverStatusCache.put(markKey,map);
            return;
        }

        if (map.getBoolean(urlKey)){
            //标记不可用状态的服务为可用状态
            map.put(urlKey,false);
            serverStatusCache.put(markKey,map);
        }
    }

    /**
     * 标记失效的服务
     * @param appId
     * @param path
     */
    public void markServerDown(String appId, String path, String url){
        String markKey = getAppRouteKey(appId,path);
        String urlKey = getAppRouteUrlKey(url);
        //标记服务失效时间60s
        JSONObject map = serverStatusCache.get(markKey);
        if (map == null || map.isEmpty()){
            map = new JSONObject();
            map.put(urlKey,true);
            serverStatusCache.put(markKey,map);
        }

        if (!map.containsKey(urlKey)){
            //标记服务为不可用状态
            map.put(urlKey,true);
            serverStatusCache.put(markKey,map);
            return;
        }

        if (!map.getBoolean(urlKey)){
            //标记可用状态的服务为不可用状态
            map.put(urlKey,true);
            serverStatusCache.put(markKey,map);
        }

    }

    /**
     * 检测服务是否已失效
     * @param appId
     * @param uri
     * @return false:可用 true：失效
     */
    private boolean checkServerDown(String appId, String uri, String url){
        String markKey = getAppRouteKey(appId,uri);
        String urlKey = getAppRouteUrlKey(url);
        JSONObject map = serverStatusCache.get(markKey);
        if (map == null || map.isEmpty()){
            return false;
        }
        Boolean flag = map.getBoolean(urlKey);
        return flag != null && flag;
    }

    /**
     * 选择一个服务
     * @param appId
     * @param path
     * @param isDebug
     * @return
     * @throws MalformedURLException
     */
    public URL chooseServer(String appId, String path, boolean isDebug) throws MalformedURLException {
        List<Server> allList = getAllServers(appId, path, isDebug);
        if (allList == null || allList.isEmpty()) {
            return null;
        }
        //使用加权轮询算法选择一个服务
        Server server = RuleUtil.weightRobin(allList);
        //将服务添加到当前上下文中
        RequestContext context = getCurrentContext();
        context.set(ApiGatewayKeyConstants.PHYSICAL_ROUTING_ADDRESS,server.getUrl());
        //标记服务可用
        markServerUp(appId,path,server.getUrl());
        return new URL(server.getUrl());
    }

    private List<Server> getAllServers(String appId, String path, boolean isDebug) {
        CustomZuulRoute customZuulRoute = zuulRouteService.findByAppIdAndPath(appId, path, isDebug);
        if (customZuulRoute == null) {
            return null;
        }

        List<Server> ribbonServers = new ArrayList<>();
        if (StrUtil.isNotBlank(customZuulRoute.getUrl())) {
            ribbonServers.add(0, new Server(customZuulRoute.getUrl().trim()));
        }

        if (StrUtil.isNotBlank(customZuulRoute.getRibbonUrl())){
            ribbonServers.addAll(JSON.parseArray(customZuulRoute.getRibbonUrl().trim(), Server.class));
        }

        if (ribbonServers.isEmpty()) {
            return null;
        }

        List<Server> list = new ArrayList<>();
        for (Server server : ribbonServers) {
            if (checkServerDown(appId,path,server.getUrl())){
                //排除失效服务
                continue;
            }

            if (isDebug && server.getIsDebug()) {
                //已开启debug模式的加入debug服务到列表中
                for (int j = 0; j <= server.getWeight(); j++) {
                    list.add(server);
                }
                continue;
            }

            for (int j = 0; j <= server.getWeight(); j++) {
                list.add(server);
            }
        }
        return list;
    }

}

