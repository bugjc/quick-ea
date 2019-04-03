package com.glcxw.gateway.service;

import com.glcxw.gateway.model.Server;

public interface IRuleService {

    Server choose();

    void setLoadBalancer(ILoadBalancerService lb);

    ILoadBalancerService getLoadBalancer();
}
