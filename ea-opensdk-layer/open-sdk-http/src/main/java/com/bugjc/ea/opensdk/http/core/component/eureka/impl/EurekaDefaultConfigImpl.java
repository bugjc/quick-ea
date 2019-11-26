package com.bugjc.ea.opensdk.http.core.component.eureka.impl;


import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.cache.impl.LRUCache;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.component.eureka.EurekaConfig;
import com.bugjc.ea.opensdk.http.core.component.eureka.RedisConstants;
import com.bugjc.ea.opensdk.http.core.component.eureka.config.MyInstanceConfig;
import com.bugjc.ea.opensdk.http.core.component.eureka.model.Server;
import com.bugjc.ea.opensdk.http.core.component.eureka.model.ZuulRoute;
import com.bugjc.ea.opensdk.http.core.util.AntPathMatcher;
import com.bugjc.ea.opensdk.http.core.util.RuleUtil;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.shared.Application;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于 redis 的简单负载实现
 * @author : qingyang
 */
@Slf4j
public class EurekaDefaultConfigImpl implements EurekaConfig {

    /**
     * 缓存容量（key1 - key2）
     */
    private static final int CACHE_KEY_CAPACITY = 1000;

    /**
     * 过期时间：60 秒
     */
    public final static int KEY_EXPIRE_DATE = 60;

    /**
     * 缓存容量（key2 - Value）
     */
    private static final int CACHE_VALUE_CAPACITY = 100;

    /**
     * 过期时间：1800 秒
     */
    public final static int VALUE_EXPIRE_DATE = 1800;

    /**
     * 远程 redis存储 token
     */
    private JedisPool redisStorage;

    /**
     * 获取 path --> serviceId 的缓存对象
     */
    private static FIFOCache<String, ZuulRoute> cacheKey = SingletonEnum.CACHE_INSTANCE.getCacheKeyInstance();

    /**
     * 获取 serviceId --> list server 的缓存对象
     */
    private static LRUCache<String, List<Server>> cacheValue = SingletonEnum.CACHE_SERVER_INSTANCE.getCacheValueInstance();

    /**
     * 私有化构造函数
     */
    private EurekaDefaultConfigImpl(){}

    /**
     * 定义一个静态枚举类
     */
    enum SingletonEnum {
        //创建一个枚举对象，该对象天生为单例
        INSTANCE,
        //创建一个缓存实例对象，用于 path --> serviceId 的缓存
        CACHE_INSTANCE,
        //创建一个缓存实例对象, 用于 serviceId --> list server 的缓存
        CACHE_SERVER_INSTANCE;
        private EurekaDefaultConfigImpl ribbonComponent;
        private FIFOCache<String, ZuulRoute> cacheKey;
        private LRUCache<String, List<Server>> cacheValue;

        /**
         * 私有化枚举的构造函数
         */
        SingletonEnum(){
            ribbonComponent = new EurekaDefaultConfigImpl();
            cacheKey = CacheUtil.newFIFOCache(CACHE_KEY_CAPACITY);
            cacheValue = CacheUtil.newLRUCache(CACHE_VALUE_CAPACITY);
        }

        public EurekaDefaultConfigImpl getInstance(){
            return ribbonComponent;
        }

        public FIFOCache<String, ZuulRoute> getCacheKeyInstance(){
            return cacheKey;
        }

        public LRUCache<String, List<Server>> getCacheValueInstance(){
            return cacheValue;
        }
    }

    /**
     * 暴露获取实例的静态方法
     * @return
     */
    public static EurekaDefaultConfigImpl getInstance(JedisPool redisStorage){
        EurekaConfig eurekaConfig = SingletonEnum.INSTANCE.getInstance();
        eurekaConfig.setStorageObject(redisStorage);
        return SingletonEnum.INSTANCE.getInstance();
    }

    @Override
    public void init() {
        // 内部调用 初始化Eureka Client，TODO eureka-client.properties 文件
        DiscoveryManager.getInstance().initComponent(new MyInstanceConfig(), new DefaultEurekaClientConfig());
    }

    @Override
    public void setStorageObject(Object storageObject) {
        this.redisStorage = (JedisPool) storageObject;
    }

    /**
     * 选择一个服务
     *
     * @param path       --根据接口路径获取一个服务，例如：path = /job/create
     * @return Server
     */
    @Override
    public Server chooseServer(String path) {
        //获取当前路由信息
        ZuulRoute currentZuulRoute = getCurrentZuulRoute(path);

        //从当前路由信息中获取服务列表
        List<Server> allList = getServers(path, currentZuulRoute);

        //使用加权轮询算法从服务列表中选择一个服务
        return RuleUtil.weightRobin(allList);
    }

