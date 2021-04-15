package com.idealo.checkout.pojo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestItem {
	@Test
	public void increaseQuantityTest() {
		Item X = new Item("A","Apple",1,40);
		
		assertEquals("Quantity not equal to 1", X.getQuantity(),new Integer(1));
		
		//Increase quantity by 1
		X.increaseQuantity(1);
		assertEquals("Quantity not equal to 2", X.getQuantity(),new Integer(2));
		
		//Increase quantity by 5
		X.increaseQuantity(5);
		assertEquals("Quantity not equal to 7", X.getQuantity(),new Integer(7));
	}
	
	@Test
	public void decreaseQuantityTest() {
		Item X = new Item("A","Apple",1,40);
		
		//Increase quantity by 5
		X.increaseQuantity(5);
		assertEquals("Quantity not equal to 6", X.getQuantity(),new Integer(6));
		
		//Decrease quantity by 3
		X.decreaseQuantity(3);
		assertEquals("Quantity not equal to 3", X.getQuantity(),new Integer(3));
		
		//Decrease quantity by 2
		X.decreaseQuantity(2);
		assertEquals("Quantity not equal to 1", X.getQuantity(),new Integer(1));
	}
}
