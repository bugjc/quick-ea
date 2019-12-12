package com.bugjc.ea.opensdk.http.core.component.eureka.impl;


import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.cache.impl.LRUCache;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.component.eureka.EurekaConfig;
import com.bugjc.ea.opensdk.http.core.component.eureka.config.MyInstanceConfig;
import com.bugjc.ea.opensdk.http.core.component.eureka.constants.EurekaConstants;
import com.bugjc.ea.opensdk.http.core.component.eureka.entity.Server;
import com.bugjc.ea.opensdk.http.core.component.eureka.entity.ZuulRoute;
import com.bugjc.ea.opensdk.http.core.exception.ElementNotFoundException;
import com.bugjc.ea.opensdk.http.core.util.AntPathMatcher;
import com.bugjc.ea.opensdk.http.core.util.IpAddressUtil;
import com.bugjc.ea.opensdk.http.core.util.RuleUtil;
import com.bugjc.ea.opensdk.http.service.HttpService;
import com.google.inject.Inject;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.shared.Application;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于 redis 的简单负载实现
 *
 * @author : qingyang
 */
@Slf4j
public class EurekaDefaultConfigImpl implements EurekaConfig {

    /**
     *  平台接口授权服务 http客户端
     */
    @Inject
    private HttpService httpService;

    /**
     * 缓存容量（key1 - key2）
     */
    private final static int CACHE_KEY_CAPACITY = 200;

    /**
     * 过期时间：60 秒
     */
    private final static int KEY_EXPIRE_DATE = 60;

    /**
     * 缓存容量（key2 - Value）
     */
    private final static int CACHE_VALUE_CAPACITY = 5;

    /**
     * 过期时间：1800 秒
     */
    private final static int VALUE_EXPIRE_DATE = 1800;

    /**
     * http 协议url前缀
     */
    private final static String HTTP_PREFIX = "http://";

    /**
     * 获取 path --> serviceId 的缓存对象
     */
    private final static FIFOCache<String, ZuulRoute> CACHE_KEY = SingletonEnum.CACHE_INSTANCE.getCacheKeyInstance();

    /**
     * 获取 serviceId --> list server 的缓存对象
     */
    private final static LRUCache<String, List<Server>> CACHE_VALUE = SingletonEnum.CACHE_SERVER_INSTANCE.getCacheValueInstance();

    /**
     * 私有化构造函数
     */
    private EurekaDefaultConfigImpl() {
    }

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
        SingletonEnum() {
            ribbonComponent = new EurekaDefaultConfigImpl();
            cacheKey = CacheUtil.newFIFOCache(CACHE_KEY_CAPACITY);
            cacheValue = CacheUtil.newLRUCache(CACHE_VALUE_CAPACITY);
        }

        public EurekaDefaultConfigImpl getInstance() {
            return ribbonComponent;
        }

        public FIFOCache<String, ZuulRoute> getCacheKeyInstance() {
            return cacheKey;
        }

