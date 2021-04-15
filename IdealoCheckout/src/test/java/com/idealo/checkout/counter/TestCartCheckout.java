package com.idealo.checkout.counter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.idealo.checkout.pojo.Item;

@RunWith(JUnit4.class)
public class TestCartCheckout {
	private static CartCheckout checkOut = new CartCheckout();
	
	@Test
	public void testScanSearchRemoveItemA() {
		//Create item instance
		String id = "skuA";
		
		//Get items count in cart
		Integer itemsCount = checkOut.cartItemsCount();
		
		//Scan item to get added to cart
		assertTrue("Scanning and adding item to cart failed",checkOut.scanItem(id));
		assertEquals("Items in cart are not as expected",checkOut.cartItemsCount(),new Integer(itemsCount+1));
		
		//Validate item added to cart
		Item X = checkOut.searchItem(id);
		assertNotNull("Item in cart cannot be NULL",X);
		assertEquals("Associated item id not as expected",X.getSkuId(),id);
		assertEquals("Associated quantity with item is not as expected",X.getQuantity(),new Integer(1));
		
		//Display shopping cart contents
		checkOut.displayCart();
		
		//Add same item again and validate quantity for the item increases, items count remain as before
        itemsCount = checkOut.cartItemsCount();
		
		//Scan same item to get added to cart and validate items count remains same as before
        Integer quantity = X.getQuantity();
        Integer reqdQty = 200;
		assertTrue("Scanning and adding item to cart failed",checkOut.scanItem(id,reqdQty));
		assertEquals("Items in cart are not as expected",checkOut.cartItemsCount(),itemsCount);
		
		//Validate item quantity increases by one
		X = checkOut.searchItem(id);
		assertEquals("Associated quantity with item is not as expected",X.getQuantity(),new Integer(quantity+reqdQty));
		
		//Display shopping cart contents
		checkOut.displayCart();
		
		//Reduce quantity 1 for item from cart and 
		//validate the item still exists in cart but quantity is reduced by 1
		itemsCount = checkOut.cartItemsCount();
		quantity = X.getQuantity();
		assertTrue("Reducing quantity by 1 for item from cart failed",checkOut.removeItem(id));
		assertEquals("Items in cart are not as expected",checkOut.cartItemsCount(),itemsCount);
		X = checkOut.searchItem(id);
		assertEquals("Associated quantity with item is not as expected",X.getQuantity(),new Integer(quantity-1));
		
		//Display shopping cart contents
		checkOut.displayCart();
	}
	
	@Test
	public void testScanSearchRemoveItemB() {
		//Create item instance
		String id = "skuB";
		
		//Get items count in cart
		Integer itemsCount = checkOut.cartItemsCount();
		
		//Scan item to get added to cart
		Integer reqdQty = 100;
		assertTrue("Scanning and adding item to cart failed",checkOut.scanItem(id,reqdQty));
		assertEquals("Items in cart are not as expected",checkOut.cartItemsCount(),new Integer(itemsCount+1));
		
		//Validate item added to cart
		Item X = checkOut.searchItem(id);
		assertNotNull("Item in cart cannot be NULL",X);
		assertEquals("Associated item id not as expected",X.getSkuId(),id);
		assertEquals("Associated quantity with item is not as expected",X.getQuantity(),reqdQty);
		
		//Display shopping cart contents
		checkOut.displayCart();
		
		//Reduce item quantity by reqdQty from the cart
		//Validate the item gets removed from the cart itself
		itemsCount = checkOut.cartItemsCount();
		assertTrue("Reducing quantity by "+reqdQty+" for item from cart failed",checkOut.removeItem(id,reqdQty));
		assertEquals("Items in cart are not as expected",checkOut.cartItemsCount(),new Integer(itemsCount-1));
		X = checkOut.searchItem(id);
		assertNull("Item in cart should be NULL",X);
		
		//Display shopping cart contents
		checkOut.displayCart();
	}
	
	private Integer computeAmountForPayment(List<String> itemIds) {
		//Create cart instance and scan all given items
		CartCheckout paymentCheckoutTestInstance = new CartCheckout();
		for(String itemId: itemIds)
			paymentCheckoutTestInstance.scanItem(itemId);
		
		//Compute amount to pay and return
		return paymentCheckoutTestInstance.totalAmountForPayment();
	}
	
