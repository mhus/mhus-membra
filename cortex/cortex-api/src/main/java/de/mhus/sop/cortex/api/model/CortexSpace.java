package de.mhus.sop.cortex.api.model;

import java.util.UUID;

import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbType.TYPE;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.errors.MException;
import de.mhus.sop.cortex.api.CortexApi;
import de.mhus.sop.mfw.api.Mfw;
import de.mhus.sop.mfw.api.model.DbMetadata;

public class CortexSpace extends DbMetadata {

	@DbPersistent(size=10, ro=true)
	private String shortcut;
	@DbPersistent
	private String title;
	@DbPersistent(type=TYPE.BLOB)
	private String description;
	@DbPersistent(type=TYPE.BLOB)
	private String definition;
	@DbPersistent(ro=true)
	private String handler;
	private Model model;
	
	public MNls getNls() {
		return null;
	}
	public String getName() {
		return null;
	}
	public String getShortcut() {
		return null;
	}
	public ModelType[] getTypes() {
		return null;
	}

	@Override
	public DbMetadata findParentObject() throws MException {
		return null;
	}
	public String getHandler() {
		return handler;
	}

	public synchronized Model getModel() {
		if (model == null) {
			model = Mfw.getApi(CortexApi.class).getCortexSpaceHandler(handler).getModel(this, description);
		}
		return model;
	}
	
	public CortexProject getProject(UUID id) {
		return null;
	}
	
	public CortexProject getProject(String shortcut) {
		return null;
	}
	
}
