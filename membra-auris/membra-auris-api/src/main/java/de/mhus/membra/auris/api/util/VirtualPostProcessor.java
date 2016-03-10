package de.mhus.membra.auris.api.util;

import de.mhus.lib.core.MProperties;
import de.mhus.membra.auris.api.AurisPostProcessor;
import de.mhus.membra.auris.api.model.LogPostProcessorConf;

public abstract class VirtualPostProcessor extends AurisPostProcessor {

	public VirtualPostProcessor(String name, MProperties props) {
		this(name, ".*", ".*", ".*", props);
	}
	
	public VirtualPostProcessor(String name, String sourceHost, MProperties props) {
		this(name, sourceHost, ".*", ".*", props);
	}
	
	public VirtualPostProcessor(String name, String sourceHost, String sourceConnector, String sourceConnectorType, MProperties props) {
		def = new LogPostProcessorConf();
		for (String key : props.keys())
			def.getProperties().setProperty(key, props.getProperty(key));
		def.setSourceConnector(sourceConnector);
		def.setSourceConnectorType(sourceConnectorType);
		def.setSourceHost(sourceHost);
		def.setName(name);
		config = def.getProperties();
		doActivateInternal(def);
	}
	
}
