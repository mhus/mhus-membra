package de.mhus.sop.api.adb;

import java.util.UUID;

import de.mhus.lib.errors.MException;
import de.mhus.sop.api.Mfw;
import de.mhus.sop.api.MfwApi;
import de.mhus.sop.api.aaa.AaaContext;
import de.mhus.sop.api.aaa.Ace;
import de.mhus.sop.api.model.DbMetadata;

public abstract class AbstractDbSchemaService implements DbSchemaService {

	@Override
	public boolean canRead(AaaContext account, DbMetadata obj)
			throws MException {
		Ace ace = getAce(account, obj);
		if (ace == null) return false;
		return ace.canRead();
	}

	@Override
	public boolean canUpdate(AaaContext account, DbMetadata obj)
			throws MException {
		Ace ace = getAce(account, obj);
		if (ace == null) return false;
		return ace.canUpdate();
	}

	@Override
	public boolean canDelete(AaaContext account, DbMetadata obj)
			throws MException {
		Ace ace = getAce(account, obj);
		if (ace == null) return false;
		return ace.canDelete();
	}

	@Override
	public boolean canCreate(AaaContext account, DbMetadata obj)
			throws MException {
		Ace ace = getAce(account, obj);
		if (ace == null) return false;
		return ace.canCreate();
	}

	@Override
	public DbMetadata getObject(String type, UUID id) throws MException {
		try {
			Class<?> clazz = Class.forName(type, true, this.getClass().getClassLoader());
			if (clazz != null) {
				return (DbMetadata)Mfw.getApi(MfwApi.class).getDbManager().getObject(clazz, id);
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
