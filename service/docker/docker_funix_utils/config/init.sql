CREATE DATABASE IF NOT EXISTS funixgaming_web;
CREATE DATABASE IF NOT EXISTS funixgaming_web_docker;

CREATE USER 'funix'@'%' IDENTIFIED BY 'funix';
GRANT ALL PRIVILEGES ON *.* TO 'funix'@'%';
