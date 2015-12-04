package de.mhus.sop.cortex.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import de.mhus.lib.core.MString;
import de.mhus.lib.jms.JmsDestination;
import de.mhus.lib.jms.ServerJms;
import de.mhus.lib.karaf.services.SimpleServiceIfc;

public class CortexBroadcast extends ServerJms implements SimpleServiceIfc {

	public CortexBroadcast() {
		super(new JmsDestination("cortex",true));
	}

	@Override
	public void receivedOneWay(Message msg) throws JMSException {

	}

	@Override
	public Message received(Message msg) throws JMSException {
		TextMessage ret = getSession().createTextMessage();
		ret.setStringProperty("channel", CortexChannel.queueName.value());
		ret.setStringProperty("shortcuts", MString.join( CortexNodeImpl.instance().getSpaceListShortcuts(), "," ));
		return ret;
	}

	@Override
	public String getSimpleServiceInfo() {
		return null;
	}

	@Override
	public String getSimpleServiceStatus() {
		return null;
	}

	@Override
	public void doSimpleServiceCommand(String cmd, Object... param) {
		
	}

}
