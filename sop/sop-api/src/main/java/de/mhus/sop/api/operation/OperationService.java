package de.mhus.sop.api.operation;

import de.mhus.lib.core.strategy.Operation;

public interface OperationService extends Operation {

	public OperationBpmDefinition getBpmDefinition();
	
}
