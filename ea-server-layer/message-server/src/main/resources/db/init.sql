-- 创建数据库和账户
CREATE DATABASE IF NOT EXISTS ea_test COLLATE = 'utf8_general_ci' CHARACTER SET = 'utf8';
GRANT ALL ON ea_test.* TO 'test'@'%' IDENTIFIED BY 'test2019';
GRANT ALL ON ea_test.* TO 'test'@'localhost' IDENTIFIED BY 'test2019';
USE `ea_test`;


DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `pwd` varchar(100) DEFAULT NULL COMMENT '密码',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`pwd`,`age`,`email`) values
(1,'Jone','123456',18,'test1@baomidou.com'),
(2,'Jack','123456',20,'test2@baomidou.com'),
(3,'Tom','123456',28,'test3@baomidou.com'),
(4,'Sandy','123456',21,'test4@baomidou.com'),
(5,'Billie','123456',24,'test5@baomidou.com');