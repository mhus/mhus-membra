package de.mhus.sop.cortex.impl.inner;

import java.util.UUID;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.karaf.jms.JmsDataChannel;
import de.mhus.sop.api.jms.AbstractOperationListChannel;

@Component(provide=JmsDataChannel.class,immediate=true)
public class CortexChannel extends AbstractOperationListChannel {

	public static CfgString connectionName = new CfgString(CortexChannel.class, "connection", "cortex");
	public static CfgString queueName = new CfgString(CortexChannel.class, "queue", "cortex_" + UUID.randomUUID().toString());

	@Activate
	public void doActivate(ComponentContext ctx) {
//		add(new );
	}
	
	@Override
	protected String getQueueName() {
		return queueName.value();
	}

	@Override
	protected String getJmsConnectionName() {
		return connectionName.value();
	}

}
