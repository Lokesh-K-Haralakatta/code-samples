package com.idealo.checkout.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.idealo.checkout.pojo.DiscountRate;
import com.idealo.checkout.pojo.Item;
import com.idealo.checkout.pojo.PricingRule;

@RunWith(JUnit4.class)
public class TestShopInventory {
	private static ShopInventory sInventory = new ShopInventory();
	
	@BeforeClass
	public static void inventorySetup() {
		assertTrue("Loading items inventory failed",sInventory.loadInventoryItems());
		assertTrue("Loading pricing rules failed", sInventory.loadPricingRules());
		assertTrue("Loading discount rates failed", sInventory.loadDiscountRates());
	}
	
	@Test
	public void testLoadedRuleA() {
		//Search and get rule object 
		String id = "skuA";
		PricingRule rule = sInventory.searchRule(id);
		
		//Validate there's rule object for given Id
		assertNotNull("Rule can not be null for id: "+id,rule);
		
		//Validate rule object contents
		Integer quantity = 3;
		Integer bulkPrice = 100;
		assertEquals("Associated skuId is not as expected",rule.getSkuId(),id);
		assertEquals("Associated quantity is not as expected",rule.getQuantity(),quantity);
		assertEquals("Associated bulk price is not as expected",rule.getBulkPrice(),bulkPrice);
	}
	
	@Test
	public void testLoadedRuleB() {
		//Search and get rule object 
		String id = "skuB";
		PricingRule rule = sInventory.searchRule(id);
		
		//Validate there's rule object for given Id
		assertNotNull("Rule can not be null for id:"+id,rule);
		
		//Validate rule object contents
		Integer quantity = 2;
		Integer bulkPrice = 80;
		assertEquals("Associated skuId is not as expected",rule.getSkuId(),id);
		assertEquals("Associated quantity is not as expected",rule.getQuantity(),quantity);
		assertEquals("Associated bulk price is not as expected",rule.getBulkPrice(),bulkPrice);
	}
	
	@Test
	public void testUnknownRule() {
		//Search and get rule object 
		String id = "unknown";
		PricingRule rule = sInventory.searchRule(id);

		//Validate there's rule object for given Id
		assertNull("Rule object should be null for id:"+id,rule);
	}
	
	@Test
	public void testLoadedDiscountRate() throws ParseException {
		//Search and get discount rate object
		String id = "skuE";
		DiscountRate dRate = sInventory.searchDiscountRate(id);
		
		//Validate discount rate contents
		Integer discount = 10;
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2021-04-03"); 
		String occassion = "Good Friday";
		assertEquals("Associated skuId is not as expected",dRate.getSkuId(),id);
		assertEquals("Associated discount is not as expected",dRate.getDiscount(),discount);
		assertEquals("Associated occassion is not as expected",dRate.getOccassion().compareTo(occassion),0);
		assertEquals("Associated date is not as expected", dRate.getDate().compareTo(date),1);
	}
	
	@Test
	public void testItemsInventorySearch() {
		String itemId = "skuA";
		String itemName = "item-A";
		Integer quantity = 1024;
		Integer price = 40;
		
		//Search for an item in inventory 
		Item invItem = sInventory.searchItem(itemId);
		
		//Validate retrieved inventory item contents
		assertNotNull("Item with id: "+itemId+" not exists in Items Inventory",invItem);
		assertEquals("Inventory Item Id not matches with "+itemId,invItem.getSkuId(),itemId);
		assertEquals("Inventory Item Name not matches with "+itemName, invItem.getName(),itemName);
		assertEquals("Inventory Item Quantity not matches with "+quantity, invItem.getQuantity(),quantity);
		assertEquals("Inventory Item Price not matches with "+price, invItem.getUnitPrice(),price);
	}
	
	@Test
	public void testItemsInventoryAddExistingItem() {
		//Create item with already existing item in inventory
		String itemId = "skuA";
		String itemName = "item-A";
		Integer quantity = 1;
		Integer price = 40;
		Item newItem = new Item(itemId,itemName,quantity,price);
		
		//Adding new item as of existing one into shop inventory failes
		assertFalse("Adding an item to inventory failed", sInventory.addItem(newItem));
	}
	
	@Test
	public void testItemsInventoryAddNewItem() {
		//Create new item to be added to inventory
		String itemId = "skuF";
		String itemName = "item-F";
		Integer quantity = 15;
		Integer price = 20;
		Item newItem = new Item(itemId,itemName,quantity,price);
		
		//Get current items count present in inventory
		Integer itemsCount = sInventory.getShopInventoryMap().size();
				
		//Add new item into shop inventory
		assertTrue("Adding an item to inventory failed", sInventory.addItem(newItem));
				
		//Validate items count in shop inventory increased by 1
		assertEquals("Total items in inventory are not expected as "+itemsCount+1,
					new Integer(sInventory.getShopInventoryMap().size()),new Integer(itemsCount+1));
		
		//Validate added item present in map
		Item invItem = sInventory.searchItem(itemId);
		assertNotNull("Item with id: "+itemId+" not exists in Items Inventory",invItem);
		assertEquals("Inventory Item Id not matches with "+itemId,invItem.getSkuId(),itemId);
		assertEquals("Inventory Item Name not matches with "+itemName, invItem.getName(),itemName);
		assertEquals("Inventory Item Quantity not matches with "+quantity, invItem.getQuantity(),quantity);
		assertEquals("Inventory Item Price not matches with "+price, invItem.getUnitPrice(),price);
		
		//Dump Shop Inventory Contents
		sInventory.displayShopInventoryItems();
	}
	
	@Test
	public void testItemsInventoryUpdateExistingItem() {
		//Create item with already existing item in inventory
		String itemId = "skuA";
		String itemName = "item-Z";
		Integer quantity = 500;
		Integer price = 50;
		Item newItem = new Item(itemId,itemName,quantity,price);
		
		//Update existing item's details in inventory
		assertTrue("Updating item's details in inventory failed", sInventory.updateItem(newItem));
		
		//Validate quantity for item updated with latest details
		Item invItem = sInventory.searchItem(itemId);
		assertEquals("Inventory Item Id not matches with "+itemId,invItem.getSkuId(),itemId);
		assertEquals("Inventory Item Name not matches with "+itemName, invItem.getName(),itemName);
		assertEquals("Inventory Item Quantity not matches with "+quantity, invItem.getQuantity(),quantity);
		assertEquals("Inventory Item Price not matches with "+price, invItem.getUnitPrice(),price);
		
		//Dump Shop Inventory Contents
		sInventory.displayShopInventoryItems();
	}
	
	@Test
	public void testItemsInventoryDeleteExistingItem() {
		//Create item with already existing item in inventory but with more or equal quantity than inventory
		String itemId = "skuB";
		String itemName = "item-B";
		Integer quantity = 1024;
		Integer price = 60;
		Item newItem = new Item(itemId,itemName,quantity,price);
		
		//Delete existing item from inventory
		assertTrue("Delete an item to inventory failed", sInventory.deleteItem(newItem));
		
		//Validate item is deleted from inventory 
		assertNull("Item with id: "+itemId+" still exists in Shop Inventory",sInventory.searchItem(itemId));
		
		//Dump Shop Inventory Contents
		sInventory.displayShopInventoryItems();
	}
}
