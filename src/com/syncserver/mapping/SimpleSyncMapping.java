package com.syncserver.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.syncserver.entity.Channel;
import com.syncserver.entity.Publication;
import com.syncserver.entity.SyncPublication;
import com.syncserver.entity.SyncSubscription;
import com.syncserver.exception.SyncMappingException;

import java.util.regex.Pattern;

/**
 * 
 * @author Chris Natali
 * Class implementing a simple translation of SyncRequests/Response
 * 
 * SyncSubscription/Publication format definitions:
 * 
 * SyncSubscription: <ClientID><DELIM_SYNC_ITEM><ChannelList>
 * ClientID:  Unique Identifier for this client assigned by server
 * ChannelList: <Channel>[<DELIM_CHANNEL_LIST_ITEM><ChannelList>]
 * Channel: <ChannelName><DELIM_CHANNEL_ITEM><SyncID>
 * ChannelName: String naming a channel to listen on 
 * SyncID:  Optional ID of last update received for this Channel/ChannelID
 *          if this is empty, server should return "current state" of data on channel
 * 
 * SyncPublication: <PublishedList>
 * PublishedList: <Publication>[<DELIM_PUBLISHED_LIST_ITEM><PublishedList>]
 * Publication: <ClientID><DELIM_SYNC_ITEM><Channel><DELIM_SYNC_ITEM><Data>
 * ClientID:  Unique Identifier for client who published assigned by server
 * Channel: <ChannelName><DELIM_CHANNEL_ITEM><SyncID>
 * ChannelName: String naming a channel that item was published to
 * SyncID:  SyncID assigned by server for this publication.
 *          This ID is retained by the client for later subscription/synchronization
 * Data:  Data published in "well-known" format specific to this channel
 *
 *
 * NOTE:  The SyncPublication contains a data element.  This element may be passed on 
 * for further processing.  THEREFORE, the data element needs to be encoded prior to
 * being mapped to a String and decoded when mapped back to a SyncPublication.  This way,
 * the mapper will not be confused by delimiters/keywords within the data element when parsing
 * the String representation.  
 * 
 * See the private encodeData, decodeData and parsePublicationString
 * for details.   
 */
public class SimpleSyncMapping implements SyncMapping {

	/*
	 * The sync exchange delimiters
	 * These delimiters separate the various parts of a SyncRequest/Response
	 */
	public static final String DELIM_SYNC_ITEM = "+";
	public static final String DELIM_CHANNEL_ITEM = "*";
	public static final String DELIM_CHANNEL_LIST_ITEM = "~";
	public static final String DELIM_PUBLISHED_LIST_ITEM = "~";
	
	/*
	 * Regex patterns used for encoding/decoding SyncPublication data element
	 */
	public static final Pattern PATTERN_ENCODE = Pattern.compile("(\\Q+\\E|\\Q*\\E|\\Q~\\E|\\Q\\\\E)");
	public static final Pattern PATTERN_DECODE= Pattern.compile("\\\\(\\Q+\\E)|\\\\(\\Q*\\E)|\\\\(\\Q~\\E)|\\\\(\\\\)");
	public static final String PATTERN_ENCODE_REPLACEMENT = "\\\\$1";
	public static final String PATTERN_DECODE_REPLACEMENT = "$1$2$3$4";
	
	/*
	 * Regex patterns used for parsing out Sync items
	 */	
	public static final Pattern PATTERN_SYNC_ITEM_SPLIT = Pattern.compile("(?<!\\\\)\\Q+\\E");
	public static final Pattern PATTERN_CHANNEL_ITEM_SPLIT = Pattern.compile("(?<!\\\\)\\Q*\\E");
	public static final Pattern PATTERN_CHANNEL_LIST_ITEM_SPLIT = Pattern.compile("(?<!\\\\)\\Q~\\E");
	public static final Pattern PATTERN_PUBLISHED_LIST_ITEM_SPLIT = Pattern.compile("(?<!\\\\)\\Q~\\E");
	
	public void map(String syncPubStr, SyncPublication syncPub)
			throws SyncMappingException {
		
		if(syncPubStr == null) {
			if(syncPub == null) {
				throw new SyncMappingException("Both parameters cannot be null");
			}
			else {
				syncPubStr = new String();
				mapFromSyncPub(syncPubStr, syncPub);
				return;
			}
		}
		
		if(syncPub == null) {
			if(syncPubStr == null) {
				throw new SyncMappingException("Both parameters cannot be null");
			}
			else {
				syncPub = new SyncPublication();
				mapFromSyncPubStr(syncPubStr, syncPub);
				return;
			}
		}	
		
		else {
			throw new SyncMappingException("At least one parameter must be null");
		}

	}

	public void map(String syncSubStr, SyncSubscription syncSub)
			throws SyncMappingException {
		
		if(syncSubStr == null) {
			if(syncSub == null) {
				throw new SyncMappingException("Both parameters cannot be null");
			}
			else {
				syncSubStr = new String();
				mapFromSyncSub(syncSubStr, syncSub);
				return;
			}
		}
		
		if(syncSub == null) {
			if(syncSubStr == null) {
				throw new SyncMappingException("Both parameters cannot be null");
			}
			else {
				syncSub = new SyncSubscription();
				mapFromSyncSubStr(syncSubStr, syncSub);
				return;
			}
		}	
		
		else {
			throw new SyncMappingException("At least one parameter must be null");
		}		

	}
	
