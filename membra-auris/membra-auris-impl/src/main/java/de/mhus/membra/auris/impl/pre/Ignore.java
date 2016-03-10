package de.mhus.membra.auris.impl.pre;

import java.util.Map;

import de.mhus.lib.core.matcher.Matcher;
import de.mhus.lib.errors.MException;
import de.mhus.membra.auris.api.AurisApi;
import de.mhus.membra.auris.api.AurisPreProcessor;
import de.mhus.membra.auris.api.model.LogEntry;

public class Ignore extends AurisPreProcessor {

	private Matcher match;
	private String field;

	@Override
	public boolean doPreProcess(Map<String, String> parts, LogEntry entry) {
		if (match == null) return false;
		return match.matches(entry.getValue(field));
	}

	@Override
	public void doActivate() {
		try {
			match = new Matcher( config.getString("condition"));
		} catch (MException e) {
			log().e(name,e);
		}
		field = config.getString("field",AurisApi.MESSAGE0);
	}

	@Override
	public void doDeactivate() {
		
	}

}