	@Test
	public void testTotalAmountForPayment() {
		//Validate payment for an empty cart is 0
		List<String> items = new ArrayList<>();
		Integer expectedAmtToPay = 0;
		assertEquals("Amount to Pay is not as expected",computeAmountForPayment(items),expectedAmtToPay);
		
		//Validate payment for cart with itemId skuA is 40
		items.add("skuA");
		expectedAmtToPay = 40;
		assertEquals("Amount to Pay is not as expected",computeAmountForPayment(items),expectedAmtToPay);
		
		//Validate payment for cart with items skuA and skuB is 90
		items.add("skuB");
		expectedAmtToPay = 90;
		assertEquals("Amount to Pay is not as expected",computeAmountForPayment(items),expectedAmtToPay);
		
		//Validate payment for cart with items skuA,skuB, skuC and skuD is 135
		items.add(0,"skuD");
		items.add(0,"skuC");
		expectedAmtToPay = 135;
		assertEquals("Amount to Pay is not as expected",computeAmountForPayment(items),expectedAmtToPay);
		
		//Validate payment for cart with items - skuA and skuA is 80
		items.clear();
		items.add("skuA");
		items.add("skuA");
		expectedAmtToPay = 80;
		assertEquals("Amount to Pay is not as expected",computeAmountForPayment(items),expectedAmtToPay);
		
		//Validate payment for cart with quantity of 3 for item - skuA is 100
		items.add("skuA");
		expectedAmtToPay = 100;
		assertEquals("Amount to Pay is not as expected",computeAmountForPayment(items),expectedAmtToPay);
		
		//Validate payment for cart with quantity of 4 for item - skuA is 140
		items.add("skuA");
		expectedAmtToPay = 140;
		assertEquals("Amount to Pay is not as expected",computeAmountForPayment(items),expectedAmtToPay);
		
		//Validate payment for cart with quantity of 5 for item - skuA is 180
		items.add("skuA");
		expectedAmtToPay = 180;
		assertEquals("Amount to Pay is not as expected",computeAmountForPayment(items),expectedAmtToPay);
		
		//Validate payment for cart with quantity of 6 for item - skuA is 200
		items.add("skuA");
		expectedAmtToPay = 200;
		assertEquals("Amount to Pay is not as expected",computeAmountForPayment(items),expectedAmtToPay);
		
		//Validate payment for cart with quantity of 3 for item - skuA and 1 for item - skuB is 150
		items.clear();
		items.add("skuA");
		items.add("skuA");
		items.add("skuA");
		items.add("skuB");
		expectedAmtToPay = 150;
		assertEquals("Amount to Pay is not as expected",computeAmountForPayment(items),expectedAmtToPay);
		
		//Validate payment for cart with quantity of 3 for item - skuA and 2 for item - skuB is 180
		items.add("skuB");
		expectedAmtToPay = 180;
		assertEquals("Amount to Pay is not as expected",computeAmountForPayment(items),expectedAmtToPay);
		
		//Validate payment for cart with quantity of 3 for item - skuA, 2 for item - skuB 
		// and 1 for item-skuD is 200
		items.add("skuD");
		expectedAmtToPay = 200;
		assertEquals("Amount to Pay is not as expected",computeAmountForPayment(items),expectedAmtToPay);
		
		//Validate payment for cart with quantity of 3 for item - skuA, 2 for item - skuB
		// and 1 for item-skuD in random order is 200
		items.clear();
		items.add("skuD");
		items.add("skuA");
		items.add("skuB");
		items.add("skuA");
		items.add("skuB");
		items.add("skuA");
		expectedAmtToPay = 200;
		assertEquals("Amount to Pay is not as expected",computeAmountForPayment(items),expectedAmtToPay);
	}
	
	@Test
	public void testIncrementalCheckOut() {
		CartCheckout incCheckout = new CartCheckout();
		
		assertEquals(new Integer(0), incCheckout.totalAmountForPayment());
		incCheckout.scanItem("skuA"); assertEquals(new Integer(40), incCheckout.totalAmountForPayment());
		incCheckout.scanItem("skuB"); assertEquals(new Integer(90), incCheckout.totalAmountForPayment());
		incCheckout.scanItem("skuA"); assertEquals(new Integer(130), incCheckout.totalAmountForPayment());
		incCheckout.scanItem("skuA"); assertEquals(new Integer(150), incCheckout.totalAmountForPayment());
		incCheckout.scanItem("skuB"); assertEquals(new Integer(180), incCheckout.totalAmountForPayment());
	}
	
	@Test
	public void testDiscountRateAndBulkOperationsOnCartCheckout() {
		CartCheckout discountCheckout = new CartCheckout();
		
		//Add 1 quantity of item skuE and validate the checkout total is 90
		discountCheckout.scanItem("skuE");
		Integer expectedAmtToPay = 90;
		assertEquals("Amount to Pay after discount is not as expected",discountCheckout.totalAmountForPayment(),expectedAmtToPay);
		
		//Add 5 quantity of item skuE and validate the checkout total is 450
		discountCheckout.removeItem("skuE");
		discountCheckout.scanItem("skuE",5);
		expectedAmtToPay = 450;
		assertEquals("Amount to Pay after discount is not as expected",discountCheckout.totalAmountForPayment(),expectedAmtToPay);
		
		//Add 3 quantity of item skuA, 2 quantity of item skuB,
		// 4 quantity of item skuC, 3 quantity of item skuD and 5 quantity of item skuE 
		//and validate the checkout total is 100+80+100+60+450 = 790
		discountCheckout.removeItem("skuE",5);
		discountCheckout.scanItem("skuA",3);
		discountCheckout.scanItem("skuB",2);
		discountCheckout.scanItem("skuC",4);
		discountCheckout.scanItem("skuD",3);
		discountCheckout.scanItem("skuE",5);
		expectedAmtToPay = 790;
		assertEquals("Amount to Pay after discount is not as expected",discountCheckout.totalAmountForPayment(),expectedAmtToPay);
	}
}
