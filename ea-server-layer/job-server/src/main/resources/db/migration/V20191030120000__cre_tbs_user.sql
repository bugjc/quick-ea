
DROP TABLE IF EXISTS `tbs_job`;

CREATE TABLE `tbs_job` (
                           `job_id` varchar(32) NOT NULL COMMENT '主键，业务方提供的唯一key',
                           `exec_time` datetime DEFAULT NULL COMMENT '执行时间',
                           `http_callback_info` varchar(512) DEFAULT NULL COMMENT 'http回调参数信息',
                           `status` smallint(4) DEFAULT '0' COMMENT '状态(0:创建任务,1:执行任务中,2:任务执行成功,3:任务执行失败)',
                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                           PRIMARY KEY (`job_id`),
                           KEY `ID_INDEX` (`job_id`),
                           KEY `REMAINING_SECONDS_INDEX` (`exec_time`),
                           KEY `STATUS_INDEX` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单定时任务表';

/*Table structure for table `tbs_job_log` */

DROP TABLE IF EXISTS `tbs_job_log`;

CREATE TABLE `tbs_job_log` (
                               `job_log_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `job_id` VARCHAR(32) NOT NULL COMMENT '任务ID',
                               `http_address` VARCHAR(100) DEFAULT NULL COMMENT 'http回调地址',
                               `http_param` VARCHAR(512) DEFAULT NULL COMMENT 'http回调参数',
                               `trigger_time` DATETIME DEFAULT NULL COMMENT '调度时间',
                               `trigger_code` VARCHAR(100) DEFAULT NULL COMMENT '调度结果',
                               `trigger_msg` VARCHAR(255) DEFAULT NULL COMMENT '调度日志',
                               `time_consuming` INT(11) DEFAULT NULL COMMENT '回调耗时(单位：秒)',
                               PRIMARY KEY (`job_log_id`)
) ENGINE=INNODB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;


