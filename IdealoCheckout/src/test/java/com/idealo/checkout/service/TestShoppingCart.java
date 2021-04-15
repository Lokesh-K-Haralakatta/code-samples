package com.idealo.checkout.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.idealo.checkout.pojo.Item;

@RunWith(JUnit4.class)
public class TestShoppingCart {
	private static ShoppingCart sCart = new ShoppingCart();
	
	@Test
	public void testAddItemsToCart() {
		//Get item present in Shop Inventory
		String itemId = "skuA";
		Item X = new Item(ShoppingCart.getShopInventory().searchItem(itemId));
		
		//Set required quantity for the item
		Integer quantity = 100;
		X.setQuantity(quantity);
		
		//Get current items count in shopping cart before adding
		Integer itemsCount = sCart.getShoppingCartMap().size();
		
		//Add new item into shopping cart
		assertTrue("Adding item to cart failed", sCart.addItemtoCart(X));
		
		//Validate items count in cart is increased by 1
		Integer itemsCountAfterAdd = sCart.getShoppingCartMap().size();
		assertEquals("Items count in cart is not as expected: "+itemsCount+1,
						itemsCountAfterAdd,new Integer(itemsCount+1));
		
		//Get newly added item from cart
		Item cartItem = sCart.getItemFromCart(itemId);
		
		//Validate item details added to cart
		assertEquals("Item Id in shopping cart is not as expected: "+itemId,cartItem.getSkuId(),itemId);
		assertEquals("Item quantity in shopping cart is not as expected: "+quantity,cartItem.getQuantity(),new Integer(quantity));
		
		//Dump shopping cart items
		sCart.displayShoppingCartItems();
		
		//Try adding the same item again and validate the adjustment in item's quantity
		Integer rQuantity = cartItem.getQuantity() + quantity;
		assertTrue("Adding same item to cart failed", sCart.addItemtoCart(X));
		
		//Validate items count in cart remain same
		assertEquals("Items count in cart is not as expected: "+itemsCountAfterAdd,
				itemsCountAfterAdd,new Integer(sCart.getShoppingCartMap().size()));

		//Get newly added item from cart
		cartItem = sCart.getItemFromCart(itemId);
		//Validate item details added to cart
		assertEquals("Item Id in shopping cart is not as expected: "+itemId,cartItem.getSkuId(),itemId);
		assertEquals("Item quantity in shopping cart is not as expected: "+rQuantity,cartItem.getQuantity(),new Integer(rQuantity));
		
		//Dump shopping cart items
		sCart.displayShoppingCartItems();
		
		//Try adding the same item again and validate it fails due to insufficient quantity availability 
		//assertFalse("Adding same item to cart successfull but expected is failure due to insufficient quantity",
			//																		sCart.addItemtoCart(X));
		
		//Get anther item present in Shop Inventory
		itemId = "skuE";
		X = new Item(ShoppingCart.getShopInventory().searchItem(itemId));

		//Set required quantity for the item
		quantity = 512;
		X.setQuantity(quantity);

		//Get current items count in shopping cart before adding
		itemsCount = sCart.getShoppingCartMap().size();

		//Add new item into shopping cart
		assertTrue("Adding item to cart failed", sCart.addItemtoCart(X));

		//Validate items count in cart is increased by 1
		itemsCountAfterAdd = sCart.getShoppingCartMap().size();
		assertEquals("Items count in cart is not as expected: "+itemsCount+1,
				itemsCountAfterAdd,new Integer(itemsCount+1));

		//Get newly added item from cart
		cartItem = sCart.getItemFromCart(itemId);

		//Validate item details added to cart
		assertEquals("Item Id in shopping cart is not as expected: "+itemId,cartItem.getSkuId(),itemId);
		assertEquals("Item quantity in shopping cart is not as expected: "+quantity,cartItem.getQuantity(),new Integer(quantity));

		//Dump shopping cart items
		sCart.displayShoppingCartItems();
	}
	
	@Test
	public void testAddNonExistingIteminInventoryToCart() {
		//Create an item instance which is not present in Shop Inventory
		String itemId = "skuNotPresent";
		String itemName = "item-Not-Present";
		Integer quantity = 0;
		Integer price = 0;
		Item X = new Item(itemId,itemName,quantity,price);
		
		//Try adding the not existing item in Shop Inventory
		//Validate it fails due to not existing of item in inventory 
		assertFalse("Adding not existing item to cart successfull but expected is failure",sCart.addItemtoCart(X));
	}
	
	@Test
	public void testRemoveItemsFromCart() {
		//Get item present in Shop Inventory
		String itemId = "skuC";
		Item X = new Item(ShoppingCart.getShopInventory().searchItem(itemId));

		//Set required quantity for the item
		Integer quantity = 512;
		X.setQuantity(quantity);

		//Add new item into shopping cart
		assertTrue("Adding item to cart failed", sCart.addItemtoCart(X));
		
		//Dump shopping cart items
		sCart.displayShoppingCartItems();
		
		//Get current items count in shopping cart before adding
		Integer itemsCount = sCart.getShoppingCartMap().size();
		
		//Remove 256 quantity for the item in Cart
		X = new Item(ShoppingCart.getShopInventory().searchItem(itemId));
		quantity = 256;
		X.setQuantity(quantity);
		assertTrue("Removing item from cart failed",sCart.removeItemFromCart(X));
		
		//Dump shopping cart items
		sCart.displayShoppingCartItems();
				
		//Validate there are 256 quantity left in cart for the item
		assertEquals("Item Id in shopping cart is not as expected: "+itemId,
					                   sCart.getItemFromCart(itemId).getSkuId(),itemId);
		assertEquals("Item quantity in shopping cart is not as expected: "+quantity,
				        sCart.getItemFromCart(itemId).getQuantity(),new Integer(quantity));

		//Dump shopping cart items
		sCart.displayShoppingCartItems();
		
		//Remove again 256 quantity for the item
		assertTrue("Removing item from cart failed",sCart.removeItemFromCart(X));
		
		//Validate item is removed from the cart as the quantity turns to 0 in cart
		assertNull("Item exists in Cart",sCart.getItemFromCart(itemId));
		
		//Validate items count in cart is decreased by 1
		assertEquals("Items count in cart is not as expected: "+(itemsCount-1),
						new Integer(sCart.getShoppingCartMap().size()),new Integer(itemsCount-1));
		
		//Dump shopping cart items
		sCart.displayShoppingCartItems();

		//Try to remove the item again should fail
		assertFalse("Removing item from cart succeeded",sCart.removeItemFromCart(X));
	}
}
