## quick-ea
[快速开始](https://github.com/bugjc/quick-ea/wiki/%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8B)企业架构，关注业务代码的开发。  



## 层级划分
- WEB层
    - [x] admin system
- 网关层
    - [x] zuul
- 基础服务层
    - [x] eureka server
- 服务层
    - [x] member server
    - [x] qrcode server
- API TEST层
    - [x] IDEA Rest Client
- 任务调度层
    - [x] xxl-job
- 开发工具层
    - [x] 服务层业务代码生成
    - [x] flyway
- 监控层
    - [x] spring boot admin/
    - [x] zipkin
    - [x] spring cloud hystrix turbine
    - [x] gcviewer

## 技术组件
- spring boot
- spring cloud 
- spring cloud feign
- spring cloud sleuth
- spring cloud eureka
- spring cloud zuul
- spring cloud zuul ratelimit
- spring cloud hystrix
- xxl-job
- docker
- jib
- kubernate

## 功能特性

- 负载均衡
- RSA签名
- 流量控制
- 应用监控
- jwt认证
- 自动化部署

## 统一API网关
- 负载均衡
- 正反代理
- 动态签名认证 | 排除
- 动态路由
- 断路器
- 应用监控（x）
- 流量控制 (x)
- 密钥自动更新分发（x）
- 动态接口权限认证（x）