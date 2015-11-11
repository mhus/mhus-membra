package de.mhus.sop.cortex.api;

import de.mhus.sop.cortex.api.model.Project;
import de.mhus.sop.mfw.api.MApi;

public interface CortexApi extends MApi {
	
	String[] getProjectList();
	
	Project getProject(String name);
	
}
