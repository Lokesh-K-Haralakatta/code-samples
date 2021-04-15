package com.idealo.checkout.pojo;

import lombok.Data;

/*
 * Author: Author: Lokesh K Haralakatta
 * Pricing rule class to help in build special pricing rules for items.
 * Used to populate rule objects from JSON Data
 */
@Data
public class PricingRule {
	private String skuId;
	private Integer quantity;
	private Integer bulkPrice;
	
	@Override
	public String toString() {
		return skuId+" : "+quantity+" : "+bulkPrice;
	}
}
