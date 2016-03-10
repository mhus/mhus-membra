package de.mhus.membra.auris.impl.pre;

import java.util.Map;

import de.mhus.membra.auris.api.AurisPreProcessor;
import de.mhus.membra.auris.api.model.LogEntry;

public class MhuTraceMark extends AurisPreProcessor {

	@Override
	public boolean doPreProcess(Map<String, String> parts, LogEntry entry) {
		
		String msg = entry.getLogMessage0();
		if (!msg.startsWith("{")) return false;
		
		for (int i = 1; i < 10; i++) {
			if (msg.length() <= i) return false;
			if (msg.charAt(i) == '}') {
				entry.setLogTrace(msg.substring(1, i));
				entry.setLogMessage0(msg.substring(i+1));
				return false;
			}
		}
		return false;
	}

	@Override
	public void doActivate() {

		
	}

	@Override
	public void doDeactivate() {
		
	}


}
