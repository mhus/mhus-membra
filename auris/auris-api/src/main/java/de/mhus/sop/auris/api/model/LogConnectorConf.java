package de.mhus.sop.auris.api.model;

import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.errors.MException;
import de.mhus.lib.karaf.MOsgi;
import de.mhus.sop.auris.api.AurisApi;
import de.mhus.sop.api.Sop;
import de.mhus.sop.api.model.DbMetadata;

public class LogConnectorConf extends DbMetadata {

	enum STATUS { UNKNOWN, DISABLED, ENABLED }
	@DbPersistent
	private String name;
	
	@DbPersistent
	private MProperties properties = new MProperties();
		
	@DbPersistent
	private boolean enabled = true;
	
	@Override
	public DbMetadata findParentObject() throws MException {
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MProperties getProperties() {
		return properties;
	}

	public void setProperties(MProperties properties) {
		this.properties = properties;
	}

	public STATUS getStatus() {
		try {
			return Sop.getApi(AurisApi.class).getConnector(name).isActive() ? STATUS.ENABLED : STATUS.DISABLED;
		} catch (Throwable t) {
			return STATUS.UNKNOWN;
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
