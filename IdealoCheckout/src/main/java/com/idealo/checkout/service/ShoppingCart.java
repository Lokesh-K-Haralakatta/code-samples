package com.idealo.checkout.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.idealo.checkout.pojo.Item;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/*
 * Author: Author: Lokesh K Haralakatta
 * ShoppingCart class represents items and methods used to place into cart.
 * Has static reference to Shop Inventory Instance to sync with items stock
 */

@Slf4j
@Data
public class ShoppingCart {
	//ShopInventory of items
	private static ShopInventory sInventory = new ShopInventory();
	
	//ShoppingCart Items
	private Map<String,Item> shoppingCartMap = new ConcurrentHashMap<>();
	
	//Load Shop Inventory Items, Special Pricing Rules and Discount rates
	static {
		sInventory.loadInventoryItems();
		sInventory.loadPricingRules();
		sInventory.loadDiscountRates();
	}
	
	//Method to return reference to Shop Inventory
	public static ShopInventory getShopInventory() {
		return sInventory;
	}
	
	//Method to update given item details to Shop Inventory
	private void updateShopInventory(Item invItem) {
		String id = invItem.getSkuId();
		if(sInventory.updateItem(invItem))
			log.debug("Shop Inventory updated with item's quantity for item: "+id);
		else
			log.warn("Updating Shop Inventory for failed for item: "+id);
			
		//Dump latest available quantity for the item in inventory
		Integer invQuantity = sInventory.searchItem(invItem.getSkuId()).getQuantity();
		log.debug("Latest available quantity in Shop Inventory for item: "+id+" = "+invQuantity);
	}
	
	//Method to add given item to Shopping Cart
	public Boolean addItemtoCart(Item X) {
		Boolean status = true;
		try {
			String id = X.getSkuId();
			//Check given item exists in Shop Inventory
			Item invItem = null;
			if((invItem = sInventory.searchItem(id)) != null) {
				Integer availableQty = invItem.getQuantity();
				Integer reqdQty = X.getQuantity();
				//Check there are enough available quantities for the item in inventory
				if(availableQty >= reqdQty) {
					log.debug("Total quantity available in Shop Inventory for item: "+id+" = "+availableQty);
					log.debug("Required quantity to be added to cart for item: "+id+" = "+reqdQty);
					
					//Check if given item already present in Shopping Cart
					//If so, increase the quantity for the item
					if(shoppingCartMap.containsKey(id)) {
						log.debug("Shopping Cart already has item with id: "+id+
								" , increasing the quantity by "+reqdQty);
						shoppingCartMap.get(id).increaseQuantity(reqdQty);
					} else {
						log.debug("Shopping cart does not contain the item: "+id+", adding it to cart");
						shoppingCartMap.put(id, X);
					}
					
					//Update Shop Inventory for this item
					invItem.setQuantity(availableQty-reqdQty);
					updateShopInventory(invItem);
					
				} else {
					log.warn("Insufficient quantity available for item with id: "+id+" in Shop Inventory");
					status = false;
				}
			} else {
				log.error("Given Item with id: "+id+" not present in Shop Inventory of Items,"
						                                   + " cannot add to cart");
				status = false;
			}
		} catch(Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			status = false;
		}
		return status;
	}

	//Method to delete given item from Shopping Cart
	public Boolean removeItemFromCart(Item X) {
		Boolean status = true;
		try {
			String id = X.getSkuId();
			//Check if given item present in Shopping Cart
			//If so, adjust the quantity for the item
			if(shoppingCartMap.containsKey(id)) {
				Integer releasingQty = X.getQuantity();
				log.debug("Shopping Cart contains item with id: "+id+" , decreasing the quantity by "+releasingQty);
				shoppingCartMap.get(id).decreaseQuantity(releasingQty);
				
				//Update release of item's quantity into Shop Inventory
				Item invItem = sInventory.searchItem(id);
				Integer invQuantity = invItem.getQuantity();
				log.debug("Item Quantity in Shop Inventory for item: "+id+" = "+invQuantity);
				//Update Shop Inventory for this item
				invItem.setQuantity(invQuantity+releasingQty);
				updateShopInventory(invItem);
				
				//If item's quantity is <= 0, remove it from the cart
				if(shoppingCartMap.get(id).getQuantity() <= 0) {
					log.debug("Item's quantity after adjustment is <=0 for item: "+id+
							                             ", removing it from the cart");
					shoppingCartMap.remove(id);
				}
			} else {
				log.debug("Shopping cart does not contain the item:"+id+" to remove");
				status = false;
			}
		} catch(Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			status = false;
		}
		return status;
	}
	
	//Method to get an item from Shopping Cart
	public Item getItemFromCart(String id) {
		if(shoppingCartMap.containsKey(id)) {
			Item x = shoppingCartMap.get(id);
			log.debug("Item: "+x.getSkuId()+" exists in cart, returning it");
			return x;
		}
		else {
			log.debug("Item: "+id+" not exists in cart, returning null");
			return null;
		}
	}
	
	//Method to dump Shop Inventory Items
	public void displayShoppingCartItems() {
		log.debug("Total Items in ShoppingCart: "+shoppingCartMap.size());
			shoppingCartMap.keySet().forEach(itemId -> {
				log.debug(shoppingCartMap.get(itemId).toString());
		});
	}
}
