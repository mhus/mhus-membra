package de.mhus.sop.api;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.errors.MException;
import de.mhus.sop.api.aaa.AaaContext;
import de.mhus.sop.api.aaa.Account;
import de.mhus.sop.api.aaa.ReferenceCollector;
import de.mhus.sop.api.aaa.Trust;
import de.mhus.sop.api.adb.DbSchemaService;
import de.mhus.sop.api.model.ActionTask;
import de.mhus.sop.api.model.DbMetadata;
import de.mhus.sop.api.model.ObjectParameter;
import de.mhus.sop.api.rest.CallContext;
import de.mhus.sop.api.rest.RestResult;

public interface SopApi extends SApi {

	int PAGE_SIZE = 100;

	DbManager getDbManager();
	
	ActionTask createActionTask(String queue, String action, String target, String[] properties, boolean smart) throws MException;

	List<ActionTask> getQueue(String queue, int max) throws MException;

	List<ObjectParameter> getParameters(Class<?> type, UUID id)
			throws MException;

	List<ObjectParameter> getParameters(String type, UUID id) throws MException;

	void setGlobalParameter(String key, String value) throws MException;

	void setParameter(Class<?> type, UUID id, String key, String value)
			throws MException;

	void setParameter(String type, UUID id, String key, String value)
			throws MException;

	ObjectParameter getGlobalParameter(String key) throws MException;

	String getValue(String type, UUID id, String key, String def)
			throws MException;

	String getValue(Class<?> type, UUID id, String key, String def)
			throws MException;

	ObjectParameter getParameter(String type, UUID id, String key)
			throws MException;

	ObjectParameter getParameter(Class<?> type, UUID id, String key)
			throws MException;

	void deleteParameters(Class<?> type, UUID id) throws MException;

	List<ObjectParameter> getParameters(Class<?> type, String key, String value) throws MException;

	<T> LinkedList<T> collectResults(AQuery<T> asc, int page) throws MException;
	
	IProperties getMainConfiguration();

	ObjectParameter getRecursiveParameter(DbMetadata obj, String key) throws MException;

	/**
	 * Executes/Create a BPM Case. The Method is specially for REST execution.
	 * 
	 * @param callContext The call context of the request
	 * @param action A special action or null if the default call action should be used
	 * @param source The source on which node the is executed
	 * @return The result for the REST handling
	 * @throws MException
	 */
	RestResult doExecuteRestAction(CallContext callContext, String action, String source) throws MException;

	List<ActionTask> getActionTaskPage(String queue, int size);
	
	// access
	
	AaaContext process(String ticket);
	AaaContext release(String ticket);
	AaaContext process(Account ac, Trust trust, boolean admin);
	AaaContext release(Account ac);
	AaaContext release(AaaContext context);
	void resetContext();

	AaaContext getCurrent();
	
	Account getCurrenAccount() throws MException;

	boolean canRead(DbMetadata obj) throws MException;
	boolean canUpdate(DbMetadata obj) throws MException;
	boolean canDelete(DbMetadata obj) throws MException;
//	boolean canCreate(Object parent, String newType) throws MException;
//	boolean canCreate(Object parent, Class<?> newType) throws MException;
	boolean canCreate(DbMetadata obj) throws MException;
	
	Account getAccount(String account) throws MException;
	
	<T extends DbMetadata> T getObject(Class<T> type, UUID id) throws MException;
	<T extends DbMetadata> T getObject(String type, UUID id) throws MException;
	<T extends DbMetadata> T getObject(String type, String id) throws MException;
	
	Set<Entry<String, DbSchemaService>> getController();

	void onDelete(DbMetadata object);
	String processAdminSession();
	void collectRefereces(DbMetadata object, ReferenceCollector collector);
	boolean validatePassword(Account account, String password);

	String createTrustTicket(AaaContext user);

	boolean isGroupMapping(Account account, String mappingName, String id, String action);

}
