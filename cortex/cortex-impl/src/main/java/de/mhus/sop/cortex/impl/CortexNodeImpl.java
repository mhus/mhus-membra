package de.mhus.sop.cortex.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import de.mhus.lib.core.MCollection;
import de.mhus.lib.karaf.MServiceList;
import de.mhus.lib.karaf.MServiceTracker;
import de.mhus.sop.cortex.api.CortexNode;
import de.mhus.sop.cortex.api.CortexService;
import de.mhus.sop.cortex.api.model.CortexSpace;

@Component(immediate=true)
public class CortexNodeImpl implements CortexNode {

	private static CortexNodeImpl instance;

	private MServiceTracker<CortexService> tracker;
	private HashMap<String, CortexSpace> serviceIndex = new HashMap<>();
	
	public static CortexNodeImpl instance() {
		return instance;
	}
	
	@Activate
	public void doActivate(ComponentContext ctx) {
		tracker = new MServiceTracker<CortexService>(ctx.getBundleContext(), CortexService.class) {

			@Override
			protected void removeService(ServiceReference<CortexService> reference, CortexService service) {
				serviceIndex.remove(service.getSpace().getShortcut());
			}

			@Override
			protected void addService(ServiceReference<CortexService> reference, CortexService service) {
				serviceIndex.put(service.getSpace().getShortcut(), service.getSpace());
			}
		};
		tracker.start();
		instance = this;
	}

	@Deactivate
	public void doDeactivate(ComponentContext ctx) {
		instance = null;
		tracker.stop();
	}

	public List<CortexSpace> getSpaceList() {
		return new LinkedList<CortexSpace>( serviceIndex.values() );
	}

	public List<String> getSpaceListShortcuts() {
		return new LinkedList<String>( serviceIndex.keySet() );
	}
}
