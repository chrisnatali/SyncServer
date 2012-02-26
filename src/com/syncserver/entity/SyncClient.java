package com.syncserver.entity;

import java.util.Date;
import java.util.HashMap;

/**
 * 
 * @author Chris Natali
 * Class representing a client of Sync services
 *
 */
public class SyncClient {
		
	private Long clientID;
	private Date lastAction;
	private Long countPublications;
	private Long countSubscriptions;
	private String lastIP;
	private Boolean waiting;
	private HashMap syncChannels;
	
	/**
	 * @return the clientID
	 */
	public Long getClientID() {
		return clientID;
	}
	/**
	 * @param clientID the clientID to set
	 */
	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}
	/**
	 * number of publications during this client lifetime
	 * @return the countPublications
	 */
	public Long getCountPublications() {
		return countPublications;
	}
	/**
	 * @param countPublications the countPublications to set
	 */
	public void setCountPublications(Long countPublications) {
		this.countPublications = countPublications;
	}
	/**
	 * number of subscriptions during this client lifetime
	 * @return the countSubscriptions
	 */
	public Long getCountSubscriptions() {
		return countSubscriptions;
	}
	/**
	 * @param countSubscriptions the countSubscriptions to set
	 */
	public void setCountSubscriptions(Long countSubscriptions) {
		this.countSubscriptions = countSubscriptions;
	}
	/**
	 * the datetime of the last subscription/publication from this client
	 * @return the lastAction
	 */
	public Date getLastAction() {
		return lastAction;
	}
	/**
	 * @param lastAction the lastAction to set
	 */
	public void setLastAction(Date lastAction) {
		this.lastAction = lastAction;
	}
	/**
	 * last IP address associated with this clientID
	 * @return the lastIP
	 */
	public String getLastIP() {
		return lastIP;
	}
	/**
	 * @param lastIP the lastIP to set
	 */
	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}
	/**
	 * The channels this client was last subscribed to
	 * @return the syncChannels
	 */
	public HashMap getSyncChannels() {
		return syncChannels;
	}
	/**
	 * @param syncChannels the syncChannels to set
	 */
	public void setSyncChannels(HashMap syncChannels) {
		this.syncChannels = syncChannels;
	}
	/**
	 * True if this client is waiting on a subscription
	 * False otherwise
	 * @return the waiting
	 */
	public Boolean getWaiting() {
		return waiting;
	}
	/**
	 * @param waiting the waiting to set
	 */
	public void setWaiting(Boolean waiting) {
		this.waiting = waiting;
	}

}
