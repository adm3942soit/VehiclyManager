-- auto-generated definition
create table carmanager.credit_card
(
  NUMBER varchar(50) not null,
  CVV2 int not null,
  TYPE varchar(10) not null,
  EXPIRE_YEAR int not null,
  EXPIRE_MONTH int not null,
  ID bigint auto_increment
    primary key,
  CREATED datetime not null,
  UPDATED datetime not null,
  constraint credit_card_NUMBER_uindex
  unique (NUMBER),
  constraint credit_card_ID_uindex
  unique (ID)
)
