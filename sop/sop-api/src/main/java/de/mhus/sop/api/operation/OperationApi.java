package de.mhus.sop.api.operation;

import java.util.List;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.sop.api.SApi;

public interface OperationApi extends SApi {

	String[] getGroups();
	String[] getOperations();
	String[] getOperationForGroup(String group);
	Operation getOperation(String path);
	
	OperationResult doExecute(String path, IProperties properties);
	
	OperationBpmDefinition getBpmDefinition(String prozess);
	List<OperationBpmDefinition> getBpmDefinitions();
}
