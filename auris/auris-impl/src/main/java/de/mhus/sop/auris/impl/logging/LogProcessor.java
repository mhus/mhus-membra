package de.mhus.sop.auris.impl.logging;

import java.util.Map;

public interface LogProcessor {

	void fireMessage(Map<String, String> msg);

}
