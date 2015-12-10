package de.mhus.sop.impl.adb;

import java.util.List;
import java.util.UUID;

import aQute.bnd.annotation.component.Component;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.MException;
import de.mhus.lib.karaf.adb.DbManagerService;
import de.mhus.sop.api.Mfw;
import de.mhus.sop.api.MfwApi;
import de.mhus.sop.api.aaa.AaaContext;
import de.mhus.sop.api.aaa.Ace;
import de.mhus.sop.api.aaa.Reference;
import de.mhus.sop.api.aaa.ReferenceCollector;
import de.mhus.sop.api.aaa.Reference.TYPE;
import de.mhus.sop.api.adb.DbSchemaService;
import de.mhus.sop.api.model.ActionTask;
import de.mhus.sop.api.model.DbMetadata;
import de.mhus.sop.api.model.ObjectParameter;

@Component(provide=DbSchemaService.class,immediate=true)
public class MfwDbImpl extends MLog implements DbSchemaService {

	private DbManagerService service;
	private static MfwDbImpl instance;

	public static MfwDbImpl instance() {
		return instance;
	}
	
	@Override
	public void registerObjectTypes(List<Class<? extends Persistable>> list) {
		list.add(ObjectParameter.class);
		list.add(ActionTask.class);
	}

	@Override
	public void doInitialize(DbManagerService ngnDbService) {
		this.service = ngnDbService;
		instance = this;
	}

	@Override
	public void doDestroy() {
		instance = null;
		service = null;
	}

	public static DbManager getManager() {
		return instance
				.service
				.getManager();
	}
	
	@Override
	public boolean canRead(AaaContext account, DbMetadata obj)
			throws MException {
		
		if (obj instanceof ObjectParameter) {
			ObjectParameter o = (ObjectParameter)obj;
			if (o.getKey() == null || o.getKey().startsWith("private.")) return false;
			
			String type = o.getObjectType();
			if (type == null) return false;
			if (type.equals(ObjectParameter.class.getCanonicalName())) return true;
			
			Ace ace = Mfw.getApi(MfwApi.class).findAce(account.getAccountId(), type, o.getObjectId() );
			if (ace == null) return false;

			return ace.canRead();
		}
		
		return false;
	}

	@Override
	public boolean canUpdate(AaaContext account, DbMetadata obj)
			throws MException {
		if (obj instanceof ObjectParameter) {
			ObjectParameter o = (ObjectParameter)obj;
			if (o.getKey() == null || o.getKey().startsWith("private.")) return false;
			
			String type = o.getObjectType();
			if (type == null) return false;
			if (type.equals(ObjectParameter.class.getCanonicalName())) return true;
			
			Ace ace = Mfw.getApi(MfwApi.class).findAce(account.getAccountId(), type, o.getObjectId() );
			if (ace == null) return false;

			return ace.canUpdate();
		}
		return false;
	}

	@Override
	public boolean canDelete(AaaContext account, DbMetadata obj)
			throws MException {
		if (obj instanceof ObjectParameter) {
			ObjectParameter o = (ObjectParameter)obj;
			if (o.getKey() == null || o.getKey().startsWith("private.")) return false;
			
			String type = o.getObjectType();
			if (type == null) return false;
			if (type.equals(ObjectParameter.class.getCanonicalName())) return true;
			
			Ace ace = Mfw.getApi(MfwApi.class).findAce(account.getAccountId(), type, o.getObjectId() );
			if (ace == null) return false;

			return ace.canDelete();
		}
		return false;
	}

	@Override
	public boolean canCreate(AaaContext account, DbMetadata obj)
			throws MException {
		
		if (obj instanceof ActionTask)
			return true;
		
		if (obj instanceof ObjectParameter) {
			ObjectParameter o = (ObjectParameter)obj;
			if (o.getKey() == null || o.getKey().startsWith("private.")) return false;
			
			String type = o.getObjectType();
			if (type == null) return false;
			if (type.equals(ObjectParameter.class.getCanonicalName())) return true;
			
			Ace ace = Mfw.getApi(MfwApi.class).findAce(account.getAccountId(), type, o.getObjectId() );
			if (ace == null) return false;

			return ace.canCreate();
		}
		
		return false;
	}

	@Override
	public Ace getAce(AaaContext account, DbMetadata id)
			throws MException {
		return Ace.ACE_NONE;
	}

	@Override
	public DbMetadata getObject(String type, UUID id) throws MException {
		if (type.equals(ObjectParameter.class.getCanonicalName()))
			return MfwDbImpl.getManager().getObject(ObjectParameter.class, id);
		if (type.equals(ActionTask.class.getCanonicalName()))
			return MfwDbImpl.getManager().getObject(ActionTask.class, id);
		throw new MException("unknown type",type);
	}

	@Override
	public DbMetadata getObject(String type, String id) throws MException {
		return getObject(type, UUID.fromString(id));
	}

	@Override
	public void collectReferences(DbMetadata object,
			ReferenceCollector collector) {
		if (object == null) return;
		try {
			for (ObjectParameter p : Mfw.getApi(MfwApi.class).getParameters(object.getClass(), object.getId())) {
				collector.foundReference(new Reference<DbMetadata>(p,TYPE.CHILD));
			}
		} catch (MException e) {
			log().d(object.getClass(),object.getId(),e);
		}
	}

	@Override
	public void doCleanup() {
		// TODO Auto-generated method stub
		
	}
	
}
