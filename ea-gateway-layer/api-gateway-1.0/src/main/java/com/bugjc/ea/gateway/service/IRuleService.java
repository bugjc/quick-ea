package com.bugjc.ea.gateway.service;

import com.bugjc.ea.gateway.model.Server;

public interface IRuleService {

    Server choose();

    void setLoadBalancer(ILoadBalancerService lb);

    ILoadBalancerService getLoadBalancer();
}
