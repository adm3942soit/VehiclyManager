-- auto-generated definition
create table vehicle_types
(
	ID bigint not null auto_increment
		primary key,
	CREATED datetime default CURRENT_TIMESTAMP not null,
	UPDATED datetime default CURRENT_TIMESTAMP not null,
	TYPE varchar(50) not null,
	PICTURE varchar(200) null,
	constraint vehicle_types_ID_uindex
	unique (ID),
	constraint vehicle_types_TYPE_uindex
	unique (TYPE)
)
;
