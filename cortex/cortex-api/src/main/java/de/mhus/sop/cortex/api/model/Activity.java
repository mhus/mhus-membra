package de.mhus.sop.cortex.api.model;

import java.util.UUID;

import de.mhus.lib.annotations.adb.DbIndex;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.errors.MException;
import de.mhus.sop.mfw.api.model.DbMetadata;

public class Activity extends DbMetadata {

	@DbIndex("1")
	@DbPersistent
	private UUID ticket;
	
	@Override
	public DbMetadata findParentObject() throws MException {
		return getDbManager().getObject(Ticket.class, ticket);
	}

}
