package de.mhus.sop.auris.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.query.Db;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.errors.MException;
import de.mhus.lib.karaf.MOsgi;
import de.mhus.lib.karaf.MOsgi.Service;
import de.mhus.lib.karaf.MServiceMap;
import de.mhus.lib.karaf.MServiceTracker;
import de.mhus.lib.logging.auris.AurisConst;
import de.mhus.sop.auris.api.AurisApi;
import de.mhus.sop.auris.api.AurisConnector;
import de.mhus.sop.auris.api.AurisPreprocessor;
import de.mhus.sop.auris.api.model.LogConnectorConf;
import de.mhus.sop.auris.api.model.LogEntry;
import de.mhus.sop.auris.api.model.LogPreProcessorConf;

@Component(immediate=true)
public class AurisImpl extends MLog implements AurisApi {
	
	private static AurisDbService dbService;
	private static MServiceTracker<AurisConnector> tracker = new MServiceTracker<AurisConnector>(AurisConnector.class) {
		@Override
		protected void addService(ServiceReference<AurisConnector> reference, AurisConnector service) {
			try {
				String name = MOsgi.getServiceName(reference);
				LogConnectorConf def = dbService.getManager().getObjectByQualification(Db.query(LogConnectorConf.class).eq("name", name) );
				if (def != null)
					service.doActivate(def.getProperties());
			} catch (Throwable t) {
				MLogUtil.log().d(t);
			}
		}

		@Override
		protected void removeService(ServiceReference<AurisConnector> arg0,
				AurisConnector arg1) {
			try {
				arg1.doDeactivate();
			} catch (Throwable t) {
				MLogUtil.log().d(t);
			}
		}
	};
	
	private LinkedList<PreProcessorAction> preProcessors = new LinkedList<AurisImpl.PreProcessorAction>();

	@Activate
	public void doActivate(ComponentContext ctx) {
		tracker.start();
	}
	
	@Deactivate
	public void doDeactivate(ComponentContext context) {
		tracker.stop();
	}
	
	public static void setDbService(AurisDbService aurisDbService) {
		dbService = aurisDbService;
	}

	@Override
	public void fireMessage(Map<String, String> parts) {
		if (dbService == null) return;
		log().d("received msg",parts);
		LogEntry entry = dbService.getManager().inject(new LogEntry());
		entry.setFullMessage(parts.get(AurisConst.MSG));
		entry.setSourceConnector(parts.get(AurisConst.CONNECTOR));
		entry.setSourceConnectorType(parts.get(AurisConst.CONNECTOR_TYPE));
		entry.setSourceHost(parts.get(AurisConst.REMOTE));
		entry.setCreated(System.currentTimeMillis());
		
		doPreProcess(parts, entry);
		
		try {
			entry.save();
		} catch (MException e) {
			log().e(e);
		}
	}

	private void doPreProcess(Map<String, String> parts, LogEntry entry) {
		for (PreProcessorAction def : preProcessors) {
			if (def.doPreProcess(parts, entry)) return;
		}
	}
	
	public void updateConnectors() {
		for (Service<AurisConnector> ref : MOsgi.getServiceRefs(AurisConnector.class, null)) {
			String name = ref.getName();
			try {
				LogConnectorConf def = dbService.getManager().getObjectByQualification(Db.query(LogConnectorConf.class).eq("name", name) );
				if (def != null)
					ref.getService().doActivate(def.getProperties());
				else
					ref.getService().doDeactivate();
			} catch (Throwable t) {
				log().e(name,t);
			}
		}
	}

	@Override
	public List<LogConnectorConf> getConnectorConfigurations() throws MException {
		return dbService.getManager().getAll(LogConnectorConf.class).toCacheAndClose();
	}

	@Override
	public DbManager getManager() {
		return dbService.getManager();
	}

	@Override
	public void updatePreProcessors() throws MException {
		
		HashMap<String,AurisPreprocessor> list = new HashMap<String, AurisPreprocessor>();
		for (Service<AurisPreprocessor> ref : MOsgi.getServiceRefs(AurisPreprocessor.class, null)) {
			list.put(ref.getName(), ref.getService());
		}
		
		for (LogPreProcessorConf def : dbService.getManager().getByQualification(Db.query(LogPreProcessorConf.class).desc("sort"))) {
			preProcessors.add(new PreProcessorAction(def, list.get( def.getProcessor() )));
		}
		
	}

	private class PreProcessorAction {

		private AurisPreprocessor processor;
		private LogPreProcessorConf def;

		public PreProcessorAction(LogPreProcessorConf def,
				AurisPreprocessor processor) {
			this.def = def;
			this.processor = processor;
			
			//TODO ...
			
		}

		public boolean doPreProcess(Map<String, String> parts, LogEntry entry) {
			if (processor == null) return false;
			
			
			return false;
		}
		
	}
}
