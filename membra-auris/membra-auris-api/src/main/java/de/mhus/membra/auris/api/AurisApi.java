package de.mhus.membra.auris.api;

import java.util.List;
import java.util.Map;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.errors.MException;
import de.mhus.membra.auris.api.model.LogConnectorConf;
import de.mhus.membra.auris.api.util.VirtualPostProcessor;
import de.mhus.osgi.sop.api.SApi;

public interface AurisApi extends SApi {

	public static final String SOURCE0 = "s0";
	public static final String SOURCE1 = "s1";
	public static final String SOURCE2 = "s2";
	public static final String MESSAGE0 = "m0";
	public static final String MESSAGE1 = "m1";
	public static final String MESSAGE2 = "m2";
	public static final String MESSAGE3 = "m3";
	public static final String MESSAGE4 = "m4";
	public static final String MESSAGE5 = "m5";
	public static final String TRACE = "t";
	public static final String FORMATED_DATE = "fd";
	public static final String LEVEL = "l";
	public static final String SOURCE_CONNECTOR = "sc";
	public static final String SOURCE_HOST = "sh";
	public static final String SOURCE_CONNECTOR_TYPE = "sct";

	void fireMessage(Map<String, String> parts);

	void updateConnectors();

	void updatePreProcessors() throws MException;
	
	void updatePostProcessors() throws MException;
	
	DbManager getManager();

	public AurisConnector getConnector(String name);
	
	public String[] getConnectorNames();

	AurisPreProcessor[] getPreProcessors();
	
	AurisPostProcessor[] getPostProcessors();

	void addVirtualPostProcessor(VirtualPostProcessor ps) throws MException;

	void removePostProcessor(String string);

	void doCheckDatabaseSize();
	
}
