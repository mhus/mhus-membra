package de.mhus.membra.auris.api;

import java.util.Map;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MProperties;
import de.mhus.membra.auris.api.model.LogConnectorConf;
import de.mhus.osgi.sop.api.Sop;

public abstract class AurisConnector extends MLog {
	
	protected int port;
	protected String name;
	protected LogConnectorConf def;
	protected MProperties config;
	
	public void doActivateInternal(LogConnectorConf def) {
		this.def = def;
		this.config = def.getProperties();
		name = def.getName();
		port = config.getInt("port", 4560);
		doActivate();
	}
	
	public String getName() {
		return name;
	}

	public abstract void doActivate();
	public abstract void doDeactivate();

	public abstract boolean isActive();

	public LogConnectorConf getConfig() {
		return def;
	}
	
	protected void fireMessage(Map<String,String> msg) {
		Sop.getApi(AurisApi.class).fireMessage(msg);
	}
	
}
