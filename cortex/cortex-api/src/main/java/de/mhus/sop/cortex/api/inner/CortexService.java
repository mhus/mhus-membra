package de.mhus.sop.cortex.api.inner;

import java.util.List;

import de.mhus.sop.cortex.api.model.CortexProject;
import de.mhus.sop.cortex.api.model.CortexSpace;
import de.mhus.sop.cortex.api.model.CortexTicket;
import de.mhus.sop.cortex.api.model.Model;

public interface CortexService {

	CortexSpace getSpace();
	
	CortexTicket getTicket(String ident);
	
	CortexProject getProject(String ident);
	
	List<CortexProject> getProjects();
	
}
