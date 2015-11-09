package de.mhus.sop.mfw.api.action;

import de.mhus.lib.core.strategy.AbstractOperation;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.strategy.TaskContext;
import de.mhus.sop.mfw.api.operation.OperationBpmDefinition;
import de.mhus.sop.mfw.api.operation.OperationService;

public abstract class BpmOperation extends AbstractOperation implements OperationService {

	@Override
	public boolean hasAccess() {
		return true;
	}

	@Override
	public final boolean canExecute(TaskContext context) {
		boolean ret = canExecuteBpm(context);
		if (!ret) BpmUtil.appendComment(context, this, "can't execute " + getClass().getCanonicalName());
		return ret;
	}

	protected abstract boolean canExecuteBpm(TaskContext context);

	@Override
	protected final OperationResult doExecute2(TaskContext context) throws Exception {
		try {
			OperationResult ret = doExecuteBpm(context);
			BpmUtil.appendComment(context, ret);
			return ret;
		} catch (Throwable t) {
			BpmUtil.appendComment(context, this, "Exception in " + getClass().getCanonicalName() +": " + t);
			throw t;
		}
	}

	protected abstract OperationResult doExecuteBpm(TaskContext context) throws Exception;

	protected void addComment(TaskContext context, String msg) {
		BpmUtil.appendComment(context, this, msg);
	}

	@Override
	public OperationBpmDefinition getBpmDefinition() {
		return null;
	}

}
