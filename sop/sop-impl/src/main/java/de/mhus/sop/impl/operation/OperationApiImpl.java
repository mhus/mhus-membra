package de.mhus.sop.impl.operation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.strategy.DefaultTaskContext;
import de.mhus.lib.core.strategy.NotSuccessful;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.OperationDescription;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.util.VectorMap;
import de.mhus.lib.jms.JmsObject;
import de.mhus.sop.api.Sop;
import de.mhus.sop.api.SopApi;
import de.mhus.sop.api.jms.JmsOperationUtil;
import de.mhus.sop.api.operation.OperationApi;
import de.mhus.sop.api.operation.OperationBpmDefinition;
import de.mhus.sop.api.operation.OperationException;
import de.mhus.sop.api.operation.OperationService;

@Component(immediate=true)
public class OperationApiImpl extends MLog implements OperationApi {

	private BundleContext context;
	private ServiceTracker<OperationService,OperationService> nodeTracker;
	private HashMap<String, OperationService> register = new HashMap<>();
	private VectorMap<String, String, OperationService> groups = new VectorMap<>();
	private HashMap<String, OperationService> bpmRegister = new HashMap<>();
	

	@Activate
	public void doActivate(ComponentContext ctx) {
		context = ctx.getBundleContext();
		nodeTracker = new ServiceTracker<>(context, OperationService.class, new MyServiceTrackerCustomizer() );
		nodeTracker.open();
	}

	@Deactivate
	public void doDeactivate(ComponentContext ctx) {
		context = null;
	}

	private class MyServiceTrackerCustomizer implements ServiceTrackerCustomizer<OperationService,OperationService> {

		@Override
		public OperationService addingService(
				ServiceReference<OperationService> reference) {

			OperationService service = context.getService(reference);
			if (service != null) {
				OperationDescription desc = service.getDescription();
				if (desc != null && desc.getPath() != null) {
					log().i("register",desc);
					synchronized (register) {
						if (register.put(desc.getPath(), service) != null)
							log().w("Operation already defined",desc.getPath());
						groups.put(desc.getGroup(), desc.getId(), service);
						OperationBpmDefinition bpmDef = service.getBpmDefinition();
						if (bpmDef != null) {
							service.getBpmDefinition().setService(service);
							if (bpmRegister.put(bpmDef.getRegisterName(), service) != null) log().w("BpmDefinition already registered",bpmDef.getRegisterName());
						}
					}
				} else {
					log().i("no description found, not registered",reference.getProperty("objectClass"));
				}
			}
			return service;
		}

		@Override
		public void modifiedService(
				ServiceReference<OperationService> reference,
				OperationService service) {

			if (service != null) {
				OperationDescription desc = service.getDescription();
				if (desc != null && desc.getPath() != null) {
					log().i("modified",desc);
					synchronized (register) {
						register.put(desc.getPath(), service);
						groups.put(desc.getGroup(), desc.getId(), service);
						OperationBpmDefinition bpmDef = service.getBpmDefinition();
						if (bpmDef != null) {
							service.getBpmDefinition().setService(service);
							bpmRegister.put(bpmDef.getRegisterName(), service);
						}
					}
				}
			}
			
		}

		@Override
		public void removedService(
				ServiceReference<OperationService> reference,
				OperationService service) {
			
			if (service != null) {
				OperationDescription desc = service.getDescription();
				if (desc != null && desc.getPath() != null) {
					log().i("unregister",desc);
					synchronized (register) {
						register.remove(desc.getPath());
						groups.removeValue(desc.getGroup(), desc.getId());
						OperationBpmDefinition bpmDef = service.getBpmDefinition();
						if (bpmDef != null) bpmRegister.remove(bpmDef.getRegisterName());
					}
				}
			}			
		}
		
	}

	@Override
	public String[] getGroups() {
		synchronized (register) {
			return groups.keySet().toArray(new String[0]);
		}
	}

	@Override
	public String[] getOperations() {
		synchronized (register) {
			return register.keySet().toArray(new String[0]);
		}
	}

	@Override
	public String[] getOperationForGroup(String group) {
		synchronized (register) {
			return groups.get(group).keySet().toArray(new String[0]);
		}
	}

	@Override
	public Operation getOperation(String path) {
		synchronized (register) {
			return register.get(path);
		}
	}

	@Override
	public OperationResult doExecute(String path, IProperties properties) {
		
		int p = path.indexOf('/');
		if (p >= 0) {
			String queue = path.substring(0,p);
			path = path.substring(p+1);
			SopApi access = Sop.getApi(SopApi.class);
			try {
				OperationResult answer = JmsOperationUtil.doExecuteOperation(Sop.getDefaultJmsConnection(), queue, path, properties, access.getCurrent(), true);
				return answer;
			} catch (Exception e) {
				log().w(path,e);
				return null;
			}
		}
		
		Operation operation = getOperation(path);
		if (operation == null) return new NotSuccessful(path, "operation not found", OperationResult.NOT_FOUND);
		
		DefaultTaskContext taskContext = new DefaultTaskContext();
		taskContext.setParameters(properties);
		try {
			return operation.doExecute(taskContext);
		} catch (OperationException e) {
			log().w(path,properties,e);
			return new NotSuccessful(path,e.getMessage(), e.getReturnCode());
		} catch (Exception e) {
			log().w(path,properties,e);
			return new NotSuccessful(path,e.toString(), OperationResult.INTERNAL_ERROR);
		}
	}

	@Override
	public OperationBpmDefinition getBpmDefinition(String prozess) {
		synchronized (register) {
			OperationService o = bpmRegister.get(prozess);
			return o == null ? null : o.getBpmDefinition();
		}
	}

	@Override
	public List<OperationBpmDefinition> getBpmDefinitions() {
		synchronized (register) {
			LinkedList<OperationBpmDefinition> out = new LinkedList<OperationBpmDefinition>();
			for (OperationService service : bpmRegister.values())
				out.add(service.getBpmDefinition());
			return out;
		}
	}

}
