package de.mhus.sop.cortex.api.outer;

import java.util.UUID;

import de.mhus.sop.cortex.api.inner.CortexService;
import de.mhus.sop.cortex.api.model.CortexProject;
import de.mhus.sop.cortex.api.model.CortexSpace;
import de.mhus.sop.mfw.api.MApi;

public interface CortexApi extends MApi {
	
	String[] getProjectList();
	
	CortexProject getProject(String name);

	CortexService getCortexService(String handler);
	
	CortexSpace getSpace(UUID id);
	
	CortexSpace getSpace(String shortcut);
	
}
