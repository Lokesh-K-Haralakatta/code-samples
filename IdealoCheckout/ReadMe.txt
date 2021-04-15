
		Solution to Shopping Checkout Operations in a Super Market
		
	This ReadMe describes about various components present in solution design, their purpose
and interaction. Also explains about the improvements/suggestions to make this solution a best one.

Components / Classes present in solution design:

	* Item : An entity used to represent each of item in super market. Each item object is 
			 uniquely identified by skuId. Item object also has name, quantity and unit price.  
			 
	* PricingRule : Represents special pricing rule associated with super market items
	
	* DiscountRate : Represents discount rate promotions on super market items
	
	* ShopInventory : Represents digital replica for super market items inventory. Loads items along 
	                  with quantity, unit price, initializes special pricing rules, initializes discount
	                  rates on items. Also provides methods to add, remove, update and search items in
	                  inventory, pricing rules and discount rates.
	                  
 	* ShoppingCart : Represents digital shopping cart which holds items to be checked out. Provides methods
 					 to add, remove and search items into shopping cart. During addition and removal, takes
 					 care of validation and synchronization between ShopInventory. Has reference to 
 					 ShopInventory at class level. We may have multiple shopping carts but all refers to 
 					 single ShopInventory.
 					 
	* CartCheckOut : Represents an interface layer between ShoppingCart and UI. Refers to underlying shopping
					 cart. Each instance of CartCheckOut has separate shopping cart instances. CartCheckOut
					 provides methods to scan items either individually or in bulk and add to shopping cart.
					 Also provides method to remove items either individually or in bulk from shopping cart.
					 Very importantly, it provides method to compute the amount to be paid by applying special
					 prices rules and discount rates for all eligible items present in the shopping cart at 
					 the time of check out.
					 
	* Unit Tests : The code implementation is tested by adding adequate number of unit tests for each of the
					described component / class above.
					
Proposed Improvements:

	* Database Layer : Introduce SQL/No SQL DB to hold inventory details, special pricing rules and 
	                   discount rates. Provide an option to update needed information in DB before hand or
	                   during the run time with appropriate authorization.
	                   
	* API Layer : Introduce an API controller layer at CartCheckOut component so that an elegant UI can be 
	              easily built in order to consume the APIs and render the shopping cart details to end user
	              and also to perform check out followed by payment options.
	              
	* Payment Options : Integration with various payment options post calculating the total amount to be paid.
	
	* Shopping Data Aggregation : Collection of shopping details into centralized big data cluster for carrying
	                              out insights analytical operations on the data. An automatic integration 
	                              using Apache Kafka can be put in place in order to produce shopping data into
	                              a data cluster.
	                              
   	* Regular Checks on updates : Implement regular check feature to take latest updates from database regarding
   								  items details, special pricing rules, discount rates at the run time so that no 
   								  need to bring down the application under execution.
   								  
   	* Thread Safety : Consider to make classes and methods complete thread safety in order to use the solution
   	                  in a concurrent access environment. 
   	                  
   	* Authorization/Authentication Module : Build authorization/authentication module so that only authorized
   											staff members are allowed to perform based on assigned roles.
   											
   	* Module to integrate customers personal information like Name, contact number and email for faster subsequent shopping.
   	                  
Software Tools used for solution implementation:

	* Build Tool: Apache Maven
	
	* Programming Language: Java 8
	
	* Additional needed Maven Dependency: Lombak (lombak.jar)   	                  	                                	          
	 					 	   