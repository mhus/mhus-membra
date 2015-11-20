package de.mhus.sop.cortex.api.model;

import java.util.UUID;

import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbType.TYPE;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.errors.MException;
import de.mhus.sop.cortex.api.CortexApi;
import de.mhus.sop.mfw.api.Mfw;
import de.mhus.sop.mfw.api.model.DbMetadata;

public class CortexProject extends DbMetadata {

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
		return Mfw.getApi(CortexApi.class).getSpace(space);
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
	
}
