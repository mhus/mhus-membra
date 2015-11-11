package de.mhus.sop.cortex.api.model;

import de.mhus.lib.core.util.MNls;

public interface PType {

	String getName();
	String getType();
	MNls getNls();
	
	PAttribute[] getAttributes();
	
	PState getState(String name);
	PForm getForms(String name);
	
	String[] getStateNames();
	String[] getFormNames();
	
}
