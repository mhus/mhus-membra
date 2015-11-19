package de.mhus.sop.auris.api;

import java.util.Map;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MProperties;
import de.mhus.sop.auris.api.model.LogConnectorConf;
import de.mhus.sop.auris.api.model.LogEntry;
import de.mhus.sop.auris.api.model.LogPreProcessorConf;

public abstract class AurisPreProcessor extends MLog {

	protected LogPreProcessorConf def;
	protected MProperties config;
	protected String name;

	// return true to discard the message
	public abstract boolean doPreProcess(Map<String, String> parts, LogEntry entry);

	public void doActivateInternal(LogPreProcessorConf def) {
		this.def = def;
		this.config = def.getProperties();
		name = def.getName();
		doActivate();
	}
	
	public String getName() {
		return name;
	}

	public abstract void doActivate();
	public abstract void doDeactivate();

	public LogPreProcessorConf getConfig() {
		return def;
	}

}
