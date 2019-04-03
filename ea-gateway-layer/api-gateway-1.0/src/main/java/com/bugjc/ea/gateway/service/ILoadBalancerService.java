package com.bugjc.ea.gateway.service;

import com.bugjc.ea.gateway.model.Server;

import java.util.List;

public interface ILoadBalancerService {

    /**
     * 标记失效的服务器
     *
     * @param server
     */
    void markServerDown(Server server);

    /**
     * 获取应用可用的服务列表
     *
     * @param appId
     * @return
     */
    List<Server> getReachableServers(String appId);



}
