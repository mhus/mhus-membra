package de.mhus.sop.impl;

import java.util.Iterator;
import java.util.Map.Entry;

import de.mhus.lib.core.util.SoftHashMap;
import de.mhus.lib.errors.MException;
import de.mhus.sop.api.Sop;
import de.mhus.sop.api.SopApi;
import de.mhus.sop.api.aaa.AaaContext;
import de.mhus.sop.api.aaa.Account;
import de.mhus.sop.api.aaa.Ace;
import de.mhus.sop.api.aaa.ContextCachedItem;

public class AaaContextImpl implements AaaContext {

	protected AaaContextImpl parent;
	private Account account;
	protected boolean adminMode = false;
	protected SoftHashMap<String, ContextCachedItem> cache = new SoftHashMap<String, ContextCachedItem>();

	public AaaContextImpl(Account account) {
		this.account = account;
	}
	public AaaContextImpl(Account account, boolean admin) throws MException {
		this.account = account;
		if (admin) {
			SopApi aa = Sop.getApi(SopApi.class);
			Ace ace = aa.findGlobalAce(account.getAccount(), Ace.GENERAL_ADMIN);
			if (ace != null && ace.canRead())
				adminMode = true;
		}
	}
	public AaaContextImpl getParent() {
		return parent;
	}

	public String toString() {
		return account + (adminMode ? "(admin)" : "" ) + "@AaaContext";
	}

	public Account getAccount() throws MException {
		return account;
	}

	public boolean isAdminMode() {
		return adminMode;
	}

	public void setParent(AaaContextImpl parent) {
		this.parent = parent;
	}

	@Override
	public String getAccountId() {
		try {
			return getAccount().getAccount();
		} catch (MException e) {
		}
		return null;
	}
	
	public ContextCachedItem getCached(String key) {
		if (key == null) return null;
		synchronized (cache) {
			ContextCachedItem ret = cache.get(key);
			if (ret != null ) {
				if (ret.isValid())
					return ret;
				else
					cache.remove(key);
			}
			return null;
		}
	}
	public void setCached(String key, ContextCachedItem item) {
		if (key == null || item == null) return;
		synchronized (cache) {
			cache.put(key, item);
		}
	}

	public void clearCache() {
		cache.clear();
	}
	
	public void cleanupCache() {
		cache.cleanup();
		synchronized (cache) {
			Iterator<Entry<String, ContextCachedItem>> iterator = cache.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, ContextCachedItem> entry = iterator.next();
				if (entry.getValue() != null && !entry.getValue().isValid())
					iterator.remove();
			}
		}
	}
	
	public int cacheSize() {
		return cache.size();
	}
	
}
