package de.mhus.sop.cortex.api.model;

import de.mhus.lib.core.util.MNls;

public interface PState {

	String getName();
	MNls getNls();
	
	PChange[] getChanges();
	
}
