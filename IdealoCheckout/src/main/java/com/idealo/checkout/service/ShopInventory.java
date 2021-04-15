package com.idealo.checkout.service;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.idealo.checkout.pojo.DiscountRate;
import com.idealo.checkout.pojo.Item;
import com.idealo.checkout.pojo.PricingRule;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/*
 * Author: Lokesh K Haralakatta
 * ShopInventory class represents details about items stock in the shop.
 * Initializes the items stock from provided inventory details through a file.
 * Initializes Special Pricing Rules provided through a file   
 * Initializes Discount Rates for items provied through a file
 */
@Slf4j
@Data
public class ShopInventory {
	private static final String INVENTORY_FILE = "items-details.json";
	private static final String PRICE_RULES_FILE = "price-rules.json";
	private static final String DISCOUNT_RATES_FILE = "discount-rates.json";
	
	private Map<String,Item> shopInventoryMap = new ConcurrentHashMap<>();
	private static Map<String,PricingRule> rulesMap = new ConcurrentHashMap<>();
	private static Map<String,DiscountRate> discountRatesMap = new ConcurrentHashMap<>();
	
	//Method to load items details into shopInventoryMap from INVENTORY_FILE
	public Boolean loadInventoryItems() {
		Boolean loadStatus = true;
		try {
			 //Load inventory file contents into an array
			 ClassLoader classLoader = getClass().getClassLoader();
			 File file = new File(classLoader.getResource(INVENTORY_FILE).getFile());
			 ObjectMapper mapper =new ObjectMapper();
			 Item[] items = mapper.readValue(file, Item[].class);
			 log.debug("Number of items read from "+INVENTORY_FILE+" : "+items.length);
			 
			 //Place loaded inventory items into shopInventoryMap for easy access
			 for(Item X : items) {
				 log.debug(X.toString());
				 shopInventoryMap.put(X.getSkuId(),X);
			 }
				 
			 log.debug("Total items loaded into shopInventoryMap: "+shopInventoryMap.size());
			 
		}catch(Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			loadStatus = false;
		}
		return loadStatus;
	}
	
	//Method to search and return item from Shop Inventory
	public Item searchItem(String id) {
		if(shopInventoryMap.containsKey(id))
			return shopInventoryMap.get(id);
		else {
			log.warn("Item with id: "+id+" not found in items inventory");
			return null;
		}
	}
	
	//Method to load special pricing rules from PRICE_RULES_FILE
	public Boolean loadPricingRules() {
		Boolean loadStatus = true;
		try {
			 //Load pricing rules file contents into an array
			 ClassLoader classLoader = getClass().getClassLoader();
			 File file = new File(classLoader.getResource(PRICE_RULES_FILE).getFile());
			 ObjectMapper mapper =new ObjectMapper();
			 PricingRule[] rules = mapper.readValue(file, PricingRule[].class);
			 log.debug("Number of rules read from "+PRICE_RULES_FILE+" : "+rules.length);
			 
			 //Place loaded rules into rulesMap for easy access
			 for(PricingRule X : rules) {
				 log.debug(X.toString());
				 rulesMap.put(X.getSkuId(),X);
			 }				 
			 log.debug("Total rules loaded into rulesMap: "+rulesMap.size());
			 
		}catch(Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			loadStatus = false;
		}
		return loadStatus;
	}
	
	//Method to search for rule based on given Id and return rule object
	public PricingRule searchRule(String id) {
		if(rulesMap.containsKey(id)) {
			log.debug("RulesMap contains rule with given Id:"+id+", returning rule object");
			return rulesMap.get(id);
		} else {
			log.debug("RulesMap doesnot contain rule with given Id:"+id+", returning null");
			return null;
		}
	}
	
	//Method to load discount rates from DISCOUNT_RATES_FILE
	public Boolean loadDiscountRates() {
		Boolean loadStatus = true;
		try {
			//Load discount rates file contents into an array
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(DISCOUNT_RATES_FILE).getFile());
			ObjectMapper mapper =new ObjectMapper();
			DiscountRate[] dRates = mapper.readValue(file, DiscountRate[].class);
			log.debug("Number of discount rates read from "+DISCOUNT_RATES_FILE+" : "+dRates.length);

			//Place loaded rules into rulesMap for easy access
			for(DiscountRate X : dRates) {
				log.debug(X.toString());
				discountRatesMap.put(X.getSkuId(),X);
			}				 
			log.debug("Total discount rates loaded into ratesMap: "+discountRatesMap.size());

		}catch(Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			loadStatus = false;
		}
		return loadStatus;
	}
	
	//Method to search for rule based on given Id and return rule object
	public DiscountRate searchDiscountRate(String id) {
		if(discountRatesMap.containsKey(id)) {
			log.debug("DiscountRatesMap contains discount rate for given Id:"+id+", returning dRate object");
			return discountRatesMap.get(id);
		} else {
			log.debug("DiscountRatesMap doesnot contain discount rate for  given Id:"+id+", returning null");
			return null;
		}
	}
		
	//Method to add new item into Shop Inventory
	public Boolean addItem(Item item) {
		Boolean addStatus = true;
		//Check the given item already exists in inventory
		//If so, then just increase it's quantity by item's quantity
		//Otherwise place item into shop inventory map
		try {
			String id = item.getSkuId();
			if(shopInventoryMap.containsKey(id)) {
				log.debug("Given item with id: "+id+" already exists in Inventory, no need to add");
				addStatus = false;
			} 
			else {
				log.debug("Given item with id: "+id+" not exists in Inventory, adding into shop inventory");
				shopInventoryMap.put(id,item);
			}
		}catch(Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			addStatus = false;
		}
		return addStatus;
	}
	
	//Method to update existing item present in Shop Inventory
	public Boolean updateItem(Item item) {
		Boolean updateStatus = true;
		//Check the given item already exists in inventory
		//If so, then update name, quantity and price with given item's quantity
		try {
			String id = item.getSkuId();
			if(shopInventoryMap.containsKey(id)) {
				log.debug("Given item with id: "+id+" exists in Inventory, updating it...");
				shopInventoryMap.get(id).setName(item.getName());
				shopInventoryMap.get(id).setQuantity(item.getQuantity());
				shopInventoryMap.get(id).setUnitPrice(item.getUnitPrice());
			} 
			else {
				log.debug("Given item with id: "+id+" not exists in Inventory, cannot update...");
				updateStatus = false;
			}
		}catch(Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			updateStatus = false;
		}
		return updateStatus;
	}
	
	//Method to delete an item from Shop Inventory
	public Boolean deleteItem(Item item) {
		Boolean delStatus = true;
		//Check the given item already exists in inventory
		//If so, then delete item from inventory
		try {
			String id = item.getSkuId();
			if(shopInventoryMap.containsKey(id)) {
				log.debug("Given item with id: "+id+" exists in Inventory, deleting it from inventory");
					shopInventoryMap.remove(id);
			} 
			else {
				log.debug("Given item with id: "+id+" not exists in Inventory, can not delete...");
				delStatus = false;
			}
		}catch(Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			delStatus = false;
		}
		return delStatus;
	}
	
	//Method to dump Shop Inventory Items
	public void displayShopInventoryItems() {
		log.debug("Total Items in Shop Inventory: "+shopInventoryMap.size());
		shopInventoryMap.keySet().forEach(itemId -> {
			log.debug(shopInventoryMap.get(itemId).toString());
		});
	}
	
	//Method to dump Special Prcing Rules
	public void dumpPriceRules() {
		rulesMap.keySet().forEach(key -> {
			log.debug(rulesMap.get(key).toString());
		});
	}
}
