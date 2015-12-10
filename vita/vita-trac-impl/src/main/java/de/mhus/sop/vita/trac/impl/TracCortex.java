package de.mhus.sop.vita.trac.impl;

import java.util.List;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import de.mhus.sop.cortex.api.inner.CortexService;
import de.mhus.sop.cortex.api.model.CortexProject;
import de.mhus.sop.cortex.api.model.CortexSpace;
import de.mhus.sop.cortex.api.model.CortexTicket;

@Component(immediate=true)
public class TracCortex implements CortexService {

	private TracSpace space = new TracSpace();
	private static TracCortex instance;
	private static TracDbService db;
	
	@Activate
	public void doActivate(ComponentContext ctx) {
		instance = this;
	}

	@Deactivate
	public void doDeactivate(ComponentContext ctx) {
		instance = null;
	}

	@Override
	public CortexSpace getSpace() {
		return space;
	}

	@Override
	public CortexTicket getTicket(String ident) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CortexProject getProject(String ident) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CortexProject> getProjects() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void setDbService(TracDbService dbService) {
		db = dbService;
	}

}