    /**
     * 获取服务列表
     * @param path
     * @param currentZuulRoute
     * @return
     */
    private List<Server> getServers(String path, ZuulRoute currentZuulRoute){

        // 尝试从缓存中获取服务列表数据
        List<Server> servers = cacheValue.get(currentZuulRoute.getServiceId());
        if (servers != null && !servers.isEmpty()){
            return servers;
        }

        // 从 eureka 服务中获取指定应用的所有服务
        Application application = DiscoveryManager.getInstance().getDiscoveryClient().getApplication(currentZuulRoute.getServiceId());
        if (application == null || application.getInstancesAsIsFromEureka().isEmpty()) {
            log.warn("serviceId:{} service is not available!", currentZuulRoute.getServiceId());
            throw new IllegalStateException("eureka service is not available");
        }

        List<Server> allList = new ArrayList<>();
        List<InstanceInfo> instanceInfos = application.getInstances();
        for (InstanceInfo instanceInfo : instanceInfos) {
            if (currentZuulRoute.isStripPrefix()){
                allList.add(new Server(dePrefix(instanceInfo.getHomePageUrl(), path, currentZuulRoute.getPath())));
            } else {
                allList.add(new Server(dePrefix(instanceInfo.getHomePageUrl()).concat(path)));
            }
        }

        //添加 serviceId --> list server 的缓存
        cacheValue.put(currentZuulRoute.getServiceId(), allList, VALUE_EXPIRE_DATE);

        //清空数据
        instanceInfos.clear();
        return allList;
    }

    /**
     * 获取当前路由信息
     * @param path
     * @return
     */
    private ZuulRoute getCurrentZuulRoute(String path){

        // 尝试从缓存中获取当前路由信息数据
        ZuulRoute currentZuulRoute = cacheKey.get(path);
        if (currentZuulRoute != null){
            log.debug("命中缓存");
            return currentZuulRoute;
        }

        if (redisStorage == null){
            //需注入 jedis
            throw new IllegalStateException("jedis not set");
        }

        String val = redisStorage.getResource().get(RedisConstants.ZUUL_ROUTE_CONFIG_INFO);
        if (StrUtil.isBlank(val)){
            //路由配置信息不能为空，需要检查 api gateway 是否配置路由和缓存路由信息到 redis中
            throw new IllegalStateException("no route configuration information was obtained");
        }

        List<ZuulRoute> zuulRoutes = JSON.parseArray(val, ZuulRoute.class);
        if (zuulRoutes == null || zuulRoutes.isEmpty()){
            //路由配置信息不能为空，需要检查 api gateway 是否配置路由和缓存路由信息到 redis中
            throw new IllegalStateException("route configuration information cannot be empty");
        }

        final AntPathMatcher pathMatcher = new AntPathMatcher();
        for (ZuulRoute zuulRoute : zuulRoutes) {
            if (pathMatcher.match(zuulRoute.getPath(), path)){
                currentZuulRoute = zuulRoute;
                break;
            }
        }

        if (currentZuulRoute == null){
            //未匹配到路由信息，如不符合预期则先检查路由信息中的 path,在检查匹配逻辑
            throw new IllegalStateException("no routing information matched");
        }

        //添加 path --> serviceId的缓存
        cacheKey.put(path, currentZuulRoute, KEY_EXPIRE_DATE);
        return currentZuulRoute;
    }

    /**
     * 去除后缀 “/”
     * @param homePageUrl
     * @return
     */
    private String dePrefix(String homePageUrl){
        //去除后缀 "/"
        if (homePageUrl.endsWith("/")){
            homePageUrl = homePageUrl.substring(0, homePageUrl.length() - 1);
        }
        return homePageUrl;
    }

    /**
     * 去除路径前缀
     * @param homePageUrl
     * @param pattern
     * @return url
     */
    private String dePrefix(String homePageUrl, String path, String pattern){

        //提取出路径前缀
        String prefix = pattern.substring(pattern.indexOf("/"), pattern.lastIndexOf("/"));
        try {
            //首先通过统一定位资源包装，其次在对当前完整路径统一斜杠，最后在替换第一个出现的前缀为空字符串。
            return dePrefix(homePageUrl) + new URI(path).getPath().replaceAll("//","/").replaceFirst(prefix, "");
        } catch (URISyntaxException e) {
            return null;
        }
    }
}

