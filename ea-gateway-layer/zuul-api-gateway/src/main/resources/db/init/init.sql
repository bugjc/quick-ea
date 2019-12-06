-- 创建数据库和账户
CREATE DATABASE IF NOT EXISTS ea_zuul_api_gateway_dev COLLATE = 'utf8_general_ci' CHARACTER SET = 'utf8';
CREATE USER 'gateway'@'%' IDENTIFIED BY 'gateway2019';
CREATE USER 'gateway'@'localhost' IDENTIFIED BY 'gateway2019';
FLUSH PRIVILEGES;
GRANT ALL ON `ea_zuul_api_gateway_dev`.* TO 'gateway'@'%' WITH GRANT OPTION;
GRANT ALL ON `ea_zuul_api_gateway_dev`.* TO 'gateway'@'localhost' WITH GRANT OPTION;
USE `ea_zuul_api_gateway_dev`;