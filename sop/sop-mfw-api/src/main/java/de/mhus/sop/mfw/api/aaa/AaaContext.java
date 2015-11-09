package de.mhus.sop.mfw.api.aaa;

import de.mhus.lib.errors.MException;

public interface AaaContext {

	Account getAccount() throws MException;
	boolean isAdminMode();
	String getAccountId();
	ContextCachedItem getCached(String key);
	void setCached(String key, ContextCachedItem item);
}
