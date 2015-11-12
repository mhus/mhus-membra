package de.mhus.sop.auris.api;

import java.util.List;
import java.util.Map;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.errors.MException;
import de.mhus.sop.auris.api.model.LogConnectorConf;
import de.mhus.sop.mfw.api.MApi;

public interface AurisApi extends MApi {

	void fireMessage(Map<String, String> parts);

	void updateConnectors();

	void updatePreProcessors() throws MException;
	
	List<LogConnectorConf> getConnectorConfigurations() throws MException;

	DbManager getManager();

	
	
}
