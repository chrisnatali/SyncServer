package test.com.syncserver.sync;

import junit.framework.TestCase;

public class TestSyncManager extends TestCase {

	SyncManager syncMgr;
	long clientID1;
	long clientID2;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		//initialize the SyncManager under test
		syncMgr = new SyncManager();
		syncMgr.init(); //config
		
		//add some channels
		BaseSyncChannel channel1 = new BaseSyncChannel("channel1");
		channel1.init(); //config
		
		BaseSyncChannel channel2 = new BaseSyncChannel("channel2");
		channel2.init(); //config
		
		syncMgr.addChannel(channel1);
		syncMgr.addChannel(channel2);
		
		//set 2 clientIDs
		clientID1 = syncMgr.assignClientID();
		clientID2 = syncMgr.assignClientID();		
				
	}
	
	/**
	 * Simple test
	 * Ensures that client1 can publish something that client2 subscribes to
	 * that client2 can get the publication and vice-versa
	 *
	 */
	public void testSimplePublishSubscribe() {
		
		//construct testMessages
		String messageFromClient1 = "This is a test from client: " + clientID1;
		String messageFromClient2 = "This is a test from client: " + clientID2;
		
		//construct SyncPublications
		SyncPublication syncPub1 = new SyncPublication();
		Publication pub1 = new Publication();
		Channel channel1 = new Channel();
		channel1.setName("channel1");
		pub1.setClientChannel(channel1);
		pub1.setData(messageFromClient1);
		syncPub1.setClientID(clientID1);
		syncPub1.addPublication(pub1);
		
		SyncPublication syncPub2 = new SyncPublication();
		Publication pub2 = new Publication();
		Channel channel2 = new Channel();
		channel2.setName("channel2");
		pub2.setClientChannel(channel2);
		pub2.setData(messageFromClient2);
		syncPub2.setClientID(clientID2);
		syncPub2.addPublication(pub2);	
		
		//construct SyncSubscriptions
		SyncSubscription syncSub1 = new SyncSubscription();
		syncSub1.setClientChannel(channel1);
		syncSub1.setClientID(clientID2);
		
		SyncSubscription syncSub2 = new SyncSubscription();
		syncSub2.setClientChannel(channel2);
		syncSub2.setClientID(clientID1);	
		
		syncMgr.publish(syncPub1);
		syncMgr.publish(syncPub2);
		
		SyncPublication syncPubRecv1 = syncMgr.subscribe(syncSub1);
		SyncPublication syncPubRecv2 = syncMgr.subscribe(syncSub2);
		
		
		//test whether client2 received message from client1 and vice-versa
		assert(syncPubRecv1.getPublications().getItem(0).getData().equals(messageFromClient1));
		assert(syncPubRecv2.getPublications().getItem(0).getData().equals(messageFromClient2));		
		
		
		
	}	

}
