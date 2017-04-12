-- auto-generated definition
create table renta_history
(
  ID bigint not null auto_increment
    primary key,
  CREATED timestamp default CURRENT_TIMESTAMP not null,
  UPDATED timestamp default '0000-00-00 00:00:00' not null,
  PERSON bigint null,
  VEHICLE bigint null,
  `FROM` timestamp null,
  `TO` timestamp null,
  PRICE double null,
  SUMMA double null,
  PAID bit default b'0' null,
  constraint renta_history_ID_uindex
  unique (ID)
)
