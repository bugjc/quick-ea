-- 创建数据库和账户
CREATE DATABASE IF NOT EXISTS ea_job_dev COLLATE = 'utf8_general_ci' CHARACTER SET = 'utf8';
CREATE USER 'job'@'%' IDENTIFIED BY 'job2019';
CREATE USER 'job'@'localhost' IDENTIFIED BY 'job2019';
FLUSH PRIVILEGES;
GRANT ALL ON `ea_job_dev`.* TO 'job'@'%' WITH GRANT OPTION;
GRANT ALL ON `ea_job_dev`.* TO 'job'@'localhost' WITH GRANT OPTION;
USE `ea_job_dev`;
