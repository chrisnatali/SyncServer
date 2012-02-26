package com.syncserver.mapping;

import com.syncserver.entity.SyncPublication;
import com.syncserver.entity.SyncSubscription;
import com.syncserver.exception.SyncMappingException;

/**
 * 
 * @author Chris Natali
 * Interface representing a mapping of a SyncPublication
 * and SyncSubscription objects to/from Strings (as they are
 * passed over the wire 
 *
 * Usage:  There are 2 map methods to be implemented
 * 1st:  Maps Strings to/from SyncPublications
 * 2nd:  Maps Strings to/from SyncSubscriptions
 * both methods work similarly:
 * if the String is null, then it is created/populated from the Sync object
 * if the Sync object is null, then it is created/populated from the String object
 * 
 * Exceptions are thrown for odd cases
 * 
 */
public interface SyncMapping {
	
	
	/**
	 * maps Strings to SyncPublications and vice-versa
	 * @param syncPubStr The String representation
	 * @param syncPub The Java object representation
	 * @throws SyncMapException if both params are null or improperly formatted
	 */
	public void map(String syncPubStr, SyncPublication syncPub) throws SyncMappingException;

	/**
	 * maps Strings to SyncSubscriptions and vice-versa
	 * @param syncSubStr The String representation
	 * @param syncSub The Java object representation
	 * @throws SyncMapException if both params are null or improperly formatted
	 */
	public void map(String syncSubStr, SyncSubscription syncSub) throws SyncMappingException;
}
