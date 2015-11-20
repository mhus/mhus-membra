package de.mhus.sop.cortex.api.model;

import de.mhus.lib.core.util.MNls;

public interface ModelState {

	String getName();
	MNls getNls();
	
	ModelChange[] getChanges();
	
}
