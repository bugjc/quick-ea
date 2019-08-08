-- 创建数据库和账户
CREATE DATABASE IF NOT EXISTS gl_api_gateway_test COLLATE = 'utf8_general_ci' CHARACTER SET = 'utf8';
GRANT ALL ON gl_api_gateway_test.* TO 'gateway'@'%' IDENTIFIED BY 'gateway2018';
GRANT ALL ON gl_api_gateway_test.* TO 'gateway'@'localhost' IDENTIFIED BY 'gateway2018';
USE `gl_api_gateway_test`;


DROP TABLE IF EXISTS `route`;

CREATE TABLE `route` (
  `id` varchar(50) NOT NULL COMMENT '路由的ID（默认情况下与其映射键相同）。',
  `path` varchar(255) NOT NULL COMMENT '路由的路径, 例： /foo/**.',
  `service_id` varchar(50) DEFAULT NULL COMMENT '要映射到此路由的服务ID，配合服务发现使用。如果没有可以指定物理URL或服务，但不能同时指定两者。',
  `url` varchar(255) DEFAULT NULL COMMENT '要映射到路由的完整物理URL。, 另一种方法是使用服务ID 和服务发现来查找物理地址。',
  `ribbon_url` varchar(255) DEFAULT NULL COMMENT '物理地址负载均衡配置',
  `retryable` tinyint(1) DEFAULT NULL COMMENT '用于指示此路由应该可重试的标志（如果支持）。 通常，重试需要服务ID和功能区。',
  `strip_prefix` int(11) DEFAULT NULL COMMENT '标记以确定在转发之前是否应剥离此路由的前缀。例：/api/v1/member/1 --> /member/1',
  `enabled` tinyint(1) NOT NULL COMMENT '是否启用（1:启用，0禁用）',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='zuul网关路由表';