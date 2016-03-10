package de.mhus.membra.auris.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.TreeMap;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.query.Db;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MThreadDaemon;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.matcher.Matcher;
import de.mhus.lib.core.util.TimerFactory;
import de.mhus.lib.core.util.TimerIfc;
import de.mhus.lib.errors.MException;
import de.mhus.lib.karaf.MOsgi;
import de.mhus.lib.karaf.MOsgi.Service;
import de.mhus.membra.auris.api.AurisApi;
import de.mhus.membra.auris.api.AurisConnector;
import de.mhus.membra.auris.api.AurisConst;
import de.mhus.membra.auris.api.AurisPostProcessor;
import de.mhus.membra.auris.api.AurisPreProcessor;
import de.mhus.membra.auris.api.model.LogConnectorConf;
import de.mhus.membra.auris.api.model.LogEntry;
import de.mhus.membra.auris.api.model.LogPostProcessorConf;
import de.mhus.membra.auris.api.model.LogPreProcessorConf;
import de.mhus.membra.auris.api.model.LogEntry.LEVEL;
import de.mhus.membra.auris.api.util.VirtualPostProcessor;
import de.mhus.membra.auris.impl.cleanup.Diet;
import de.mhus.membra.auris.impl.cleanup.MaxSizeMonitor;
import de.mhus.lib.karaf.MServiceMap;
import de.mhus.lib.karaf.MServiceTracker;

@Component(immediate=true)
public class AurisImpl extends MLog implements AurisApi {
	
	private static AurisDbService dbService;
	
	private HashMap<String,AurisConnector> connectors = new HashMap<>();
	private LinkedList<ProcessorAction> preProcessors = new LinkedList<>();
	private LinkedList<ProcessorAction> postProcessors = new LinkedList<>();
	private LinkedList<Object[]> postQueue = new LinkedList<>();

	private TimerIfc timer;

	private MThreadDaemon postProcessor;

	private long characterCounter = 0;

	private long characterCleanupSize = 1024 * 1024 * 10; // every 10 MB of characters (e.g. 20 MB) check DB health
	