	//From SyncSubscription to String
	protected void mapFromSyncSub(String syncSubStr, SyncSubscription syncSub) throws SyncMappingException
	{
		syncSubStr += syncSub.getClientID() + DELIM_SYNC_ITEM;
		
		//create ChannelList
		List channelList = syncSub.getChannelList();
		String strChannelList = "";
		if(channelList != null && !channelList.isEmpty()) {
			Iterator iter = channelList.iterator();
			while(iter.hasNext()) {
				Channel channel = (Channel) iter.next();
				strChannelList += channel.getName() + DELIM_CHANNEL_ITEM + channel.getSyncID();
				if(iter.hasNext()) {
					strChannelList += DELIM_CHANNEL_LIST_ITEM;
				}
			}
		}
		
		syncSubStr += strChannelList;
	}
	
	//From SyncPublication to String	
	protected void mapFromSyncPub(String syncPubStr, SyncPublication syncPub) throws SyncMappingException
	{
		List publicationList = syncPub.getPublicationList();
		String strPublicationList = "";
		if(publicationList != null && !publicationList.isEmpty()) {
			Iterator iter = publicationList.iterator();
			while(iter.hasNext()) {
				Publication pub = (Publication) iter.next();
				strPublicationList += pub.getClientID() + DELIM_SYNC_ITEM;
				Channel channel = pub.getChannel();
				if(channel != null) {
					strPublicationList += channel.getName() + DELIM_CHANNEL_ITEM + channel.getSyncID();
				}
				strPublicationList += DELIM_SYNC_ITEM + encodeData(pub.getData());
				if(iter.hasNext()) {
					strPublicationList += DELIM_PUBLISHED_LIST_ITEM;
				}
			}
		}		
	}
	
	//From String to SyncPublication	
	protected void mapFromSyncPubStr(String syncPubStr, SyncPublication syncPub) throws SyncMappingException
	{
		String[] publications = parseSyncString(PATTERN_PUBLISHED_LIST_ITEM_SPLIT, syncPubStr);
		if(publications == null || publications.length == 0) {
			throw new SyncMappingException("Published List is empty or null");
		}
		
		ArrayList publicationList = new ArrayList();
		for(int i = 0; i < publications.length; i++) {
			String publicationStr = publications[i];
			if(publicationStr == null) {
				throw new SyncMappingException("Publication Item is null");
			}
			String[] pubItems = parseSyncString(PATTERN_SYNC_ITEM_SPLIT, publicationStr);
			if(pubItems.length != 3) {
				throw new SyncMappingException("Publication item " + i + " does not contain 3 items");
			}
			Long clientID = Long.parseLong(pubItems[0]);
			String channelStr = pubItems[1];
			String data = decodeData(pubItems[2]);
			String[] channelItems = parseSyncString(PATTERN_CHANNEL_ITEM_SPLIT, channelStr);
			if(channelItems.length != 2) {
				throw new SyncMappingException("Channel item does not contain 2 items");				
			}
			String channelName = channelItems[0];
			Long syncID = Long.parseLong(channelItems[1]);
			
			//populate the Publication and add it to the list
			Publication publication = new Publication();
			publication.setClientID(clientID);
			publication.setData(data);
			
			Channel channel = new Channel();
			channel.setName(channelName);
			channel.setSyncID(syncID);
			publication.setChannel(channel);
			
			publicationList.add(publication);
		}
		
		syncPub.setPublicationList(publicationList);
	}
	
	//From String to SyncSubscription	
	protected void mapFromSyncSubStr(String syncSubStr, SyncSubscription syncSub) throws SyncMappingException
	{
		
		String[] subscriptionItems = parseSyncString(PATTERN_SYNC_ITEM_SPLIT, syncSubStr);
		if(subscriptionItems.length != 2) {
			throw new SyncMappingException("Sync subscription does not contain 2 items");
		}
		
		Long clientID = Long.parseLong(subscriptionItems[0]);
		String channelListStr = subscriptionItems[1];
		
		String[] channels = parseSyncString(PATTERN_CHANNEL_LIST_ITEM_SPLIT, channelListStr);
		if(channels == null || channels.length == 0) {
			throw new SyncMappingException("Channel List is empty or null");
		}
		
		ArrayList channelList = new ArrayList();
		for(int i = 0; i < channels.length; i++) {
			String channelStr = channels[i];
			if(channelStr == null) {
				throw new SyncMappingException("Channel Item is null");
			}

			String[] channelItems = parseSyncString(PATTERN_CHANNEL_ITEM_SPLIT, channelStr);
			if(channelItems.length != 2) {
				throw new SyncMappingException("Channel item does not contain 2 items");				
			}
			String channelName = channelItems[0];
			Long syncID = Long.parseLong(channelItems[1]);
			
			//populate the channel and add it to the list
			Channel channel = new Channel();
			channel.setName(channelName);
			channel.setSyncID(syncID);
			
			channelList.add(channel);
		}
		
		syncSub.setClientID(clientID);
		syncSub.setChannelList(channelList);
	}	

	private String encodeData(String decodedData) {
		return PATTERN_ENCODE.matcher(decodedData).replaceAll(PATTERN_ENCODE_REPLACEMENT);		
	}
	
	private String decodeData(String encodedData) {
		return PATTERN_DECODE.matcher(encodedData).replaceAll(PATTERN_DECODE_REPLACEMENT);
	}
	
	private String[] parseSyncString(Pattern splitPattern, String syncStr) {
		return splitPattern.split(syncStr);
	}
}
