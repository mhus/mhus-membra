package de.mhus.sop.cortex.api.model;

import java.util.UUID;

import de.mhus.lib.annotations.adb.DbIndex;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.errors.MException;
import de.mhus.sop.cortex.api.CortexApi;
import de.mhus.sop.mfw.api.Mfw;
import de.mhus.sop.mfw.api.model.DbMetadata;

public class CortexTicket extends DbMetadata {
	
	@DbIndex("1")
	@DbPersistent
	private UUID project;
	@DbPersistent
	private UUID space;
	
	@Override
	public DbMetadata findParentObject() throws MException {
		return Mfw.getApi(CortexApi.class).getSpace(space).getProject(project);
	}

}
