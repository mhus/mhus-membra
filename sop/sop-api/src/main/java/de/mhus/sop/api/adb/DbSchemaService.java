package de.mhus.sop.api.adb;

import java.util.List;
import java.util.UUID;

import de.mhus.lib.adb.Persistable;
import de.mhus.lib.errors.MException;
import de.mhus.lib.karaf.adb.DbManagerService;
import de.mhus.sop.api.aaa.AaaContext;
import de.mhus.sop.api.aaa.Ace;
import de.mhus.sop.api.aaa.ReferenceCollector;
import de.mhus.sop.api.model.DbMetadata;

public interface DbSchemaService {

	void registerObjectTypes(List<Class<? extends Persistable>> list);

	void doInitialize(DbManagerService dbService);

	void doDestroy();

	boolean canRead(AaaContext context, DbMetadata obj) throws MException;
	boolean canUpdate(AaaContext context, DbMetadata obj) throws MException;
	boolean canDelete(AaaContext context, DbMetadata obj) throws MException;
	boolean canCreate(AaaContext context, DbMetadata obj) throws MException;

	Ace getAce(AaaContext context, DbMetadata obj) throws MException;

	DbMetadata getObject(String type, UUID id) throws MException;
	DbMetadata getObject(String type, String id) throws MException;
	
	void collectReferences(DbMetadata object, ReferenceCollector collector);
	
	void doCleanup();
	
}
