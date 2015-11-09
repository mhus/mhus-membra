package de.mhus.sop.mfw.api.operation;

import de.mhus.lib.core.strategy.AbstractOperation;

public abstract class DriverOperation extends AbstractOperation implements OperationService {

	@Override
	public OperationBpmDefinition getBpmDefinition() {
		return null;
	}

	@Override
	public boolean hasAccess() {
		return true;
	}

}
