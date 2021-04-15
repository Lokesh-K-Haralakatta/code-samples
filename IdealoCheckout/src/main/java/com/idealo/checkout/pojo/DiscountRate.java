package com.idealo.checkout.pojo;

import java.util.Date;

import lombok.Data;

/*
 * Author: Author: Lokesh K Haralakatta
 * Discount Rate class to build discount rate objects from JSON Data
 */
@Data
public class DiscountRate {
	private String skuId;
	private Integer discount;
	private Date date;
	private String occassion;
	
	@Override
	public String toString() {
		return skuId+" : "+discount+" : "+date.toString()+" : "+occassion;
	}
}
