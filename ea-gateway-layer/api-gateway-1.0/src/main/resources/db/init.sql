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
  `app_id` VARCHAR(32) NOT NULL COMMENT '应用id',
  `rsa_public_key` TEXT COMMENT 'RSA公钥',
  `rsa_private_key` TEXT COMMENT 'RSA私钥',
  `enabled` TINYINT(1) DEFAULT NULL COMMENT '是否启用（1:启用，0禁用）',
  `description` VARCHAR(50) DEFAULT NULL COMMENT '描述',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`app_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT  INTO `app`(`app_id`,`rsa_public_key`,`rsa_private_key`,`enabled`,`description`,`create_time`) VALUES 
('509008971445761792','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7phHLqnazavjGoj5JnDnsJzGI4vsKjvX9RN+8HMawgGt4/vdfbgIWJNRyvMF/90yixaSI7yP4yybbZuKlStvh6aDBWn+GOq02QvT0txOaLvUmZtBQImyDuKtzEpnmU7R26R/TlHcm5jnDQ4ZbZ1E4mcCN1FNKZZ8FSqnHMceETQIDAQAB','MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALumEcuqdrNq+MaiPkmcOewnMYji+wqO9f1E37wcxrCAa3j+919uAhYk1HK8wX/3TKLFpIjvI/jLJttm4qVK2+HpoMFaf4Y6rTZC9PS3E5ou9SZm0FAibIO4q3MSmeZTtHbpH9OUdybmOcNDhltnUTiZwI3UU0plnwVKqccxx4RNAgMBAAECgYAEUhWUz+79wJfL0w6GGV/IDTr5wOgw3QvS8hQIu8zjYYGX/p7phpnrsptlrOzzqlkMYmqgcIkugFb7tEnBrpCTf+ttvDKHvLejXRlr/Gip5tqlidKDBOOQAm/NuuzdunVQnXQwkbCkOLSstuNgv5WG4I0e19boX+T9EtKc/K9+LQJBAPRg5st4uFLEfiLD8soWKFraH5LXkt+ZwV9dTb4iyJraFiHzUuq43jwh4mVPYxJn6IPSta77FE6mZolY9WBrRisCQQDEkogA0N37BATZ/kSgXkLq6kVZTpvFsYZ2hDxHTRoau6i/kqCT0qgeJpEJoQ/rV9h4U2WjxnJZPgMMsWHMh1tnAkEA0zqa4w7Ci9AJkvU5+5Exam4VUnCBJEKbUVmtpAYezTJqZQgUCIyokuNa8+StprAn2yGbJtchU2YjRN6eoau3pwJAcGTYHp9OTRgfLgWUd51t5aPNwyKPLpoyp9E0JhCPvHlQIzlTVzI7rgGfLEJLN+UigKouk1YES8KJO9iwcqFyQQJBAKDZNobGwS7VkCCoM7CLC51i8hQRqODy/BCCKUjCpeSP43gbwIn8dN1Ne/MuQptVAhzPMfqpZcFmBzO4YZQ2/+E=',1,'乘车码','2018-11-05 14:21:52'),
('509737928172220918','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZ7/OKn2aAOvjfybqr+pbK1ohJwmKzbjTg2utLY/LK8KZe3omIM1GoHMdAmzyCwubju0EhtSEgcLvw72NiFvBKRubROJjwPLQr8qlIkvf/gs0Z+V9zE2l5dgxf3H3fOJmxGr/Yiq+ujDE3uTNkfIezK4h30php68rAxGuVfS8ofQIDAQAB','MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJnv84qfZoA6+N/Juqv6lsrWiEnCYrNuNODa60tj8srwpl7eiYgzUagcx0CbPILC5uO7QSG1ISBwu/DvY2IW8EpG5tE4mPA8tCvyqUiS9/+CzRn5X3MTaXl2DF/cfd84mbEav9iKr66MMTe5M2R8h7MriHfSmGnrysDEa5V9Lyh9AgMBAAECgYAuoSzYtOhLt5Fj2KufJM1ArDOkhCl5yMxjwGy97YzCRJtg6XAnvcPidLU2sM9nnLpsCXD1UPSz6vJDTYCBWgl3PiiL+gIG6sSf7PehFVQD1MRI7vMczSKJcyf88V2SNcew9AUDth+Kt1Z8kTWbrpKXlcl9326b/Rl4wW244ads0QJBAMybqy8ozDNIU87wAjhsup+7Nf5okdrlhpBH0Rj0rObnVTvaJjqlVzRVVcN+ZPnR9PATlsTk2Qm/EVFyhZ6VtW8CQQDAmiTrs36aoTJV3exHmXBpcFCR1xvQe9ExXjfh/QDB6frz0GWR+LB9px2kzIICjTk/7ibvUEdUy82jerVnPELTAkEAgX3/4C/c1JPw3qYNcbJ2hkMQj/uUW8op2MRq9HVdvCEqU1/kE/eych+T0M78jxMvBoYPRHtlVQLErhxhrpUnJwJAJfp/S0cCsQUWQt5W6Ct2giQWjxuGrY6synpUtKhKDPLRfGBclvMeAjkA3G1DObOVVWjlno0K88qYSyM4QBoe5wJAT3+BmNQEImnsejuPvUjBBkcR2OuE0D05/y9+KLvwg4dM0tQu8p3Bu9MYm4hg2kEmb6BgtzFXR0xCSm5asabffg==',1,'银联消费维码','2018-11-07 14:46:33');

DROP TABLE IF EXISTS `app_security_config`;
CREATE TABLE `app_security_config` (
  `id` INT(12) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` VARCHAR(50) NOT NULL COMMENT 'appid',
  `exclude_path` VARCHAR(100) DEFAULT NULL COMMENT '排除前缀路径',
  `is_verify_signature` TINYINT(1) DEFAULT '0' COMMENT '是否核验签名（1:是,0:否）',
  `is_verify_token` TINYINT(1) DEFAULT '0' COMMENT '是否核验Token（1:是,0:否）',
  `enabled` TINYINT(1) DEFAULT NULL COMMENT '是否启用配置（1:启用，0:禁用）',
  `description` VARCHAR(100) DEFAULT NULL COMMENT '描述',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8 COMMENT='应用安全配置表';

