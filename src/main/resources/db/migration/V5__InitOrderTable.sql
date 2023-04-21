create table orders
(
	id 		uuid,
	amount_ordered	integer,
	shipping_date	date,
	total_price	double precision,
	fk_item_id	uuid,
	fk_customer_id	uuid,
	foreign key(fk_item_id) references items(id),
	foreign key(fk_customer_id) references customers(id),
	primary key(id)
);
