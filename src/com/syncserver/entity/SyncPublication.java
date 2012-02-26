package com.syncserver.entity;

import java.util.List;

/**
 * 
 * @author Chris Natali
 * Class representing a list of publications from a client
 * @see Publication
 *
 */
public class SyncPublication {
	
	private List publicationList;

	public List getPublicationList() {
		return publicationList;
	}

	public void setPublicationList(List publicationList) {
		this.publicationList = publicationList;
	}

}
