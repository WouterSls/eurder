create table orders
(
	id 		uuid,
	total_price	double precision,
	fk_customer_id	bigint,
	foreign key(fk_customer_id) references customers(id),
	primary key(id)
);
