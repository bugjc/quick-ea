-- 创建数据库和账户
CREATE DATABASE IF NOT EXISTS ea_api_gateway COLLATE = 'utf8_general_ci' CHARACTER SET = 'utf8';
GRANT ALL ON ea_api_gateway.* TO 'ea_api_gateway'@'%' IDENTIFIED BY 'ea_api_gateway2018';
GRANT ALL ON ea_api_gateway.* TO 'ea_api_gateway'@'localhost' IDENTIFIED BY 'ea_api_gateway2018';
USE ea_api_gateway;

-- 创建数据库表及插入初始示例数据
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ea_api_gateway` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `ea_api_gateway`;

DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `app_id` varchar(32) NOT NULL COMMENT '应用id',
  `rsa_public_key` text COMMENT '接入方RSA公钥',
  `rsa_private_key` text COMMENT '服务方RSA私钥',
  `access_party_metadata` text COMMENT '接入方元数据',
  `service_party_metadata` text COMMENT '服务方元数据',
  `type` smallint(2) DEFAULT '0' COMMENT '1:企业内部接入，2:外部企业接入',
  `enabled` tinyint(1) DEFAULT '0' COMMENT '是否启用（1:启用，0禁用）',
  `description` varchar(50) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `app_route` */

DROP TABLE IF EXISTS `app_route`;

CREATE TABLE `app_route` (
  `app_id` varchar(32) NOT NULL COMMENT '应用ID',
  `route_id` varchar(32) NOT NULL COMMENT '路由ID',
  `is_debug` tinyint(1) DEFAULT NULL COMMENT '是否开启debug模式（1:启用，0禁用）',
  `enabled` tinyint(1) DEFAULT NULL COMMENT '是否启用（1:启用，0禁用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  KEY `app_id` (`app_id`),
  KEY `route_id` (`route_id`),
  CONSTRAINT `app_route_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `app` (`app_id`),
  CONSTRAINT `app_route_ibfk_2` FOREIGN KEY (`route_id`) REFERENCES `route` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用路由表';

/*Table structure for table `app_security_config` */

DROP TABLE IF EXISTS `app_security_config`;

CREATE TABLE `app_security_config` (
  `app_id` varchar(50) NOT NULL COMMENT 'appid',
  `path` varchar(100) DEFAULT NULL COMMENT '路径',
  `is_verify_signature` tinyint(1) DEFAULT '0' COMMENT '是否核验签名（1:是,0:否）',
  `is_verify_token` tinyint(1) DEFAULT '0' COMMENT '是否核验Token（1:是,0:否）',
  `is_data_encrypt` tinyint(1) DEFAULT '0' COMMENT '是否加密数据（1:是,0:否）',
  `enabled` tinyint(1) DEFAULT NULL COMMENT '是否启用配置（1:启用，0:禁用）',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  KEY `app_id` (`app_id`),
  CONSTRAINT `app_security_config_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `app` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用安全配置表';

/*Table structure for table `app_version_map` */

DROP TABLE IF EXISTS `app_version_map`;

CREATE TABLE `app_version_map` (
  `app_id` varchar(32) NOT NULL COMMENT '应用id',
  `version_no` varchar(10) DEFAULT NULL COMMENT '当前版本号',
  `path` varchar(30) DEFAULT NULL COMMENT '路径',
  `map_path` varchar(30) DEFAULT NULL COMMENT '映射路径',
  `description` varchar(50) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  UNIQUE KEY `INDEX_KEY1` (`app_id`,`version_no`),
  CONSTRAINT `app_version_map_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `app` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用版本映射表';

/*Table structure for table `route` */

DROP TABLE IF EXISTS `route`;

CREATE TABLE `route` (
  `id` varchar(50) NOT NULL COMMENT '路由的ID（默认情况下与其映射键相同）。',
  `path` varchar(100) NOT NULL COMMENT '路由的路径, 例： /foo/**.',
  `service_id` varchar(50) DEFAULT NULL COMMENT '要映射到此路由的服务ID，配合服务发现使用。如果没有可以指定物理URL或服务，但不能同时指定两者。',
  `url` varchar(50) DEFAULT NULL COMMENT '要映射到路由的完整物理URL。, 另一种方法是使用服务ID 和服务发现来查找物理地址。',
  `ribbon_url` varchar(255) DEFAULT NULL COMMENT '物理地址负载均衡配置',
  `retryable` tinyint(1) DEFAULT NULL COMMENT '用于指示此路由应该可重试的标志（如果支持）。 通常，重试需要服务ID和功能区。',
  `strip_prefix` tinyint(1) DEFAULT NULL COMMENT '标记以确定在转发之前是否应剥离此路由的前缀。例：/api/v1/member/1 --> /member/1',
  `enabled` tinyint(1) NOT NULL COMMENT '是否启用（1:启用，0禁用）',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='zuul网关路由表';