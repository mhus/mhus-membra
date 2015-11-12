package de.mhus.sop.auris.api.model;

import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.errors.MException;
import de.mhus.sop.mfw.api.model.DbMetadata;

public class LogPreProcessorConf extends DbMetadata {

	@DbPersistent
	private String sourceHost;
	@DbPersistent
	private String sourceConnector;
	@DbPersistent
	private String sourceConnectorType;

	@DbPersistent
	private String processor;
	
	@DbPersistent
	private String sort;
	
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


	public String getProcessor() {
		return processor;
	}


	public void setProcessor(String processor) {
		this.processor = processor;
	}


	public String getSort() {
		return sort;
	}


	public void setSort(String sort) {
		this.sort = sort;
	}

}
