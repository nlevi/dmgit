package com.emc.monitor.utils;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSessionManagerStatistics;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLoginInfo;

public class DocbaseSessionUtils {
	protected static DocbaseSessionUtils instance;
	protected static IDfSessionManager sessionManager;
	protected static IDfSession session;

	public static DocbaseSessionUtils getInstance() {
		if (instance == null) {
			instance = new DocbaseSessionUtils();
		}

		return instance;
	}

	public IDfSession getDocbaseSession(String docbase, String username, String password) throws DfException {
		
		if (sessionManager == null) {
			sessionManager = getSessionManager();			
			sessionManager.setIdentity(docbase, new DfLoginInfo(username, password));			
		} else if (!sessionManager.hasIdentity(docbase)) {			
			sessionManager.setIdentity(docbase, new DfLoginInfo(username, password));			
		}
		
//		sessionManager = getSessionManager();
//		System.out.println("Identity before set: " + sessionManager.getIdentity(docbase));
//		sessionManager.setIdentity(docbase, new DfLoginInfo(username, password));
//		System.out.println("Identity after set: " + sessionManager.getIdentity(docbase));
		session = sessionManager.getSession(docbase);
		IDfSessionManagerStatistics stats = sessionManager.getStatistics();		
		return session;
	}

	private IDfSessionManager getSessionManager() throws DfException {
		return (new DfClientX()).getLocalClient().newSessionManager();

	}

	public void releaseSession(IDfSession session) throws DfException {		
		sessionManager.release(session);		
	}
}
