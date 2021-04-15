package com.everon.charging.controller;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.everon.charging.dao.ChargingEntity;
import com.everon.charging.dao.ChargingSessionsRepository;
import com.everon.charging.dao.ChargingSummary;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/*
 * @author Lokesh Kotturappa
 * Java Class to handle web requests for Charging Service
 */
@RestController
public class ChargingSessionsController {
	private static final Logger LOGGER = Logger.getLogger(ChargingSessionsController.class.getName());
	private static final Gson gson = new Gson();
	@Autowired
	private ChargingSessionsRepository sessionsRepo;
	
	@RequestMapping(value = "/")
	public String index() {
		LOGGER.info("Serving request to endpoint /");
		return "Car Charging Service for Everon";
	}
	
	/*
	 * Method to serve POST request made to end point /chargingSessions.
	 * Handles the error scenarios
	 * @return Created Resource with 200 OK or Error Message with appropriate status code
	 */
	@RequestMapping(value = "/chargingSessions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createChargingSession(@RequestBody String requestBody) {
		LOGGER.info("Serving POST request to endpoint /chargingSessions");
		LOGGER.info("Request Body: " +requestBody);
		try {
			JsonObject jsonObject = new JsonParser().parse(requestBody).getAsJsonObject();
			String stationId = jsonObject.get("stationId").getAsString();
			if(stationId.equals("")) {
				LOGGER.severe("stationId can not be empty");
				return ResponseEntity
						.badRequest()
						.contentType(MediaType.TEXT_PLAIN)
						.body("stationId can not be empty");
			}
			else {
				ChargingEntity entity = sessionsRepo.addChargingSession(stationId);
				String responseString = gson.toJson(entity);
				LOGGER.info(responseString);
				return ResponseEntity.accepted().body((responseString));
			}
		}catch(NullPointerException e) {
			LOGGER.severe(ExceptionUtils.getStackTrace(e));
			return ResponseEntity
					.unprocessableEntity()
					.contentType(MediaType.TEXT_PLAIN).body("stationId is needed in request body");
		}catch(Exception e) {
			LOGGER.severe(ExceptionUtils.getStackTrace(e));
			return ResponseEntity.unprocessableEntity()
					.contentType(MediaType.TEXT_PLAIN).body("Error while processing requestbody");
		}
	}
	
	/*
	 * Method to serve GET request made to end point /chargingSessions
	 * Handle Error Scenarios
	 * @return List of charging sessions in the form of JSON String
	 */
	@RequestMapping(value = "/chargingSessions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getChargeSessions(){
		LOGGER.info("Serving GET request to endpoint /chargingSessions");
		List<ChargingEntity> entitiesList = sessionsRepo.getChargingSessions();
		String responseString = gson.toJson(entitiesList);
		LOGGER.info(responseString);
		return ResponseEntity.ok(responseString);
	}
	
	/*
	 * Method to serve GET request made to end point /chargingSessions/{id}
	 * Handles Error Scenarios
	 * @return Charging Entity in the form of JSON String with 200 OK or Appropriate HTTP Status Code on error
	 */
	@RequestMapping(value = "/chargingSessions/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getChargeSession(@PathVariable String id){
		LOGGER.info("Serving GET request to endpoint /chargingSessions/{id}");
		LOGGER.info("Given Id: " + id);
		try {
			ChargingEntity entity = sessionsRepo.getChargingSession(UUID.fromString(id));
			if(entity != null) {
				String responseString = gson.toJson(entity);
				LOGGER.info(responseString);
				return ResponseEntity.ok(responseString);
			} else {
				String responseString = "Charge Session with Id: " +id + " not found in the repository";
				LOGGER.info(responseString);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseString);
			}
		}catch(Exception e) {
			LOGGER.severe(ExceptionUtils.getStackTrace(e));
			return ResponseEntity.unprocessableEntity()
					.contentType(MediaType.TEXT_PLAIN).body("Unable to process provided UUID");
		}
	}
	
	/*
	 * Method to serve PUT request made to end point /chargingSessions/{id}
	 * Handles Error Scenarios
	 * Stops Charging Session and 
	 * @return Charging Entity in the form of JSON String with 200 OK or Appropriate HTTP Status Code on error
	 */
	@RequestMapping(value = "/chargingSessions/{id}", method = RequestMethod.PUT, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> stopChargingSession(@PathVariable String id){
		LOGGER.info("Serving PUT request to endpoint /chargingSessions/{id}");
		LOGGER.info("Given Id: " + id);
		try {
			ChargingEntity entity = sessionsRepo.stopChargingSession(UUID.fromString(id));
			if(entity != null) {
				String responseString = gson.toJson(entity);
				LOGGER.info(responseString);
				return ResponseEntity.ok(responseString);
			} else {
				String responseString = "Charge Session with Id: " +id + " not found in the repository";
				LOGGER.info(responseString);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseString);
			}
		}catch(Exception e) {
			LOGGER.severe(ExceptionUtils.getStackTrace(e));
			return ResponseEntity.unprocessableEntity()
					.contentType(MediaType.TEXT_PLAIN).body("Unable to process provided UUID");
		}
	}
	
	/*
	 * Method to serve GET request made to end point /chargingSessions/summary
	 * Handle Error Scenarios
	 * @return Charging sessions summary instance in the form of JSON String
	 */
	@RequestMapping(value = "/chargingSessions/summary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getChargeSessionsSummary(){
		LOGGER.info("Serving GET request to endpoint /chargingSessions/summary");
		ChargingSummary summary = sessionsRepo.getChargingSessionsSummary();
		String responseString = gson.toJson(summary);
		LOGGER.info(responseString);
		return ResponseEntity.ok(responseString);
	}
}
