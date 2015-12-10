package de.mhus.sop.cortex.api.model;

import java.util.UUID;

import de.mhus.lib.annotations.adb.DbIndex;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.errors.MException;
import de.mhus.sop.api.model.DbMetadata;

public class CortexActivity extends DbMetadata {

	@DbPersistent(ro=true)
	private UUID space;
	@DbPersistent(ro=true)
	private UUID project;
	@DbIndex("1")
	@DbPersistent(ro=true)
	private UUID ticket;
	
	@Override
	public DbMetadata findParentObject() throws MException {
		return getDbManager().getObject(CortexTicket.class, ticket);
	}

}
