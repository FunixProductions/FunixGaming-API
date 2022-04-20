CREATE DATABASE IF NOT EXISTS funixgaming_web;

CREATE USER 'funix'@'%' IDENTIFIED BY 'funix';
GRANT ALL PRIVILEGES ON *.* TO 'funix'@'%';
