package com.syncserver.entity;

/**
 * 
 * @author Chris Natali
 * Class representing a single publication
 * Maintains the clientID, channel published to and the data published
 * Note:  data is a String, it's format is known only to the client and the SyncChannel
 *
 */
public class Publication {
	
	private Long clientID;
	private Channel channel;
	private String data;
	
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public Long getClientID() {
		return clientID;
	}
	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

}
