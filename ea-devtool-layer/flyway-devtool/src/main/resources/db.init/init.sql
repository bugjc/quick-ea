
DROP DATABASE IF EXISTS `gl_consume_code` ;
CREATE DATABASE	`gl_consume_code` DEFAULT CHARSET utf8;

CREATE USER 'gl_consume_code'@'%' IDENTIFIED BY 'gl_consume_code2018';

GRANT ALL PRIVILEGES ON gl_consume_code.* TO 'gl_consume_code'@'localhost' WITH GRANT OPTION;

USE gl_consume_code;
