package de.mhus.sop.auris.impl;

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
public class AurisCortex implements CortexService {

	private AurisSpace space = new AurisSpace();
	private static AurisCortex instance;
	
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

		return null;
	}

	@Override
	public CortexProject getProject(String ident) {
		return null;
	}

	@Override
	public List<CortexProject> getProjects() {
		return null;
	}
	
	public static AurisCortex instance() {
		return instance;
	}

}
