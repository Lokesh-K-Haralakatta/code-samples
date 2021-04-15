package com.everon.charging.dao;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ChargingEntityTests {
	private static ChargingEntity entity;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		entity = new ChargingEntity();
	}

	@Test
	void testEntityId() {
		UUID randomId = UUID.randomUUID();
		entity.setId(randomId);
		assert(entity.getId().equals(randomId));
	}

	@Test
	void testEntityStationId() {
		String stationId = "Station-23";
		entity.setStationId(stationId);
		assert(entity.getStationId().equals(stationId));
	}
	
	@Test
	void testEntityStartedAt() {
		LocalDateTime startTime = LocalDateTime.now();
		entity.setStartedAt(startTime);
		assert(entity.getStartedAt().equals(startTime));
	}
	
	@Test
	void testEntityStoppedAt() {
		LocalDateTime stopTime = LocalDateTime.now();
		entity.setStoppedAt(stopTime);
		assert(entity.getStoppedAt().equals(stopTime));
	}
	
	@Test
	void testEntityStatus() {
		StatusEnum status = StatusEnum.IN_PROGRESS;
		entity.setStatus(status);
		assert(entity.getStatus().equals(status));
	}
}
