package de.mhus.sop.cortex.api.model;

import java.util.UUID;

import de.mhus.lib.annotations.adb.DbIndex;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.errors.MException;
import de.mhus.sop.mfw.api.model.DbMetadata;

public class Ticket extends DbMetadata {
	
	@DbIndex("1")
	@DbPersistent
	private UUID project;
	
	@Override
	public DbMetadata findParentObject() throws MException {
		return getDbManager().getObject(Project.class, project);
	}

}
