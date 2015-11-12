package de.mhus.sop.auris.api.model;

import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.errors.MException;
import de.mhus.lib.karaf.MOsgi;
import de.mhus.sop.auris.api.AurisConnector;
import de.mhus.sop.mfw.api.model.DbMetadata;

public class LogConnectorConf extends DbMetadata {

	enum STATUS { UNKNOWN, DISABLED, ENABLED }
	@DbPersistent
	private String name;
	
	@DbPersistent
	private MProperties properties = new MProperties();
		
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
			return MOsgi.getService(AurisConnector.class, MOsgi.filterServiceName(name)).isActive() ? STATUS.ENABLED : STATUS.DISABLED;
		} catch (Throwable t) {
			return STATUS.UNKNOWN;
		}
	}
	
	
}
