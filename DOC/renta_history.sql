-- auto-generated definition
create table renta_history
(
  ID bigint not null auto_increment
    primary key,
  CREATED timestamp not null,
  UPDATED timestamp not null,
  PERSON bigint null,
  VEHICLE bigint null,
  `FROM` timestamp null,
  `TO` timestamp null,
  PRICE double null,
  SUMMA double null,
  PAID bit default '0' null,
  constraint renta_history_ID_uindex
  unique (ID)
)
