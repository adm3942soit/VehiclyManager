create table vehicle_models
(
	ID bigint not null auto_increment
		primary key,
	CREATED datetime not null,
	UPDATED datetime not null,
	VEHICLE_TYPE bigint not null,
	MODEL varchar(100) not null,
	COMMENT varchar(100) null,
	constraint `vehicle-models_ID_uindex`
	unique (ID)
)
;
;
