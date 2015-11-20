package de.mhus.sop.cortex.api.model;

import de.mhus.lib.core.util.MNls;

public interface ModelChange {

	String getName();
	MNls getMNls();
	String getNextState();
	String getForm();
	
}
