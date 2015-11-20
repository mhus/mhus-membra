package de.mhus.sop.cortex.api;

import java.util.UUID;

import de.mhus.sop.cortex.api.model.CortexProject;
import de.mhus.sop.cortex.api.model.CortexSpace;
import de.mhus.sop.mfw.api.MApi;

public interface CortexApi extends MApi {
	
	String[] getProjectList();
	
	CortexProject getProject(String name);

	CortexSpaceHandlerService getCortexSpaceHandler(String handler);
	
	CortexSpace getSpace(UUID id);
	
	CortexSpace getSpace(String shortcut);
	
}
