-- 创建数据库和账户
CREATE DATABASE IF NOT EXISTS ea_test COLLATE = 'utf8_general_ci' CHARACTER SET = 'utf8';
GRANT ALL ON ea_test.* TO 'test'@'%' IDENTIFIED BY 'test2019';
GRANT ALL ON ea_test.* TO 'test'@'localhost' IDENTIFIED BY 'test2019';
USE `ea_test`;