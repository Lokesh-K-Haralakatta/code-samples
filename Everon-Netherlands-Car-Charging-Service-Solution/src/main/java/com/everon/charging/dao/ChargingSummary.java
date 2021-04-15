package com.everon.charging.dao;

/*
 * @author Lokesh Kotturappa
 * Java Class to store charging summary information
 */
public class ChargingSummary {
	private long totalCount;
	private long startedCount;
	private long stoppedCount;
	
	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getStartedCount() {
		return startedCount;
	}

	public void setStartedCount(long startedCount) {
		this.startedCount = startedCount;
	}

	public long getStoppedCount() {
		return stoppedCount;
	}

	public void setStoppedCount(long stoppedCount) {
		this.stoppedCount = stoppedCount;
	}
	
	
}
