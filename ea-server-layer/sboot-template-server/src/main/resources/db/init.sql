-- 创建数据库和账户
CREATE DATABASE IF NOT EXISTS gl_charge_prod COLLATE = 'utf8_general_ci' CHARACTER SET = 'utf8';
GRANT ALL ON gl_charge_prod.* TO 'charge'@'%' IDENTIFIED BY 'charge2019';
GRANT ALL ON gl_charge_prod.* TO 'charge'@'localhost' IDENTIFIED BY 'charge2019';
USE `gl_charge_prod`;


DROP TABLE IF EXISTS `tbs_order`;

CREATE TABLE `tbs_order` (
  `id` varchar(27) COLLATE utf8mb4_bin NOT NULL COMMENT '充电订单号',
  `order_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '特来电充电订单号',
  `order_status` int(4) DEFAULT NULL COMMENT '充电订单状态',
  `pay_status` int(4) DEFAULT NULL COMMENT '支付状态',
  `connector_status` int(4) DEFAULT NULL COMMENT '充电设备接口状态',
  `connector_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '充电设备接口编码',
  `start_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始充电时间',
  `end_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '结束充电时间',
  `total_power` double(10,2) DEFAULT '0.00' COMMENT '累计充电量',
  `total_elec_money` double(10,2) DEFAULT '0.00' COMMENT '总电费',
  `total_service_money` double(10,2) DEFAULT '0.00' COMMENT '总服务费',
  `total_money` double(10,2) DEFAULT '0.00' COMMENT '累计总金额',
  `stop_reason` int(4) DEFAULT NULL COMMENT '充电结束原因',
  `sum_period` int(4) DEFAULT NULL COMMENT '时段数N',
  `user_id` int(12) DEFAULT NULL COMMENT '用户ID',
  `version` int(9) DEFAULT '0' COMMENT '版本',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `station_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '电站ID',
  `operator_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '运营商ID',
  `equipment_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '设备唯一ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`id`) USING BTREE,
  UNIQUE KEY `te_ld_order_id` (`order_id`),
  KEY `user_id` (`user_id`),
  KEY `station_id` (`station_id`),
  KEY `operator_id` (`operator_id`),
  KEY `connector_id` (`connector_id`),
  KEY `equipment_id` (`equipment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='充电订单表';

/*Table structure for table `tbs_order_detail` */

DROP TABLE IF EXISTS `tbs_order_detail`;

CREATE TABLE `tbs_order_detail` (
  `order_detail_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '单时段充电明细一致性HASHID',
  `order_id` varchar(27) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '充电订单号',
  `detail_start_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `detail_end_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '结束时间',
  `elec_price` double(10,4) DEFAULT '0.0000' COMMENT '时段电价',
  `service_price` double(10,4) DEFAULT '0.0000' COMMENT '时段服务费价格',
  `detail_power` double(10,2) DEFAULT '0.00' COMMENT '时段充电量，单位：度',
  `detail_elec_money` double(10,2) DEFAULT '0.00' COMMENT '时段电费',
  `detail_service_money` double(10,2) DEFAULT '0.00' COMMENT '时段服务费',
  `user_id` int(12) DEFAULT NULL COMMENT '用户ID',
  `version` int(12) DEFAULT '0' COMMENT '版本',
  PRIMARY KEY (`order_detail_id`),
  KEY `order_detail_id` (`order_detail_id`),
  KEY `order_id` (`order_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='充电订单明细表\r\n';

/*Table structure for table `tbs_station` */

DROP TABLE IF EXISTS `tbs_station`;

CREATE TABLE `tbs_station` (
  `id` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '充电站ID（运营商自定义的唯一编码）',
  `operator_id` varchar(9) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '运营商ID',
  `equipment_owner_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '设备所有者ID（设备所属运营平台组织机构代码）',
  `station_name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '充电站名称',
  `country_code` varchar(2) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '充电站国家代码',
  `area_code` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '充电站省市辖区编码',
  `address` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '详细地址',
  `station_tel` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '站点电话（能够联系场站工作人员进行协 助的联系电话）',
  `service_tel` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '服务电话（平台服务电话，例如400的电 话）',
  `station_type` int(4) DEFAULT NULL COMMENT '站点类型',
  `station_status` int(4) DEFAULT NULL COMMENT '站点状态',
  `park_nums` int(4) DEFAULT '0' COMMENT '车位数量',
  `station_lng` double(10,6) DEFAULT '0.000000' COMMENT '经度',
  `station_lat` double(10,6) DEFAULT '0.000000' COMMENT '维度',
  `site_guide` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '站点引导',
  `construction` int(4) DEFAULT NULL COMMENT '建设场所',
  `match_cars` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '使用车型描述（描述该站点接受的车大小以及 类型，如大巴、物流车、私家乘 用车、出租车等）',
  `park_info` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '车位楼层及数量描述',
  `busine_hours` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '营业时间',
  `electricity_fee` text COLLATE utf8mb4_bin COMMENT '充电电费率',
  `service_fee` text COLLATE utf8mb4_bin COMMENT '服务费率',
  `park_fee` text COLLATE utf8mb4_bin COMMENT '停车费',
  `payment` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '付款方式',
  `support_order` int(2) DEFAULT NULL COMMENT '是否支持预约',
  `remark` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `station_id` (`id`),
  KEY `operator_id` (`operator_id`),
  KEY `equipment_owner_id` (`equipment_owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='电站表';

/*Table structure for table `tbs_station_equipment` */

DROP TABLE IF EXISTS `tbs_station_equipment`;

CREATE TABLE `tbs_station_equipment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `equipment_id` varchar(23) COLLATE utf8mb4_bin NOT NULL COMMENT '设备唯一编码，对同一运营 商，保证唯一',
  `equipment_name` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '设备名称',
  `manufacturer_id` varchar(9) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '设备生产商组织机 构代码',
  `manufacturer_name` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '设备生产商名称',
  `equipment_model` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '设备型号',
  `production_date` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '生产日期',
  `equipment_type` int(2) DEFAULT NULL COMMENT '设备类型',
  `equipment_lng` double(10,6) DEFAULT '0.000000' COMMENT '经度',
  `equipment_lat` double(10,6) DEFAULT '0.000000' COMMENT '纬度',
  `power` double(12,1) DEFAULT '0.0' COMMENT '充电设备总功率，单位：kW',
  `station_id` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '电站id',
  `operator_id` varchar(9) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '运营商ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `equipment_id_2` (`equipment_id`,`station_id`),
  KEY `equipment_id` (`equipment_id`),
  KEY `manufacturer_id` (`manufacturer_id`),
  KEY `station_id` (`station_id`),
  KEY `operator_id` (`operator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1168922 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='充电设备信息';

/*Table structure for table `tbs_station_equipment_connector` */

DROP TABLE IF EXISTS `tbs_station_equipment_connector`;

CREATE TABLE `tbs_station_equipment_connector` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `connector_id` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '充电设备接口编码，同一运营商内唯一',
  `connector_name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '充电设备接口名称',
  `connector_type` int(11) unsigned zerofill DEFAULT NULL COMMENT '充电设备接口类型',
  `voltage_upper_limits` int(11) DEFAULT NULL COMMENT '额定电压上限,单位：V',
  `voltage_lower_limits` int(11) DEFAULT NULL COMMENT '额定电压下限,单位：V',
  `current` int(11) DEFAULT NULL COMMENT '额定电流,单位：A',
  `power` double DEFAULT NULL COMMENT '额定功率，单位：kW',
  `park_no` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '车位号(停车场车位编号)',
  `national_standard` int(11) DEFAULT NULL COMMENT '国家标准',
  `equipment_id` varchar(23) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '设备唯一编码，对同一运营 商，保证唯一',
  `station_id` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '电站ID',
  `operator_id` varchar(9) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '运营商ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `connector_id` (`connector_id`,`station_id`) USING BTREE,
  KEY `id` (`id`),
  KEY `connector_id_2` (`connector_id`),
  KEY `equipment_id` (`equipment_id`),
  KEY `station_id` (`station_id`),
  KEY `operator_id` (`operator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1168960 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='充电设备接口信息表';

/*Table structure for table `tbs_station_picture` */

DROP TABLE IF EXISTS `tbs_station_picture`;

CREATE TABLE `tbs_station_picture` (
  `id` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片地址',
  `station_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '电站ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `station_id` (`station_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='电站图片表';

/*Table structure for table `tbs_token` */

DROP TABLE IF EXISTS `tbs_token`;

CREATE TABLE `tbs_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operator_id` varchar(27) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '运营商ID',
  `access_token` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '接口调用凭证',
  `token_available_time` int(8) DEFAULT NULL COMMENT '凭证有效期，单位秒',
  `status` int(4) DEFAULT NULL COMMENT 'token状态',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `operator_id_index` (`operator_id`),
  KEY `access_token_index` (`access_token`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;