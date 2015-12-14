package de.mhus.sop.api.adb;

import java.util.UUID;

import de.mhus.lib.errors.MException;
import de.mhus.sop.api.Sop;
import de.mhus.sop.api.SopApi;
import de.mhus.sop.api.aaa.AaaContext;
import de.mhus.sop.api.aaa.Account;
import de.mhus.sop.api.model.DbMetadata;

public abstract class AbstractDbSchemaService implements DbSchemaService {

	@Override
	public boolean canRead(AaaContext account, DbMetadata obj)
			throws MException {
		return Sop.getApi(SopApi.class).isGroupMapping(account.getAccount(),obj.getClass().getName(), String.valueOf(obj.getId()), Account.ACT_READ);
	}

	@Override
	public boolean canUpdate(AaaContext account, DbMetadata obj)
			throws MException {
		return Sop.getApi(SopApi.class).isGroupMapping(account.getAccount(),obj.getClass().getName(), String.valueOf(obj.getId()), Account.ACT_UPDATE);
	}

	@Override
	public boolean canDelete(AaaContext account, DbMetadata obj)
			throws MException {
		return Sop.getApi(SopApi.class).isGroupMapping(account.getAccount(),obj.getClass().getName(), String.valueOf(obj.getId()), Account.ACT_DELETE);
	}

	@Override
	public boolean canCreate(AaaContext account, DbMetadata obj)
			throws MException {
		return Sop.getApi(SopApi.class).isGroupMapping(account.getAccount(),obj.getClass().getName(), String.valueOf(obj.getId()), Account.ACT_CREATE);
	}

	@Override
	public DbMetadata getObject(String type, UUID id) throws MException {
		try {
			Class<?> clazz = Class.forName(type, true, this.getClass().getClassLoader());
			if (clazz != null) {
				return (DbMetadata)Sop.getApi(SopApi.class).getDbManager().getObject(clazz, id);
			}
		} catch (Throwable t) {
			throw new MException("type error",type,t);
		}
		throw new MException("unknown type",type);
	}

	@Override
	public DbMetadata getObject(String type, String id) throws MException {
		try {
			return getObject(type, UUID.fromString(id));
		} catch (Throwable t) {
			throw new MException("type error",type,t);
		}
	}
	
}
