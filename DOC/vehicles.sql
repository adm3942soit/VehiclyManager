-- auto-generated definition
create table carmanager.vehicles
(
	ID bigint not null auto_increment
		primary key,
	VEHICLE_NMBR varchar(30) null,
	LICENSE_NMBR varchar(100) not null,
	MAKE varchar(100) null,
	MODEL varchar(100) null,
	YEAR int null,
	STATUS varchar(100) null,
	VEHICLE_TYPE varchar(100) null,
	ACTIVE bit default b'0' not null,
	LOCATION varchar(100) null,
	VIN_NUMBER varchar(50) null,
	CREATED datetime  null,
	UPDATED datetime  null,
	PRICE        DOUBLE DEFAULT '0'                 NULL,
	PRICE_WEEK  DOUBLE DEFAULT '0'                      NULL,
	PRICE_MONTH DOUBLE DEFAULT '0'                      NULL,
	PRICE_DAY   DOUBLE DEFAULT '0'                      NULL,

	constraint vehicles_ID_uindex
	unique (ID),
	constraint vehicles_vehicleNmbr_uindex
	unique (VEHICLE_NMBR),
	constraint vehicles_LICENSE_NMBR_uindex
	unique (LICENSE_NMBR)
)
;


