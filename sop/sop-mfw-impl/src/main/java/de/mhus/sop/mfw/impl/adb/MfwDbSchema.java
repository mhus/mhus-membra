package de.mhus.sop.mfw.impl.adb;

import java.util.List;

import de.mhus.lib.adb.DbAccessManager;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.DbObject;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.adb.model.Table;
import de.mhus.lib.adb.transaction.DbLockObject;
import de.mhus.lib.errors.AccessDeniedException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbResult;
import de.mhus.sop.mfw.api.Mfw;
import de.mhus.sop.mfw.api.MfwApi;
import de.mhus.sop.mfw.api.adb.AbstractDbSchema;
import de.mhus.sop.mfw.api.adb.DbSchemaService;
import de.mhus.sop.mfw.api.model.DbMetadata;

public class MfwDbSchema extends AbstractDbSchema {

	private MfwDbManagerService admin;
	private DbAccessManager accessManager;
	
	public MfwDbSchema() {
	}
	
	public MfwDbSchema(MfwDbManagerService admin) {
		this.admin = admin;
	}
	
	@Override
	public void findObjectTypes(List<Class<? extends Persistable>> list) {
		
		list.add(DbLockObject.class); // needed for object locking
		
		for (DbSchemaService schema : admin.getSchemas()) {
			schema.registerObjectTypes(list);
		}
		
	}


	public Object createObject(Class<?> clazz, String registryName, DbResult ret, DbManager manager, boolean isPersistent) throws Exception {
		Object object = clazz.newInstance();
		if (object instanceof DbObject) {
			((DbObject)object).doInit(manager, registryName, isPersistent);
		}
		return object;
	}

	public synchronized DbAccessManager getAccessManager(Table c) {
		if (accessManager == null)
			accessManager = new MyAccessManager();
		return accessManager;
	}
	
	private class MyAccessManager extends DbAccessManager {

		@Override
		public void hasAccess(DbManager manager, Table c, DbConnection con,
				Object object, ACCESS right) throws AccessDeniedException {

			if (object instanceof DbMetadata) {
				DbMetadata obj = (DbMetadata)object;
				try {
					MfwApi aa = Mfw.getApi(MfwApi.class);
					switch(right) {
					case CREATE:
						if (!aa.canCreate(obj))
							throw new AccessDeniedException(c.getName(),right);
						break;
					case DELETE:
						if (!aa.canDelete(obj))
							throw new AccessDeniedException(c.getName(),right);
						break;
					case READ:
						if (!aa.canRead(obj))
							throw new AccessDeniedException(c.getName(),right);
						break;
					case UPDATE:
						if (!aa.canUpdate(obj))
							throw new AccessDeniedException(c.getName(),right);
						break;
					default:
						throw new AccessDeniedException(c.getName(),right,"unknown right");
					}
				} catch (AccessDeniedException ade) {
					throw ade;
				} catch (Throwable t) {
					log().d(t);
					throw new AccessDeniedException(c.getName(),right,t);
				}
			}
		}
	}

}
