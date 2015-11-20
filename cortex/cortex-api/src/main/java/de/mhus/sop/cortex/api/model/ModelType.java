package de.mhus.sop.cortex.api.model;

import de.mhus.lib.core.util.MNls;

public interface ModelType {

	String getName();
	String getType();
	MNls getNls();
	
	ModelAttribute[] getAttributes();
	
	ModelState getState(String name);
	ModelForm getForms(String name);
	
	String[] getStateNames();
	String[] getFormNames();
	
}
