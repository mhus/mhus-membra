package de.mhus.sop.impl.operation;

import java.util.LinkedList;
import java.util.List;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.OperationDescription;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.jms.ServerJms;
import de.mhus.lib.karaf.jms.JmsDataChannel;
import de.mhus.lib.karaf.jms.JmsManagerService;
import de.mhus.sop.api.Mfw;
import de.mhus.sop.api.jms.AbstractOperationExecuteChannel;
import de.mhus.sop.api.jms.TicketAccessInterceptor;
import de.mhus.sop.api.operation.OperationApi;

@Component(provide=JmsDataChannel.class,immediate=true)
public class OperationExecuteChannel extends AbstractOperationExecuteChannel {

	@Activate
	public void doActivate(ComponentContext ctx) {
		super.doActivate(ctx);
//		getServer().setInterceptorIn(new TicketAccessInterceptor()); // for authentication
	}	
	
	@Deactivate
	public void doDeactivate(ComponentContext ctx) {
		super.doDeactivate(ctx);
	}
	
	@Reference
	public void setJmsManagerService(JmsManagerService manager) {
		super.setJmsManagerService(manager);
	}


	protected void doAfterReset() {
		if (getServer() != null && getServer().getInterceptorIn() == null)
			getServer().setInterceptorIn(new TicketAccessInterceptor()); // for authentication
	}

	@Override
	protected String getQueueName() {
		return "mhus.operation";
	}

	@Override
	protected String getJmsConnectionName() {
		return "mhus"; // TODO from config
	}

	@Override
	protected OperationResult doExecute(String path, IProperties properties) {

		log().d("execute operation",path,properties);
		
		OperationApi admin = Mfw.getApi(OperationApi.class);
		OperationResult res = admin.doExecute(path, properties);
		
		log().d("operation result",path,res, res == null ? "" : res.getResult());
		return res;
	}

	@Override
	protected List<String> getPublicOperations() {
		OperationApi admin = Mfw.getApi(OperationApi.class);
		LinkedList<String> out = new LinkedList<String>();
		for (String path : admin.getOperations()) {
			try {
				Operation oper = admin.getOperation(path);
				if (oper.hasAccess())
					out.add(path);
			} catch (Throwable t) {
				log().d(path,t);
			}
		}
			
		return out;
	}

	@Override
	protected OperationDescription getOperationDescription(String path) {
		OperationApi admin = Mfw.getApi(OperationApi.class);
		Operation oper = admin.getOperation(path);
		if (!oper.hasAccess()) return null;
		return oper.getDescription();
	}

}
