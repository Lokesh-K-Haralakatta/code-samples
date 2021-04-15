package com.everon.charging.dao;

import org.junit.BeforeClass;
import org.junit.Test;

public class ChargingSummaryTests {
	private static ChargingSummary summary;
	private static final long TOTAL_COUNT = 5;
	private static final long STARTED_COUNT = 3;
	private static final long STOPPED_COUNT = 2;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		summary = new ChargingSummary();
	}

	@Test
	public void testTotalCount() {
		summary.setTotalCount(TOTAL_COUNT);
		assert(summary.getTotalCount() == TOTAL_COUNT);
	}

	@Test
	public void testStartedCount() {
		summary.setStartedCount(STARTED_COUNT);
		assert(summary.getStartedCount() == STARTED_COUNT);
	}
	
	@Test
	public void testStoppedCount() {
		summary.setStoppedCount(STOPPED_COUNT);
		assert(summary.getStoppedCount() == STOPPED_COUNT);
	}
}
