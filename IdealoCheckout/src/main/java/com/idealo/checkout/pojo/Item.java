package com.idealo.checkout.pojo;

import lombok.Data;

/*
 * Author: Author: Lokesh K Haralakatta
 * Item Class to represent each of item going to be present in Shop inventory
 */
@Data
public class Item {
	private String skuId;
	private String name;
	private Integer quantity;
	private Integer unitPrice;
	
	public Item() {}
	
	public Item(String skuId, String name, Integer quantity, Integer unitPrice) {
		super();
		this.skuId = skuId;
		this.name = name;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}
	
	public Item(Item X) {
		super();
		this.skuId = X.skuId;
		this.name = X.name;
		this.quantity = 0;
		this.unitPrice = X.unitPrice;
	}
	
	public void increaseQuantity(int qty) {
		this.quantity += qty;
	}
	
	public void decreaseQuantity(int qty) {
		this.quantity -= qty;
	}
	
	@Override
	public String toString() {
		return this.skuId +":"+ this.name +":"+ this.quantity +":"+ this.unitPrice;
	}
}
