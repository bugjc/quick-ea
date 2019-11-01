-- 创建数据库和账户
CREATE DATABASE IF NOT EXISTS ea_zuul_api_gateway COLLATE = 'utf8_general_ci' CHARACTER SET = 'utf8';
GRANT ALL ON ea_zuul_api_gateway.* TO 'gateway'@'%' IDENTIFIED BY 'gateway2018';
GRANT ALL ON ea_zuul_api_gateway.* TO 'gateway'@'localhost' IDENTIFIED BY 'gateway2018';
GRANT SELECT ON *.* TO 'job'@'%';
GRANT SELECT ON *.* TO 'job'@'localhost';
USE `ea_zuul_api_gateway`;