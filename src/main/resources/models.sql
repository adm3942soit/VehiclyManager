create table carmanager.vehicle_models
(
	ID bigint not null auto_increment
		primary key,
	CREATED datetime not null,
	UPDATED datetime  not null,
	VEHICLE_TYPE varchar(50) not null,
	MODEL varchar(100) not null,
	COMMENT varchar(100) null,
	PICTURE varchar(200) null,
	constraint `vehicle-models_ID_uindex`
	unique (ID),
	constraint vehicle_models_vehicle_types_TYPE_fk
	foreign key (VEHICLE_TYPE) references vehicle_types (TYPE)
)
;

create index vehicle_models_vehicle_types_TYPE_fk
	on vehicle_models (VEHICLE_TYPE)
;

