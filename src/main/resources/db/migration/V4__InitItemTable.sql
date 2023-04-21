create table items
(
	id		UUID PRIMARY KEY,
	name		varchar(255),
	description	varchar(255),
	price		decimal,
	amount		integer,
	urgency		varchar(50)
);
