package com.idealo.checkout.counter;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.idealo.checkout.pojo.DiscountRate;
import com.idealo.checkout.pojo.Item;
import com.idealo.checkout.pojo.PricingRule;
import com.idealo.checkout.service.ShoppingCart;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/*
 * Author: Lokesh K Haralakatta
 * CartCheckout class contains references to Shop Inventory and Shopping Cart.
 * Provides methods to perform cart related operations like loading special prices,
 * discount rates, adding items to cart, removing items from cart
 * and computing total payment by applying active discount rates
 */

@Slf4j
@Data
public class CartCheckout {
	private ShoppingCart sCart = new ShoppingCart();
	
	//Method to simulate scanning of item and add to shopping cart with quantity as 1
	public Boolean scanItem(String itemId) {
		Boolean status = true;
		try {
			//Call underlying bulk scan with quantity as 1
			status = scanItem(itemId,1);
		}catch(Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			status = false;
		}
		return status;
	}
	
	//Method to simulate scanning of item and add to shopping cart with bulk quantity
	public Boolean scanItem(String itemId, Integer qty) {
		Boolean status = true;
		try {
			//Check item with scanned item id exists in Shop Inventory
			Item invItem = null;
			if((invItem = ShoppingCart.getShopInventory().searchItem(itemId)) != null) {
				Item X = new Item(invItem);
				X.setQuantity(qty);
				log.debug("Scanning item with id: "+X.getSkuId()+" and adding to shopping cart");
				if(sCart.addItemtoCart(X))
					log.debug("Item: "+X.getSkuId()+" of quantity: "+X.getQuantity()+" is in Cart");
				else {
					log.debug("Item: "+X.getSkuId()+" failed to be added to Cart");
					status = false;
				}
			} else {
				log.warn("Scanned item with id: "+itemId+" is invalid");
				status = false;
			}
		}catch(Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			status = false;
		}
		return status;
	}
	
	//Method to remove item's quantity from the cart.
	public Boolean removeItem(String itemId) {
		Boolean status = true;
		try {
			//Call underlying remove item with quantity as 1
			status = removeItem(itemId,1);
		}catch(Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			status = false;
		}
		return status;
	}
		
	//Method to remove the item's quantity from the cart
	public Boolean removeItem(String itemId, Integer qty) {
		Boolean status = true;
		try {
			//Check item with scanned item id exists in Shop Inventory
			Item invItem = null;
			if((invItem = ShoppingCart.getShopInventory().searchItem(itemId)) != null) {
				Item X = new Item(invItem);
				X.setQuantity(qty);
				log.debug("Removing item quantity: "+qty+" with id: "+X.getSkuId()+" from shopping cart");
				if(sCart.removeItemFromCart(X))
					log.debug("Item: "+X.getSkuId()+" of quantity: "+X.getQuantity()+" removed from Cart");
				else {
					log.debug("Item: "+X.getSkuId()+" failed to be removed from Cart");
					status = false;
				}
			} else {
				log.warn("Scanned item with id: "+itemId+" is invalid");
				status = false;
			}
		}catch(Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			status = false;
		}
		return status;
	}
	
	//Method to search for item in the cart
	public Item searchItem(String id) {
		return sCart.getItemFromCart(id);
	}
	
	//Method to return items count in the cart
	public Integer cartItemsCount() {
		return sCart.getShoppingCartMap().size();
	}
	
	//Method to display cart contents
	public void displayCart() {
		sCart.displayShoppingCartItems();
	}
	
	//Method to calculate total amount for payment by applying applicable
	// special price and discount rates for qualifying items quantity
	public Integer totalAmountForPayment() {
		Integer totalAmt = 0;
		//Parse through each of item present in shopping cart
		//Check and apply special pricing if applicable
	    //Check and apply discount rates if applicable
		//Calculate normal amount for items not qualified for any promotions
		for(String itemId: sCart.getShoppingCartMap().keySet()) {
			PricingRule pRule = null;
			DiscountRate dRate = null;
			if((pRule = ShoppingCart.getShopInventory().searchRule(itemId)) != null) {
				log.debug("Special pricing rule applies for item: "+itemId+
						                       " for min quantity of "+pRule.getQuantity());
				totalAmt += applySpecialPricingRule(pRule,this.searchItem(itemId));
				log.debug("Amount to pay after applying Special Pricing Rules for item:"+itemId+" = "+totalAmt);
			}
			else if((dRate = ShoppingCart.getShopInventory().searchDiscountRate(itemId)) != null) {
				log.debug("Discount rate: "+dRate.getDiscount()+"% applies for item: "
										+itemId+" on occassion of "+dRate.getOccassion());
				totalAmt += applyDiscountRates(dRate,this.searchItem(itemId));
				log.debug("Amount to pay after applying Discount Rate for item:"+itemId+" = "+totalAmt);
			} else {
				//Item is not eligible for any kind of promotion
				Item X = this.searchItem(itemId);
				totalAmt += X.getQuantity()*X.getUnitPrice();
			}
		}
		log.debug("Total Amount to be Paid for items in Shopping Cart: " + totalAmt);
		return totalAmt;
	}
	
	//Method to check and apply special pricing rule on given item
	private Integer applySpecialPricingRule(PricingRule pRule,Item item) {
		Integer specialAmt = 0;
		if(item.getQuantity() >= pRule.getQuantity()) {
			log.debug("Item: "+item.getSkuId()+" is eligible for Special Pricing Rule as it's quantity"
														+ " in cart: "+item.getQuantity());
			//Repeat applying special price till cart quantity is more than bulk quantity
			Integer cartQty = item.getQuantity();
			do {
				cartQty = cartQty - pRule.getQuantity();
				specialAmt += pRule.getBulkPrice();
			}while(cartQty >= pRule.getQuantity());
			//Apply unit price for left over cart quantity
			if(cartQty > 0) {
				log.debug("Applying unit price: "+item.getUnitPrice()+" for left over quantity of "+cartQty);
				specialAmt += cartQty*item.getUnitPrice();
			}
		} else {
			log.debug("Item: "+item.getSkuId()+" is not eligible for Special Pricing Rule as it's quantity"
					+ " in cart: "+item.getQuantity());
			//Applying unit price for item quantity
			specialAmt = item.getQuantity()*item.getUnitPrice();
		}
		
		log.debug("Amount computed for item: "+item.getSkuId()+" after applying SPR: "+specialAmt);
		return specialAmt;
	}
	
	//Method to apply qualified discount rate for the eligible items
	private Integer applyDiscountRates(DiscountRate dRate,Item item) {
		log.debug("Applying discount rate: "+dRate.getDiscount()+"% for item: "+item.getSkuId()+
				" on the occassion of "+dRate.getOccassion());
		Integer itemAmt = item.getQuantity()*item.getUnitPrice();
		log.debug("Item amount for quanity of "+item.getQuantity()+" before discount : "+itemAmt);
		Integer discountedAmt = (itemAmt * dRate.getDiscount())/100;
		itemAmt -= discountedAmt;
		log.debug("Item amount after applying discount rate of "+dRate.getDiscount()+"% : "+itemAmt);
		
		return itemAmt;
	}
}