INSERT  INTO `app_exclude_security_authentication_path`(`id`,`app_id`,`exclude_path`,`is_signature`,`is_verify_token`,`enabled`,`description`,`create_time`) VALUES 
(8,'509737928172220918','/qrcode',1,1,1,'银联消费维码业务','2018-12-13 15:41:20'),
(110,'509008971177132032','/member/v2/member/balance-pay',1,0,1,'会员余额支付','2019-03-15 20:36:21'),
(111,'509737928172220918','/qrcode/zs/trade/notify',0,0,1,'银联消费维码主扫交易通知','2018-11-07 14:59:53'),
(112,'509737928172220918','/qrcode/bs/trade/notify',0,0,1,'银联消费维码被扫交易通知','2018-11-07 15:02:27'),
(113,'509737928172220918','/qrcode/bs/trade/notify/pre',0,0,1,'银联消费维码被扫前置通知','2018-11-07 15:04:22'),
(116,'509008971445761792','/ewallet',1,1,1,'乘车码业务','2019-03-16 17:55:57'),
(125,'509008971445761792','/ewallet/api/v1/order',0,0,1,'交易订单（乘车码）','2019-03-25 11:00:07'),
(126,'509008971445761792','/ewallet/v2/order',0,0,1,'交易订单（乘车码）','2019-03-25 11:01:39');


DROP TABLE IF EXISTS `app_route`;
CREATE TABLE `app_route` (
  `id` BIGINT(12) NOT NULL COMMENT '主键',
  `app_id` VARCHAR(32) DEFAULT NULL COMMENT '应用ID',
  `route_id` VARCHAR(32) DEFAULT NULL COMMENT '路由ID',
  `is_debug` TINYINT(1) DEFAULT NULL COMMENT '是否开启debug模式（1:启用，0禁用）',
  `enabled` TINYINT(1) DEFAULT NULL COMMENT '是否启用（1:启用，0禁用）',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='应用路由表';

INSERT  INTO `app_route`(`id`,`app_id`,`route_id`,`is_debug`,`enabled`,`create_time`) VALUES 
(3,'509008971445761792','ewallet-server',0,1,'2019-03-27 16:23:15'),
(4,'509737928172220918','qrcode-server',0,1,'2019-03-27 16:23:28');


DROP TABLE IF EXISTS `app_version_map`;
CREATE TABLE `app_version_map` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` VARCHAR(32) DEFAULT NULL COMMENT '应用id',
  `version_no` VARCHAR(10) DEFAULT NULL COMMENT '当前版本号',
  `path` VARCHAR(30) DEFAULT NULL COMMENT '路径',
  `map_path` VARCHAR(30) DEFAULT NULL COMMENT '映射路径',
  `description` VARCHAR(50) DEFAULT NULL COMMENT '描述',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_KEY1` (`app_id`,`version_no`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='应用版本映射表';

INSERT INTO `app_version_map`(`id`,`app_id`,`version_no`,`path`,`map_path`,`description`,`create_time`) VALUES 
(2,'509008971445761792','2.0','/qrcode','/v2/qrcode','乘车码第二版','2019-03-15 11:55:12');


DROP TABLE IF EXISTS `route`;
CREATE TABLE `route` (
  `id` VARCHAR(50) NOT NULL COMMENT '路由的ID（默认情况下与其映射键相同）。',
  `path` VARCHAR(255) NOT NULL COMMENT '路由的路径, 例： /foo/**.',
  `service_id` VARCHAR(50) DEFAULT NULL COMMENT '要映射到此路由的服务ID，配合服务发现使用。如果没有可以指定物理URL或服务，但不能同时指定两者。',
  `url` VARCHAR(255) DEFAULT NULL COMMENT '要映射到路由的完整物理URL。, 另一种方法是使用服务ID 和服务发现来查找物理地址。',
  `ribbon_url` VARCHAR(255) DEFAULT NULL COMMENT '物理地址负载均衡配置',
  `retryable` TINYINT(1) DEFAULT NULL COMMENT '用于指示此路由应该可重试的标志（如果支持）。 通常，重试需要服务ID和功能区。',
  `strip_prefix` INT(11) DEFAULT NULL COMMENT '标记以确定在转发之前是否应剥离此路由的前缀。例：/api/v1/member/1 --> /member/1',
  `enabled` TINYINT(1) NOT NULL COMMENT '是否启用（1:启用，0禁用）',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='zuul网关路由表';

INSERT  INTO `route`(`id`,`path`,`service_id`,`url`,`ribbon_url`,`retryable`,`strip_prefix`,`enabled`,`description`) VALUES 
('ewallet-server','/ewallet/**',NULL,'http://192.168.35.15:8002','[{\"url\":\"http://127.0.0.1:8002\"}]',0,1,1,'乘车码服务'),
('qrcode-server','/qrcode/**','qrcode-server',NULL,NULL,0,1,1,'银联消费维码二维码服务');