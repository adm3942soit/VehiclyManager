create table carmanager.address
(
	ID bigint not null auto_increment
		primary key,
	STREET varchar(30) null,
	CITY varchar(30) null,
	COUNTRY varchar(30) null,
	ZIP varchar(10) null,
	CREATED datetime null,
	UPDATED datetime null,
	constraint address_ID_uindex
	unique (ID)
)
;

