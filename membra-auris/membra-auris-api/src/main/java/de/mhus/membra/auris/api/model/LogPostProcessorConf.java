package de.mhus.membra.auris.api.model;

import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.errors.MException;
import de.mhus.osgi.sop.api.model.DbMetadata;

public class LogPostProcessorConf extends DbMetadata {

	@DbPersistent
	private String sourceHost = ".*";
	@DbPersistent
	private String sourceConnector = ".*";
	@DbPersistent
	private String sourceConnectorType = ".*";

	@DbPersistent
	private String name;
	
	@DbPersistent(size=10)
	private String sort = "100000";

	@DbPersistent
	private boolean enabled = true;

	@DbPersistent
	private MProperties properties = new MProperties();

	@Override
	public DbMetadata findParentObject() throws MException {
		return null;
	}


	public String getSourceHost() {
		return sourceHost;
	}


	public void setSourceHost(String sourceHost) {
		this.sourceHost = sourceHost;
	}


	public String getSourceConnector() {
		return sourceConnector;
	}


	public void setSourceConnector(String sourceConnector) {
		this.sourceConnector = sourceConnector;
	}


	public String getSourceConnectorType() {
		return sourceConnectorType;
	}


	public void setSourceConnectorType(String sourceConnectorType) {
		this.sourceConnectorType = sourceConnectorType;
	}

	public String getSort() {
		return sort;
	}


	public void setSort(String sort) {
		this.sort = sort;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public MProperties getProperties() {
		return properties;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

}
