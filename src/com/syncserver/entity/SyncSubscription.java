package com.syncserver.entity;

import java.util.List;

/**
 * 
 * @author Chris Natali
 * Class representing a single SyncSubscription request from a client
 * Maintains the clientID and the list of clientChannels to be subscribed to
 * @see Channel
 *
 */
public class SyncSubscription {
	
	private Long clientID;
	private List channelList;
	
	public List getChannelList() {
		return channelList;
	}
	public void setChannelList(List channelList) {
		this.channelList = channelList;
	}
	public Long getClientID() {
		return clientID;
	}
	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}

}
