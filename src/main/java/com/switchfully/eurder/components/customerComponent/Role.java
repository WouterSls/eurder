package com.switchfully.eurder.components.customerComponent;


import com.switchfully.eurder.utils.Feature;

import java.util.ArrayList;
import java.util.List;

enum Role {
	CUSTOMER(
			new ArrayList<>(){{
				add(Feature.CREATE_NEW_ORDER);
				add(Feature.VIEW_MY_ORDERS);
				add(Feature.REORDER_ORDER);
			}}
	),
	ADMIN(
			new ArrayList<>() {{
				add(Feature.VIEW_ALL_CUSTOMERS);
				add(Feature.VIEW_CUSTOMER_BY_ID);
				add(Feature.CREATE_NEW_ITEM);
				add(Feature.UPDATE_ITEM);
				add(Feature.GET_ITEMS);
				add(Feature.GET_ITEMS_ON_URGENCY);
				add(Feature.GET_SHIPPING_LIST);
			}}
	);

	private final List<Feature> features;

	Role(List<Feature> features) {
		this.features = features;
	}

	public boolean hasFeature(Feature feature) {
		return features.contains(feature);
	}
}
