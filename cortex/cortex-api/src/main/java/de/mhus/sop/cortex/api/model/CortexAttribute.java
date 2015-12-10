package de.mhus.sop.cortex.api.model;

import java.util.Date;
import java.util.UUID;

import de.mhus.lib.annotations.adb.DbIndex;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbType.TYPE;
import de.mhus.lib.errors.MException;
import de.mhus.sop.api.model.DbMetadata;

public class CortexAttribute extends DbMetadata {

	@DbPersistent(ro=true)
	private UUID space;
	@DbPersistent(ro=true)
	private UUID project;
	@DbIndex("1")
	@DbPersistent(ro=true)
	private UUID ticket;
	@DbPersistent(ro=true)
	private String key;
	
	@DbPersistent
	private String valueStr;
	@DbPersistent
	private Date valueDate;
	@DbPersistent
	private long valueLong;
	@DbPersistent(type=TYPE.BLOB)
	private String valueBlob;
	
	@Override
	public DbMetadata findParentObject() throws MException {
		return getDbManager().getObject(CortexTicket.class, ticket);
	}

	public String getKey() {
		return key;
	}

}
