package de.mhus.sop.cortex.api;

import de.mhus.sop.cortex.api.model.CortexSpace;
import de.mhus.sop.cortex.api.model.Model;

public interface CortexSpaceHandlerService {

	
	Model getModel(CortexSpace space, String description);
	
}
