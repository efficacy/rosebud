drop database if exists rosebud;
create database rosebud;
drop user rosebud;
create user rosebud identified by 'rosebud';
grant all privileges on rosebud.* to rosebud identified by 'rosebud';
use rosebud;