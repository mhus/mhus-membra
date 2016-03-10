package de.mhus.membra.auris.api;

import java.util.Map;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MProperties;
import de.mhus.membra.auris.api.model.LogConnectorConf;
import de.mhus.membra.auris.api.model.LogEntry;
import de.mhus.membra.auris.api.model.LogPostProcessorConf;
import de.mhus.membra.auris.api.model.LogPreProcessorConf;

public abstract class AurisPostProcessor extends MLog {

	protected LogPostProcessorConf def;
	protected MProperties config;
	protected String name;

	public abstract void doPostProcess(Map<String, String> parts, LogEntry entry);

	public void doActivateInternal(LogPostProcessorConf def) {
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

	public LogPostProcessorConf getConfig() {
		return def;
	}

}
