package com.everon.charging.dao;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class ChargingSessionsRepositoryTest {
	private static ChargingSessionsRepository repo;
	
	@Before
	public void setUpForAdd() throws Exception {
		repo = new ChargingSessionsRepository();
	}

	@Test
	public void testAddChargingSessions() {
		//Check there are no charging sessions in the repository
		assert(repo.getChargingSessions().size() == 0);
		
		//Add charging entities into repository
		ChargingEntity entity1 = repo.addChargingSession("station-1");
		ChargingEntity entity2 = repo.addChargingSession("station-2");
		ChargingEntity entity3 = repo.addChargingSession("station-3");
		
		//Check there are 3 charging sessions in the repository
		assert(repo.getChargingSessions().size() == 3);
		
		//Check stationIds with the entities
		assert(entity1.getStationId().equals("station-1"));
		assert(entity2.getStationId().equals("station-2"));
		assert(entity3.getStationId().equals("station-3"));
		
		//Check status to be IN_PROGRESS
		assert(entity1.getStatus().equals(StatusEnum.IN_PROGRESS));
		assert(entity2.getStatus().equals(StatusEnum.IN_PROGRESS));
		assert(entity3.getStatus().equals(StatusEnum.IN_PROGRESS));
	}

	@Before
	public void setUpForStopChargingSessions() throws Exception {
		repo = new ChargingSessionsRepository();
	}

	@Test
	public void testStopGetChargingSessions() {
		//Check there are no charging sessions in the repository
		assert(repo.getChargingSessions().size() == 0);
		
		//Add charging entities into repository
		ChargingEntity entity1 = repo.addChargingSession("station-1");
		ChargingEntity entity2 = repo.addChargingSession("station-2");
		ChargingEntity entity3 = repo.addChargingSession("station-3");
		
		//Extract UUIDs from returned entities
		UUID id1 = entity1.getId();
		UUID id2 = entity2.getId();
		UUID id3 = entity3.getId();
		
		//Stop Charging Sessions
		entity1 = repo.stopChargingSession(id1);
		entity2 = repo.stopChargingSession(id2);
		entity3 = repo.stopChargingSession(id3);
		
		//Check status to be STOPPED
		assert(entity1.getStatus().equals(StatusEnum.STOPPED));
		assert(entity2.getStatus().equals(StatusEnum.STOPPED));
		assert(entity3.getStatus().equals(StatusEnum.STOPPED));
				
		//Check there are 3 charging sessions in the repository
		List<ChargingEntity> entitiesList = repo.getChargingSessions();
		assert(entitiesList.size() == 3);
		
		//Check status to be STOPPED
		assert(entitiesList.get(0).getStatus().equals(StatusEnum.STOPPED));
		assert(entitiesList.get(1).getStatus().equals(StatusEnum.STOPPED));
		assert(entitiesList.get(2).getStatus().equals(StatusEnum.STOPPED));
	}

	@Before
	public void setUpForGetChargingSession() throws Exception {
		repo = new ChargingSessionsRepository();
	}

	@Test
	public void testGetChargingSession() {
		//Check there are no charging sessions in the repository
		assert(repo.getChargingSessions().size() == 0);
		
		//Add charging entity into repository
		ChargingEntity entity1 = repo.addChargingSession("station-1");
		ChargingEntity entity2 = repo.addChargingSession("station-2");
		ChargingEntity entity3 = repo.addChargingSession("station-3");
		
		//Extract UUIDs from returned entities
		UUID id1 = entity1.getId();
		UUID id2 = entity2.getId();
		UUID id3 = entity3.getId();
		
		//Retrieve charging entity from repository
		entity1 = repo.getChargingSession(id1);
		entity2 = repo.getChargingSession(id2);
		entity3 = repo.getChargingSession(id3);
		
		//Validate Ids
		assert(entity1.getId().equals(id1));
		assert(entity2.getId().equals(id2));
		assert(entity3.getId().equals(id3));
				
		//Check status to be IN_PROGRESS
		assert(entity1.getStatus().equals(StatusEnum.IN_PROGRESS));
		assert(entity2.getStatus().equals(StatusEnum.IN_PROGRESS));
		assert(entity3.getStatus().equals(StatusEnum.IN_PROGRESS));
		
		//Stop Charging Sessions
		entity1 = repo.stopChargingSession(id1);
		entity2 = repo.stopChargingSession(id2);
		entity3 = repo.stopChargingSession(id3);
		
		//Check status to be STOPPED
		assert(entity1.getStatus().equals(StatusEnum.STOPPED));
		assert(entity2.getStatus().equals(StatusEnum.STOPPED));
		assert(entity3.getStatus().equals(StatusEnum.STOPPED));
				
		//Retrieve charging entity from repository after stopping the charging
		entity1 = repo.getChargingSession(id1);
		entity2 = repo.getChargingSession(id2);
		entity3 = repo.getChargingSession(id3);
		
		//Check status to be STOPPED
		assert(entity1.getStatus().equals(StatusEnum.STOPPED));
		assert(entity2.getStatus().equals(StatusEnum.STOPPED));
		assert(entity3.getStatus().equals(StatusEnum.STOPPED));
	}

	@Before
	public void setUpForSessionsSummary() throws Exception {
		repo = new ChargingSessionsRepository();
	}

	@Test
	public void testChargingSessionsSummary() {
		//Add charging entities into repository
		ChargingEntity entity1 = repo.addChargingSession("station-1");
		ChargingEntity entity2 = repo.addChargingSession("station-2");
		ChargingEntity entity3 = repo.addChargingSession("station-3");
		ChargingEntity entity4 = repo.addChargingSession("station-4");
		ChargingEntity entity5 = repo.addChargingSession("station-5");
		ChargingEntity entity6 = repo.addChargingSession("station-6");
		
		//Extract UUIDs from 3 returned entities
		UUID id1 = entity1.getId();
		UUID id2 = entity2.getId();
		UUID id3 = entity3.getId();
		
		//Stop Charging Sessions for 3 entities
		entity1 = repo.stopChargingSession(id1);
		entity2 = repo.stopChargingSession(id2);
		entity3 = repo.stopChargingSession(id3);
		
		//Get Charging Sessions Summary
		ChargingSummary sessionsSummary = repo.getChargingSessionsSummary();
		
		//Check for count values
		assert(sessionsSummary.getTotalCount() == 6);
		assert(sessionsSummary.getStartedCount() == 3);
		assert(sessionsSummary.getStoppedCount() == 3);
	}

}
