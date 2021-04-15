package com.everon.charging.dao;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

/*
 * @author Lokesh Kotturappa
 * Java Class to represent Charging Sessions repository using in-memory data structures
 */
@Repository
public class ChargingSessionsRepository {
	/*
	 * Hash Map to store charging session entities
	 */
	private Map<UUID,ChargingEntity> entitiesMap = new HashMap<UUID,ChargingEntity>();
	
	/*
	 * Method to add new charging session entity into entitiesMap
	 * @param StationId
	 * @return ChargingEntity Instance
	 */
	public ChargingEntity addChargingSession(final String stationId) {
		/*
		 * Create an instance for ChargingEntity
		 */
		ChargingEntity entity = new ChargingEntity();
		entity.setId(UUID.randomUUID());
		entity.setStationId(stationId);
		entity.setStartedAt(LocalDateTime.now());
		entity.setStatus(StatusEnum.IN_PROGRESS);
		
		// Add ChargingEntity Instance into entitiesMap
		entitiesMap.put(entity.getId(), entity);
		
		// Return ChargingEntity Instance
		return entity;
	}
	
	/*
	 * Method to stop charging session, record stop time and return
	 * @param ChargingEntity UUID
	 * @return ChargingEntity Instance
	 */
	public ChargingEntity stopChargingSession(final UUID entityId) {
		//Check whether we have charging session with the given uuid or not
		if (entitiesMap.containsKey(entityId)){
			//We have charging session, get it
			ChargingEntity entity = entitiesMap.get(entityId);
			//Stop charging, update status and update in the map
			entity.setStoppedAt(LocalDateTime.now());
			entity.setStatus(StatusEnum.STOPPED);
			entitiesMap.put(entityId, entity);
			//Return ChargingEntity
			return entity;
		}
		else
			return null;
	}
	
	/*
	 * Method retrieve and return all Charging Sessions present in the repository
	 * @return List of Charging Sessions
	 */
	public List<ChargingEntity> getChargingSessions(){
		return entitiesMap.values().stream().collect(Collectors.toList());
	}
	
	/*
	 * Method to search and retrieve Charging Session present in the repository
	 * @return Charging Entity Instance if found, null otherwise
	 */
	public ChargingEntity getChargingSession(final UUID id){
		return entitiesMap.get(id);
	}
	
	/*
	 * Method to calculate and return charging sessions summary for last 1 minute
	 * @return ChargingSummary Instance
	 */
	public ChargingSummary getChargingSessionsSummary() {
		//Instantiate Sessions Summary instance
		ChargingSummary sessionsSummary = new ChargingSummary();
		
		//Filter out sessions that are in progress state for last 1 minute and count, set started count in summary instance
		sessionsSummary.setStartedCount(getChargingSessions().stream().filter(session -> 
							((LocalDateTime.now().getMinute() - session.getStartedAt().getMinute()) <= 1
											&& session.getStatus() == StatusEnum.IN_PROGRESS)).count());
		
		//Filter out sessions that are in stopped state for last 1 minute and count, set stopped count in summary instance
		sessionsSummary.setStoppedCount(getChargingSessions().stream().filter(session -> session.getStoppedAt() != null &&
							((LocalDateTime.now().getMinute() - session.getStoppedAt().getMinute()) <= 1
											&& session.getStatus() == StatusEnum.STOPPED)).count());
		
		//Set total count for last 1 minute using started and stopped count
		sessionsSummary.setTotalCount(sessionsSummary.getStartedCount() + sessionsSummary.getStoppedCount());
		
		return sessionsSummary;
	}
} 
