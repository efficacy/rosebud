drop table if exists attribute;
create table attribute (
  src varchar(128),
  rel varchar(128),
  seq bigint,
  dest text,
  modified timestamp,
  primary key (src,rel,seq)
);