        public LRUCache<String, List<Server>> getCacheValueInstance() {
            return cacheValue;
        }
    }

    /**
     * 暴露获取实例的静态方法
     *
     * @return
     */
    public static EurekaDefaultConfigImpl getInstance() {
        return SingletonEnum.INSTANCE.getInstance();
    }

    @Override
    public void init() {
        // 内部调用 初始化Eureka Client
        try {
            //设置被读取配置文件名称  默认config.properties
            System.setProperty("archaius.configurationSource.defaultFileName", EurekaConstants.EUREKA_DEFAULT_FILE_NAME);
            DiscoveryManager.getInstance().initComponent(new MyInstanceConfig(), new DefaultEurekaClientConfig());
        }catch (Exception ex){
            log.error("初始化 Eureka 服务失败！错误信息：{}", ex.getMessage());
        }
    }

    @Override
    public void shutdown() {
        DiscoveryManager.getInstance().shutdownComponent();
    }

    /**
     * 选择一个服务
     *
     * @param path --根据接口路径获取一个服务，例如：path = /job/create
     * @return Server
     */
    @Override
    public Server chooseServer(String path) {
        try {
            //获取当前路由信息
            ZuulRoute currentZuulRoute = getCurrentZuulRoute(path);

            //从当前路由信息中获取服务列表
            List<Server> allList = getServers(path, currentZuulRoute);

            //使用加权轮询算法从服务列表中选择一个服务
            return RuleUtil.weightRobin(allList);
        } catch (ElementNotFoundException e) {
            //TODO 这里统计获取内部服务地址失败的次数，当超过失败次数设定的阈值或时间比例则主动将内部调用切换成外部调用方式。
            log.warn("尝试获取内部服务地址出错了！错误信息：{}",e.getMsg());
            return null;
        }

    }

    /**
     * 获取服务列表
     *
     * @param path
     * @param currentZuulRoute
     * @return
     */
    private List<Server> getServers(String path, ZuulRoute currentZuulRoute) {

        // 尝试从缓存中获取服务列表数据
        List<Server> servers = CACHE_VALUE.get(currentZuulRoute.getServiceId());
        if (servers != null && !servers.isEmpty()) {
            return servers;
        }

        // 从 eureka 服务中获取指定应用的所有服务
        Application application = DiscoveryManager.getInstance().getDiscoveryClient().getApplication(currentZuulRoute.getServiceId());
        if (application == null || application.getInstancesAsIsFromEureka().isEmpty()) {
            throw new ElementNotFoundException(currentZuulRoute.getServiceId() + " eureka service is not available!");
        }

        List<Server> allList = new ArrayList<>();
        List<InstanceInfo> instanceInfos = application.getInstances();
        for (InstanceInfo instanceInfo : instanceInfos) {
            String url = instanceInfo.getHomePageUrl();
            if (IpAddressUtil.internalIp(instanceInfo.getInstanceId())){
                //eureka ip 是内网的则组装内网调用地址。
                url = HTTP_PREFIX + instanceInfo.getIPAddr() +":"+ instanceInfo.getPort();
            }
            if (currentZuulRoute.isStripPrefix()) {
                allList.add(new Server(dePrefix(url, path, currentZuulRoute.getPath())));
            } else {
                allList.add(new Server(dePrefix(url).concat(path)));
            }
        }

        //添加 serviceId --> list server 的缓存
        CACHE_VALUE.put(currentZuulRoute.getServiceId(), allList, VALUE_EXPIRE_DATE);

        //清空数据
        instanceInfos.clear();
        return allList;
    }

    /**
     * 获取当前路由信息
     *
     * @param path
     * @return
     */
    private ZuulRoute getCurrentZuulRoute(String path) {

        // 尝试从缓存中获取当前路由信息数据
        ZuulRoute currentZuulRoute = CACHE_KEY.get(path);
        if (currentZuulRoute != null) {
            return currentZuulRoute;
        }
        /**
         * 远程 redis存储 token
         */
        JedisPool redisStorage = httpService.getAppInternalParam().getJedisPool();
        if (redisStorage == null) {
            throw new ElementNotFoundException("jedis not set");
        }

        try (Jedis jedis = redisStorage.getResource()) {
            String val = jedis.get(EurekaConstants.ZUUL_ROUTE_CONFIG_INFO);
            if (StrUtil.isBlank(val)) {
                //路由配置信息不能为空，需要检查 api gateway 是否配置路由和缓存路由信息到 redis中
                throw new ElementNotFoundException("no route configuration information was obtained");
            }

            List<ZuulRoute> zuulRoutes = JSON.parseArray(val, ZuulRoute.class);
            if (zuulRoutes == null || zuulRoutes.isEmpty()) {
                //路由配置信息不能为空，需要检查 api gateway 是否配置路由和缓存路由信息到 redis中
                throw new ElementNotFoundException("route configuration information cannot be empty");
            }

            final AntPathMatcher pathMatcher = new AntPathMatcher();
            for (ZuulRoute zuulRoute : zuulRoutes) {
                if (pathMatcher.match(zuulRoute.getPath(), path)) {
                    currentZuulRoute = zuulRoute;
                    break;
                }
            }

            if (currentZuulRoute == null) {
                //未匹配到路由信息，如不符合预期则先检查路由信息中的 path,在检查匹配逻辑
                throw new ElementNotFoundException("no routing information matched");
            }

            //添加 path --> serviceId的缓存
            CACHE_KEY.put(path, currentZuulRoute, KEY_EXPIRE_DATE);
            return currentZuulRoute;
        }
    }

    /**
     * 去除后缀 “/”
     *
     * @param homePageUrl
     * @return
     */
    private String dePrefix(String homePageUrl) {
        //去除后缀 "/"
        if (homePageUrl.endsWith("/")) {
            homePageUrl = homePageUrl.substring(0, homePageUrl.length() - 1);
        }
        return homePageUrl;
    }

    /**
     * 去除路径前缀
     *
     * @param url
     * @param pattern
     * @return url
     */
    private String dePrefix(String url, String path, String pattern) {

        //提取出路径前缀
        String prefix = pattern.substring(pattern.indexOf("/"), pattern.lastIndexOf("/"));
        try {
            //首先通过统一定位资源包装，其次在对当前完整路径统一斜杠，最后在替换第一个出现的前缀为空字符串。
            return dePrefix(url) + new URI(path).getPath().replaceAll("//", "/").replaceFirst(prefix, "");
        } catch (URISyntaxException e) {
            throw new ElementNotFoundException("error removing path prefix");
        }
    }
}

