# LunaTech Assessment - IMDB Movies Webservice

IMDB Movies Webservice is a standalone web application being built using Java, Springboot, PostgreSQL database. Web application provides API end points in order to retrieve Cast and Crew details for given movie title, movies based on ratings for given genre and six degrees of sepration for any actor/actress with Kevin Bacon.

### Prerequisites
   - [Git](https://git-scm.com/)
   - [Java 8](https://openjdk.java.net/projects/jdk8/)
   - [Spring Boot](https://spring.io/projects/spring-boot)
   - [PostgreSQL](https://www.postgresql.org/)
   - [Apache Maven](https://maven.apache.org/)
   - [IMDB sample dataset](https://www.imdb.com/interfaces/)
   - [cURL](https://curl.se/)

### Build and Execution
   - Get Code from repository using `git clone`
   - Move to `IMDB-Webservice` directory
   - Update required PostgreSQL connection details in `application.properties` file
   - Command to build executable Jar file
     - `mvn clean package` to build with tests execution
     - `mvn clean package -DskipTests` to build skipping tests execution
   - Jar file `IMDB-Webservice-0.0.1-SNAPSHOT.jar` should be available within `target` directory on successful completion of build step
   - Execute Jar by running command `java -jar target/IMDB-Webservice-0.0.1-SNAPSHOT.jar`
   - On successful bootstrap of web application, we should have `IMDB Movies Webservice` waiting to serve client requests on port 9000

### Available API End Points
   - `/imdb/movies/title/{titleName}` : Accepts required primary or original title and gets cast/crew details
   - `/imdb/movies/ratings/{genre}` : Accepts valid genre category and gets movies details for requested genre based on ratings

### CURL Samples to access API End Points
   - Get Cast and Crew details for title `GeGe`
     - Curl Command: `curl http://localhost:9000/imdb/movies/title/GeGe`  
     - Response:
       ```json
          [{
	             "title": {
		                      "type": "movie",
		                      "primaryTitle": "Brother",
		                      "originalTitle": "GeGe",
		                      "genres": "Drama"
	                       },
	            "castAndCrew": [
                              {
		                             "name": "Stanley Tam",
		                             "role": "actor"
	                             }
	                           ]
           }]
       ```
   - Get titles details based on given genre type - `Horror / Comedy / Crime / Documentary`
     - Curl Command: `curl http://localhost:9000/imdb/movies/genre/horror`
     - Response:
       ```json
          [{
			"primaryTitle": "Red",
			"originalTitle": "Red",
			"genres": "Horror"
		}, 
		{
			"primaryTitle": "Notuku Potu",
			"originalTitle": "Notuku Potu",
			"genres": "Horror,Mystery"
		}, 
		{
			"primaryTitle": "Isha",
			"originalTitle": "Isha",
			"genres": "Drama,Horror"
		}, 
		{
			"primaryTitle": "Waiting for death",
			"originalTitle": "Waiting for death",
			"genres": "Fantasy,Horror,Thriller"
		}, 
		{
			"primaryTitle": "The Exorcism in Amarillo",
			"originalTitle": "The Exorcism in Amarillo",
			"genres": "Horror"
		}, 
		{
			"primaryTitle": "Land of Hope and Glory",
			"originalTitle": "Land of Hope and Glory",
			"genres": "Documentary,Horror"
		}, 
		{
			"primaryTitle": "Follow the Dead",
			"originalTitle": "Follow the Dead",
			"genres": "Comedy,Drama,Horror"
		}, 
		{
			"primaryTitle": "Asih 2",
			"originalTitle": "Asih 2",
			"genres": "Horror"
		}]
      ```
### API End Points to be added
   - `/imdb/movies/kevin-bacon/{actorName}` : Accepts required actor / actress name to find six degree of separation
   - 
### Proposed Enhancements
   - Enhanced error handling
   - Deeper logging with external configuration
   - Secured Authorisation
   - Secure communication with HTTPS
   - Integration with production ready features using Actuator Module
   - Beautiful Front End for easy user interaction
   - 
### Interested to contribute?
   - Please drop an email to [Maaike](maaike.burgeat@lunatech.nl) / [Lokesh](lokesh.h.k@gmail.com) with your skill sets and details, 
    clearly mentioning the reason why you are interested to contribute to this project.
