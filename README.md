## quick-ea
[快速开始](https://github.com/bugjc/quick-ea/wiki/%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8B)企业架构，关注业务代码的开发。  

## 开发环境
- JDK 8
- Maven 3.2以上

## 端口约定
- API 网关：7900 ~ 8000
- 基础服务：8000 ~ 8100
- 监控应用：8200 ~ 8500
- 定制业务任务：8600 ~ 8800
- 数据分析任务：8900 ~ 9400
- 定制业务服务：9500 ~ 10000

## 应答码约定
### 通用应答码规定
- 200:成功
- 400：失败
- 401：认证失败
- 404：接口不存在
- 500：服务器内部错误
#### 应用应答码规定
- API Gateway:1000 ~ 2000
- 认证服务器：2001 ~ 3001
- 业务服务器：不包含1000 ~ 3001的即可

##### API Gateway 应答码
- 1000：业务负载均衡服务不可用

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