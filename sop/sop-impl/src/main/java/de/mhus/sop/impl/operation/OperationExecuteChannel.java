package de.mhus.sop.impl.operation;

import java.util.LinkedList;
import java.util.List;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MMath;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.service.ServerIdent;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.OperationDescription;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.jms.ServerJms;
import de.mhus.lib.karaf.jms.JmsDataChannel;
import de.mhus.lib.karaf.jms.JmsManagerService;
import de.mhus.sop.api.Sop;
import de.mhus.sop.api.jms.AbstractOperationExecuteChannel;
import de.mhus.sop.api.jms.TicketAccessInterceptor;
import de.mhus.sop.api.operation.OperationApi;

@Component(provide=JmsDataChannel.class,immediate=true)
public class OperationExecuteChannel extends AbstractOperationExecuteChannel {

	public static CfgString queueName = new CfgString(OperationExecuteChannel.class, "queue", "mhus.operation." + MSingleton.baseLookup(null, ServerIdent.class));
	public static CfgString connectionName = new CfgString(OperationExecuteChannel.class, "connection", "mhus");
	static OperationExecuteChannel instance;
	
	@Activate
	public void doActivate(ComponentContext ctx) {
		super.doActivate(ctx);
		if (MSingleton.getCfg(OperationExecuteChannel.class).getBoolean("accessControl", true))
			getServer().setInterceptorIn(new TicketAccessInterceptor());
		instance = this;
	}	
	
	@Deactivate
	public void doDeactivate(ComponentContext ctx) {
		instance = null;
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
		return  queueName.value();
	}

	@Override
	protected String getJmsConnectionName() {
		return connectionName.value(); 
	}

	@Override
	protected OperationResult doExecute(String path, IProperties properties) {

		log().d("execute operation",path,properties);
		
		OperationApi admin = Sop.getApi(OperationApi.class);
		OperationResult res = admin.doExecute(path, properties);
		
		log().d("operation result",path,res, res == null ? "" : res.getResult());
		return res;
	}

	@Override
	protected List<String> getPublicOperations() {
		OperationApi admin = Sop.getApi(OperationApi.class);
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
		OperationApi admin = Sop.getApi(OperationApi.class);
		Operation oper = admin.getOperation(path);
		if (!oper.hasAccess()) return null;
		return oper.getDescription();
	}

}
