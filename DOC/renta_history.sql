-- auto-generated definition
-- auto-generated definition
create table carmanager.renta_history
(
  ID bigint not null auto_increment
    primary key,
  CREATED timestamp not null,
  UPDATED timestamp not null,
  PERSON varchar(255) null,
  VEHICLE varchar(255) null,
  PRICE double default '0' null,
  SUMMA double default '0' null,
  PAID bit default b'0' null,
  FROM_DATE timestamp  not null,
  TO_DATE timestamp  not null,
  constraint renta_history_ID_uindex
  unique (ID),
  constraint renta_history_persons_NAME_fk
  foreign key (PERSON) references persons (NAME),
  constraint renta_history_vehicles_VEHICLE_NMBR_fk
  foreign key (VEHICLE) references vehicles (VEHICLE_NMBR)
)
;

create index renta_history_persons_NAME_fk
  on renta_history (PERSON)
;

create index renta_history_vehicles_VEHICLE_NMBR_fk
  on renta_history (VEHICLE)
;

comment on column renta_history.SUMMA is '
'

