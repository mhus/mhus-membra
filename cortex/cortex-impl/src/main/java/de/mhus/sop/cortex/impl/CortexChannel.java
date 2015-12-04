package de.mhus.sop.cortex.impl;

import java.util.UUID;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.configupdater.ConfigString;
import de.mhus.lib.karaf.jms.JmsDataChannel;
import de.mhus.sop.mfw.api.jms.AbstractOperationListChannel;

@Component(provide=JmsDataChannel.class,immediate=true)
public class CortexChannel extends AbstractOperationListChannel {

	public static ConfigString connectionName = new ConfigString(CortexChannel.class, "connection", "cortex");
	public static ConfigString queueName = new ConfigString(CortexChannel.class, "queue", "cortex_" + UUID.randomUUID().toString());

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
