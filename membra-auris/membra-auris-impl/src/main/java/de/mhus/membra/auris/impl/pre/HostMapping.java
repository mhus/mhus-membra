package de.mhus.membra.auris.impl.pre;

import java.util.HashMap;
import java.util.Map;

import de.mhus.membra.auris.api.AurisPreProcessor;
import de.mhus.membra.auris.api.model.LogEntry;

public class HostMapping extends AurisPreProcessor {

	HashMap<String, String> hosts = new  HashMap<String, String>();
	
	@Override
	public boolean doPreProcess(Map<String, String> parts, LogEntry entry) {
		String host = entry.getSourceHost();
		if (host == null) return false;
		String port = "";
		int p = host.indexOf(':');
		if (p >= 0) {
			port = host.substring(p);
			host = host.substring(0,p);
		}
		String map = hosts.get(host);
		if (map == null) return false;
		if (map.length() == 0) return true; // ignore this host
		entry.setSourceHost(map + port);
		
		return false;
	}

	@Override
	public void doActivate() {
		for (String key : config.keys()) {
			if (key.startsWith("host_")) {
				hosts.put( key.substring(5), config.getString(key, ""));
			}
		}
	}

	@Override
	public void doDeactivate() {
		
	}

}
