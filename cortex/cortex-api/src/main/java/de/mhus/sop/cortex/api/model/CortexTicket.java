package de.mhus.sop.cortex.api.model;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.mhus.lib.adb.query.Db;
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
	
	protected List<CortexActivity> activities;
	protected List<CortexAttribute> attributes;
	private HashMap<String,CortexAttribute> attributeIndex;
	
	@Override
	public DbMetadata findParentObject() throws MException {
		return null;
	}

	public synchronized List<CortexActivity> getActivities() throws MException {
		if (activities == null) loadActivities();
		return activities;
	}
	
	protected void loadActivities() throws MException {
		activities = getDbManager().getByQualification(Db.query(CortexActivity.class).eq("ticket", getId()).asc("creationdate") ).toCacheAndClose();
	}

	public synchronized List<CortexAttribute> getAttributes() throws MException {
		if (attributes == null) loadAttributes();
		return attributes;
	}

	protected void loadAttributes() throws MException {
		attributes = getDbManager().getByQualification(Db.query(CortexAttribute.class).eq("ticket", getId())).toCacheAndClose();
		attributeIndex = new HashMap<>();
		for (CortexAttribute a :attributes)
			attributeIndex.put(a.getKey(), a);
	}

}
