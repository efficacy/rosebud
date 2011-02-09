drop database if exists ${db.schema};
create database ${db.schema};
drop user ${db.user};
create user ${db.user} identified by '${db.password}';
grant all privileges on ${db.schema}.* to ${db.user} identified by '${db.password}';
use ${db.schema};