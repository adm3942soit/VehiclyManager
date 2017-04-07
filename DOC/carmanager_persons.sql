create table persons
(
	ID bigint not null auto_increment
		primary key,
	CREATED datetime default CURRENT_TIMESTAMP not null,
	EMAIL varchar(255) not null,
	FIRST_NAME varchar(255) not null,
	LAST_NAME varchar(255) not null,
	LOGIN varchar(255) null,
	NOTES longtext null,
	PASSWORD varchar(255) null,
	PICTURE varchar(255) null,
	REMIND tinyint(1) default '0' null,
	UPDATED datetime default CURRENT_TIMESTAMP not null,
	BIRTH_DATE date null,
	constraint EMAIL
		unique (EMAIL),
	constraint persons_LOGIN_uindex
		unique (LOGIN)
)
;

