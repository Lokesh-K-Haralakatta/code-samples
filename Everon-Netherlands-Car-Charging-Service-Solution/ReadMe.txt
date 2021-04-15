###############################################################################

				CAR CHARGING WEB SERVICE
							
###############################################################################

This ReadMe file contains steps to build and run car-charging-service
module developed as part of Everon Assignment.

1. Prequisite Softwares/Tools:
	- JDK 8 or later
	- Apache Maven

2. Extract the source files from ZIP archive car-charging-service.zip

3. We can directly run already existing jar present with in "target" directory,

4. Steps to build JAR Artifact:
	- Go to car-charging-service directory and run command 
	                   
	                   mvn clean package
	                   
	- We should find car-charging-service-0.0.1-SNAPSHOT.jar in folder "target"
	
5. Steps to run JAR Artifact:
	- Car Charging Web Serivce Module is implemented using SpringBoot Framework
	- Go to directory "car-charging-service/target" and run below command to 
	  start web service from terminal window
	             
	             java -jar car-charging-service-0.0.1-SNAPSHOT.jar
	             
	- Web service starts at default port of 8080 as one can notice below message 
	  on the console
	
				Tomcat started on port(s): 8080 (http) with context path ''

4. Implemented ReST End Points with Car Charging Service:

	- POST 	/chargingSessions	{ "stationId" : "station-1" }
	
	- GET 	/chargingSessions   Returns list of charging sessions present in repository
	
	- PUT	/chargingSessions/{id}	Id is UUID of Charging Entity returned as part of POST Response Body
	
	- GET   /chargingSessions/{id}  Id is UUID of Charging Entity returned as part of POST Response Body
	
	- GET	/chargingSessions/summary	Returns Charging Summary information for last minute 

5. Use either CURL Command / any ReST Client tool to hit the defined end points as part of car-charging-service
	
6. Shutting down car-charging-service - CTRL-C from terminal

*******************    ----------------------------     *****************   ------------------------  **********************
