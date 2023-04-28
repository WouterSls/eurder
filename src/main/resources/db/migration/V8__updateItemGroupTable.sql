alter table item_groups
	add column fk_order_id UUID;
alter table item_groups
	add constraint fk_order_id foreign key (fk_order_id)
		references purchase_orders(id);
