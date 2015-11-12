package de.mhus.sop.auris.impl;

import java.util.LinkedList;
import java.util.Map;

import aQute.bnd.annotation.component.Component;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.util.PropertiesSubset;
import de.mhus.sop.auris.api.AurisApi;
import de.mhus.sop.auris.api.AurisConnector;
import de.mhus.sop.auris.impl.logging.AurisFactory;
import de.mhus.sop.auris.impl.logging.AurisReceiver;
import de.mhus.sop.auris.impl.logging.LogProcessor;
import de.mhus.sop.mfw.api.Mfw;

@Component(immediate=true,name=DefaultConnector.NAME)
public class DefaultConnector extends MLog implements AurisConnector, LogProcessor {

	public static final String NAME = "default";
	LinkedList<AurisReceiver> receivers = null;
	
	@Override
	public void doActivate(IProperties properties) {
		if (isActive()) doDeactivate();
		synchronized (this) {
			receivers = new LinkedList<AurisReceiver>();
			for (String name : properties.keys()) {
				if (name.endsWith(".name"))
					receivers.add( AurisFactory.instance().createReceiver( 
							new PropertiesSubset(properties, name.substring(0, name.length() - 4)),
							this)
							);
			}
		}
	}

	@Override
	public void doDeactivate() {
		if (receivers == null) return;
		synchronized (this) {
			for (AurisReceiver i : receivers)
				try {
					i.close();
				} catch (Throwable t) {
					log().d(t);
				}
			receivers = null;
		}
		
	}

	@Override
	public boolean isActive() {
		return receivers != null;
	}

	@Override
	public void fireMessage(Map<String, String> msg) {
		Mfw.getApi(AurisApi.class).fireMessage(msg);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDisplayName() {
		return "Default";
	}

}
