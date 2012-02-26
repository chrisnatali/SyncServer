package com.syncserver.entity;

/**
 * 
 * @author Chris Natali
 * Class representing Client representation of a channel for communicating
 * with the SyncServer.
 * Maintains a name, and the syncID
 *
 */
public class Channel {
	
	private String name;
	private Long syncID;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Long getSyncID() {
		return syncID;
	}
	public void setSyncID(Long syncID) {
		this.syncID = syncID;
	}
}
