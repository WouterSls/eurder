Create table item_groups(
	id	 	bigint,
	fk_item_id	 	UUID,
	amount_ordered 	integer,
	foreign key (fk_item_id) references items (id),
	primary key (id)
);
