package de.mhus.sop.cortex.api.model;

import de.mhus.lib.core.util.MNls;
import de.mhus.lib.errors.MException;
import de.mhus.sop.mfw.api.model.DbMetadata;

public class Project extends DbMetadata {

	public MNls getNls() {
		return null;
	}
	public String getName() {
		return null;
	}
	public String getShortcut() {
		return null;
	}
	public PType[] getTypes() {
		return null;
	}
	
	@Override
	public DbMetadata findParentObject() throws MException {
		return null;
	}
	
}
