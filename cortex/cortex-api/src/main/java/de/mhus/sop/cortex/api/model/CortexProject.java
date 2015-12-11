package de.mhus.sop.cortex.api.model;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.adb.query.Db;
import de.mhus.lib.adb.query.SearchHelper;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbType.TYPE;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.errors.MException;
import de.mhus.sop.cortex.api.outer.CortexApi;
import de.mhus.sop.api.Sop;
import de.mhus.sop.api.model.DbMetadata;

public class CortexProject extends DbMetadata {

	private static final SearchHelper PROJECT_HELPER = new SearchHelper() {
//TODO fill		
	};
	@DbPersistent(ro=true)
	private UUID space;
	@DbPersistent(size=10, ro=true)
	private String shortcut;
	@DbPersistent
	private String title;
	@DbPersistent(type=TYPE.BLOB)
	private String description;
	@DbPersistent(type=TYPE.BLOB)
	private String definition;

	public CortexProject() {}
	
	public CortexProject(UUID space, String shortcut) {
		this.space = space;
		this.shortcut = shortcut;
	}
	
	@Override
	public DbMetadata findParentObject() throws MException {
		return null;
	}

	public UUID getSpace() {
		return space;
	}

	public String getShortcut() {
		return shortcut;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<CortexTicket> getTickets(int page, int size, String search) {
		AQuery<CortexTicket> query = Db.query(CortexTicket.class).eq("project", getId());
		Db.extendObjectQueryFromSearch(query, search, PROJECT_HELPER);
		LinkedList<CortexTicket> out = new LinkedList<CortexTicket>();
		
		//XXX
		
		return out;
		
	}
	
}
