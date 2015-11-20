package de.mhus.sop.cortex.api;

import java.util.List;

import de.mhus.sop.cortex.api.model.CortexProject;
import de.mhus.sop.cortex.api.model.CortexSpace;
import de.mhus.sop.cortex.api.model.CortexTicket;
import de.mhus.sop.cortex.api.model.Model;

public interface CortexSpaceHandlerService {

	
	Model getModel(CortexSpace space, String description);
	
	CortexTicket getTicket(String ident);
	
	CortexProject getProject(String ident);
	
	List<CortexProject> getProjects();
	
}
