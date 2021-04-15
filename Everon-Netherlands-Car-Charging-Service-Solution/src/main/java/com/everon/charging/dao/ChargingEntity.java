package com.everon.charging.dao;

import java.time.LocalDateTime;
import java.util.UUID;

/*
 * @author Lokesh Kotturappa
 * Java Class to present charging entity
 */

enum StatusEnum {
	STARTED, IN_PROGRESS, STOPPED
}

public class ChargingEntity {
	/*
	 * Charging Entity Properties
	 */
	UUID id;
	String stationId;
	LocalDateTime startedAt;
	LocalDateTime stoppedAt;
	StatusEnum status;
	
	/*
	 * Default constructor for ChargingEntity
	 */
	public ChargingEntity() {
		super();
	}
	
	/*
	 * Parameterized constructor to instantiate ChargingEntity Object
	 */
	public ChargingEntity(UUID id, String stationId, LocalDateTime startedAt, LocalDateTime stoppedAt,
			StatusEnum status) {
		super();
		this.id = id;
		this.stationId = stationId;
		this.startedAt = startedAt;
		this.stoppedAt = stoppedAt;
		this.status = status;
	}

	/*
	 * Getter Method to return Charging Entity Instance ID
	 */
	public UUID getId() {
		return id;
	}

	/*
	 * @param UUID
	 * Setter Method to set Charging Entity Instance ID
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/*
	 * Getter Method to return Charging Entity Instance Station ID
	 */
	public String getStationId() {
		return stationId;
	}

	/*
	 * Setter Method to set Charging Entity Instance Station ID
	 * @param stationId
	 */
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	/*
	 * Getter Method to return Charging Entity Instance start time
	 */
	public LocalDateTime getStartedAt() {
		return startedAt;
	}

	/*
	 * Setter Method to set Charging Entity Instance start time
	 * @param startedAT
	 */
	public void setStartedAt(LocalDateTime startedAt) {
		this.startedAt = startedAt;
	}

	/*
	 * Getter Method to return Charging Entity Instance stop time
	 */
	public LocalDateTime getStoppedAt() {
		return stoppedAt;
	}

	/*
	 * Setter Method to set Charging Entity Instance stop time
	 * @param stoppedAt
	 */
	public void setStoppedAt(LocalDateTime stoppedAt) {
		this.stoppedAt = stoppedAt;
	}

	/*
	 * Getter Method to return Charging Entity Instance status
	 */
	public StatusEnum getStatus() {
		return status;
	}

	/*
	 * Getter Method to set Charging Entity Instance status
	 * @param status
	 */
	public void setStatus(StatusEnum status) {
		this.status = status;
	} 
	
	
}
