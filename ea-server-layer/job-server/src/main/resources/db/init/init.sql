-- 创建数据库和账户
CREATE DATABASE IF NOT EXISTS ea_job_dev COLLATE = 'utf8_general_ci' CHARACTER SET = 'utf8';
GRANT ALL ON ea_job_dev.* TO 'job'@'%' IDENTIFIED BY 'job2019';
GRANT ALL ON ea_job_dev.* TO 'job'@'localhost' IDENTIFIED BY 'job2019';
GRANT SELECT ON *.* TO 'job'@'%';
GRANT SELECT ON *.* TO 'job'@'localhost';
USE `ea_job_dev`;
