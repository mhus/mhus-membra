package de.mhus.sop.auris.api;

import de.mhus.lib.core.IProperties;

public interface AurisConnector {

	String getName();
	String getDisplayName();
	void doActivate(IProperties properties);
	void doDeactivate();
	boolean isActive();
	
}