	@Activate
	public void doActivate(ComponentContext ctx) {
		postProcessor = new MThreadDaemon(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					if (postProcessor == null) return; 
					doPostProcess();
					MThread.sleep(100);
				}
			}
		},"log_post_processor");
		postProcessor.start();
	}
	
	@Deactivate
	public void doDeactivate(ComponentContext context) {
		timer.cancel();
		postProcessor = null;
		
		synchronized (connectors) {
			for (AurisConnector con : connectors.values()) {
				con.doDeactivate();
			}
		}
		
		for (ProcessorAction action : postProcessors) {
			action.post.doDeactivate();
		}

		for (ProcessorAction action : preProcessors) {
			action.pre.doDeactivate();
		}

		
	}
	
	public static void setDbService(AurisDbService aurisDbService) {
		dbService = aurisDbService;
	}
	
	@Reference(service=TimerFactory.class)
	public void setTimerFactory(TimerFactory factory) {
		if (factory != null) {
			timer = factory.getTimer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					log().i("start");
					try {
						updatePreProcessors();
					} catch (MException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						updatePostProcessors();
					} catch (MException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					updateConnectors();
				}
			}, 1000 * 5);
		}
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
		entry.setLogLevel( mapLevel( parts.get(AurisConst.LEVEL ) ) );
		entry.setLogSource0(parts.get(SOURCE0));
		entry.setLogSource1(parts.get(SOURCE1));
		entry.setLogSource2(parts.get(SOURCE2));
		entry.setLogMessage0(parts.get(MESSAGE0));
		entry.setLogMessage1(parts.get(MESSAGE1));
		entry.setLogMessage2(parts.get(MESSAGE2));
		entry.setLogMessage3(parts.get(MESSAGE3));
		entry.setLogMessage4(parts.get(MESSAGE4));
		entry.setLogMessage5(parts.get(MESSAGE5));
		entry.setLogTrace(parts.get(TRACE));
		
		if (doPreProcess(parts, entry))
			return; // do not save or post processes
		
		try {
			entry.save();
			
			if (characterCounter >= 0) {
				characterCounter += sizeOf(entry.getLogMessage0())
					+ sizeOf(entry.getLogMessage1())
					+ sizeOf(entry.getLogMessage2())
					+ sizeOf(entry.getLogMessage3())
					+ sizeOf(entry.getLogMessage4())
					+ sizeOf(entry.getLogMessage5())
					
					+ sizeOf(entry.getLogSource0())
					+ sizeOf(entry.getLogSource1())
					+ sizeOf(entry.getLogSource2())
					
					+ sizeOf(entry.getFullMessage())
	
					+ sizeOf(entry.getLogTrace())
	
					+ sizeOf(entry.getSourceConnector() )
					+ sizeOf(entry.getSourceHost() )
					+ sizeOf(entry.getSourceConnectorType() );
			}
		} catch (MException e) {
			log().e(e);
		}
		
		doPostProcess(parts, entry);
		
		if (characterCounter >= 0 && characterCounter > characterCleanupSize) {
			new MThread(new Runnable() {
				
				@Override
				public void run() {
					doCheckDatabaseSize();
				}
			}).start();
		}
	}

	public synchronized void doCheckDatabaseSize() {
		
		characterCounter = -1;
		
		log().d("doCheckDatabaseSize");
		int cnt = 0;
		while (new MaxSizeMonitor().needSlim()) {
			cnt ++;
			if (cnt > 10) {
				log().w("max doCheckDatabaseSize reached without really cleanup DB");
				characterCounter = 0;
				return;
			}
			
			log().i("doCheckDatabaseSize cleanup");
			
			new Diet().doCleanup();
			
		}
		
		characterCounter = 0;
		
	}

	private long sizeOf(String string) {
		if (string == null) return 0;
		return string.length();
	}

	private void doPostProcess(Map<String, String> parts, LogEntry entry) {
		
		synchronized (postQueue) {
			postQueue.add(new Object[] {parts,entry});
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void doPostProcess() {

		while (true) {
			Object[] item = null;
			synchronized (postQueue) {
				if (postQueue.size() == 0) return;
				item = postQueue.removeFirst();
			}
			if (item == null) return;
			
			Iterator<ProcessorAction> iter = postProcessors.iterator();
			while (iter.hasNext()) {
				ProcessorAction def = iter.next(); 
				try {
					def.doProcess((Map<String, String>)item[0], (LogEntry)item[1]);
				} catch (Throwable t) {
					log().d(def,t);
					if (def.post instanceof VirtualPostProcessor) {
						log().i("remove virtual post processor because of errors", t);
						iter.remove();
					}
				}
			}

		}
	}

	private LogEntry.LEVEL mapLevel(String level) {
		if (level == null) return LogEntry.LEVEL.UNKNOWN;
		level = level.trim().toUpperCase();
		switch (level) {
		case "FINEST":
		case "FINER":
		case "TRACE":
			return LogEntry.LEVEL.TRACE;
		case "DEBUG":
		case "FINE":
			return LogEntry.LEVEL.DEBUG;
		case "INFO":
			return LogEntry.LEVEL.INFO;
		case "WARN":
		case "WARNING":
			return LogEntry.LEVEL.WARN;
		case "ERROR":
			return LogEntry.LEVEL.ERROR;
		case "FATAL":
		case "SEVERE":
			return LogEntry.LEVEL.FATAL;
		}
		return LogEntry.LEVEL.UNKNOWN;
	}

	/**
	 * Return true to discard the message
	 * @param parts
	 * @param entry
	 * @return
	 */
	private boolean doPreProcess(Map<String, String> parts, LogEntry entry) {
		for (ProcessorAction def : preProcessors) {
			try {
				if (def.doProcess(parts, entry)) return true;
			} catch (Throwable t) {
				log().d(def,t);
			}
		}
		return false;
	}
	
	public void updateConnectors() {
		synchronized (connectors) {
			for (AurisConnector connector : connectors.values()) {
				connector.doDeactivate();
			}
			connectors.clear();
			try {
				for (LogConnectorConf def : dbService.getManager().getAll(LogConnectorConf.class) ) {
					if (!def.isEnabled()) continue;
					try {
						String clazz = def.getProperties().getString("class","");
						if (MString.isEmpty(clazz)) {
							log().d("class not set",def);
							continue;
						}
						Class<?> c = Class.forName(clazz);
						AurisConnector i = (AurisConnector)c.newInstance();
						i.doActivateInternal(def);
						connectors.put(def.getName(),i);
					} catch (Throwable t) {
						log().e(def,t);
					}
				}
			} catch (Throwable t) {
				log().e(t);
			}
		}
		
	}

	@Override
	public DbManager getManager() {
		return dbService.getManager();
	}

	@Override
	public void updatePreProcessors() throws MException {
		synchronized (preProcessors) {
			
			for (ProcessorAction action : preProcessors) {
				action.pre.doDeactivate();
			}

			preProcessors.clear();
			
			for (LogPreProcessorConf def : getManager().getByQualification(Db.query(LogPreProcessorConf.class).desc("sort").eq("enabled", true) ) ) {
				try {
					String clazz = def.getProperties().getString("class","");
					if (MString.isEmpty(clazz)) {
						log().d("class not set",def);
						continue;
					}
					Class<?> c = Class.forName(clazz);
					AurisPreProcessor i = (AurisPreProcessor)c.newInstance();
					i.doActivateInternal(def);
					preProcessors.add(new ProcessorAction(i));
				} catch (Throwable t) {
					log().e(def,t);
				}
			}
		}
	}

	@Override
	public void updatePostProcessors() throws MException {
		synchronized (postProcessors) {
			
			for (ProcessorAction action : postProcessors) {
				action.post.doDeactivate();
			}

			postProcessors.clear();
			
			for (LogPostProcessorConf def : getManager().getByQualification(Db.query(LogPostProcessorConf.class).desc("sort").eq("enabled", true) ) ) {
				try {
					String clazz = def.getProperties().getString("class","");
					if (MString.isEmpty(clazz)) {
						log().d("class not set",def);
						continue;
					}
					Class<?> c = Class.forName(clazz);
					AurisPostProcessor i = (AurisPostProcessor)c.newInstance();
					i.doActivateInternal(def);
					postProcessors.add(new ProcessorAction(i));
				} catch (Throwable t) {
					log().e(def,t);
				}
			}
		}
	}
	
	private class ProcessorAction {

		private AurisPreProcessor pre;
		private LogPreProcessorConf defPre;
		private LogPostProcessorConf defPost;
		private Matcher filterCon;
		private Matcher filterType;
		private Matcher filterHost;
		private AurisPostProcessor post;

		public ProcessorAction(AurisPreProcessor processor) throws MException {
			this.pre = processor;
			this.defPre = processor.getConfig();
			filterCon = new Matcher( defPre.getSourceConnector() );
			filterType = new Matcher( defPre.getSourceConnectorType() );
			filterHost = new Matcher( defPre.getSourceHost() );
		}

		public ProcessorAction(AurisPostProcessor processor) throws MException {
			this.post = processor;
			this.defPost = processor.getConfig();
			filterCon = new Matcher( defPost.getSourceConnector() );
			filterType = new Matcher( defPost.getSourceConnectorType() );
			filterHost = new Matcher( defPost.getSourceHost() );
		}
		
		// return true to discard the message
		public boolean doProcess(Map<String, String> parts, LogEntry entry) {
			if (pre == null && post == null) return false;
			// filter
			if (!filterHost.matches(entry.getSourceHost()) || 
				!filterCon.matches(entry.getSourceConnector()) ||
				!filterType.matches(entry.getSourceConnectorType())
			) return false;
			
			if (pre != null)
				return pre.doPreProcess(parts, entry);
			
			post.doPostProcess(parts, entry);
			return false;
		}
		
	}

	@Override
	public AurisConnector getConnector(String name) {
		synchronized (connectors) {
			return connectors.get(name);
		}
	}

	@Override
	public String[] getConnectorNames() {
		synchronized (connectors) {
			return connectors.keySet().toArray(new String[connectors.size()]);
		}
	}

	@Override
	public AurisPreProcessor[] getPreProcessors() {
		
		synchronized (preProcessors) {
			LinkedList<AurisPreProcessor> out = new LinkedList<>();
			for (ProcessorAction action : preProcessors)
				out.add(action.pre);
			return out.toArray(new AurisPreProcessor[out.size()]);
		}
	}
	
	@Override
	public AurisPostProcessor[] getPostProcessors() {
		
		synchronized (postProcessors) {
			LinkedList<AurisPostProcessor> out = new LinkedList<>();
			for (ProcessorAction action : postProcessors)
				out.add(action.post);
			return out.toArray(new AurisPostProcessor[out.size()]);
		}
	}

	public void addVirtualPostProcessor(VirtualPostProcessor ps) throws MException {
		synchronized (postProcessors) {
			postProcessors.add(new ProcessorAction(ps));
		}
	}

	@Override
	public void removePostProcessor(String name) {
		synchronized (postProcessors) {
			Iterator<ProcessorAction> iter = postProcessors.iterator();
			while (iter.hasNext()) {
				ProcessorAction def = iter.next(); 
				if (def.post.getName().equals(name))
					iter.remove();
			}
		}
	}
	
}
