package de.mhus.sop.auris.impl.logging;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MLog;

public abstract class AurisReceiver extends MLog {

	protected int port;
	protected String name;
	protected LogProcessor processor;
	
	public AurisReceiver(IProperties config, LogProcessor processor) {
		this.processor = processor;
		name = config.getString("name",null);
		port = config.getInt("port", 4560);
	}

	public abstract void close();
}
