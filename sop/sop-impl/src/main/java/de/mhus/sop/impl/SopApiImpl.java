package de.mhus.sop.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import aQute.bnd.annotation.component.Component;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.MException;
import de.mhus.sop.api.SopApi;
import de.mhus.sop.api.aaa.AaaContext;
import de.mhus.sop.api.aaa.Account;
import de.mhus.sop.api.aaa.Ace;
import de.mhus.sop.api.aaa.ReferenceCollector;
import de.mhus.sop.api.aaa.Trust;
import de.mhus.sop.api.adb.DbSchemaService;
import de.mhus.sop.api.model.ActionTask;
import de.mhus.sop.api.model.DbMetadata;
import de.mhus.sop.api.model.ObjectParameter;
import de.mhus.sop.api.rest.CallContext;
import de.mhus.sop.api.rest.RestResult;

@Component(immediate=true,provide=SopApi.class,name="SopApi")
public class SopApiImpl extends MLog implements SopApi {

	@Override
	public DbManager getDbManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionTask createActionTask(String queue, String action, String target, String[] properties, boolean smart)
			throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActionTask> getQueue(String queue, int max) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ObjectParameter> getParameters(Class<?> type, UUID id) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ObjectParameter> getParameters(String type, UUID id) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGlobalParameter(String key, String value) throws MException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParameter(Class<?> type, UUID id, String key, String value) throws MException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParameter(String type, UUID id, String key, String value) throws MException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ObjectParameter getGlobalParameter(String key) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue(String type, UUID id, String key, String def) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue(Class<?> type, UUID id, String key, String def) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectParameter getParameter(String type, UUID id, String key) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectParameter getParameter(Class<?> type, UUID id, String key) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteParameters(Class<?> type, UUID id) throws MException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ObjectParameter> getParameters(Class<?> type, String key, String value) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> LinkedList<T> collectResults(AQuery<T> asc, int page) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IProperties getMainConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectParameter getRecursiveParameter(DbMetadata obj, String key) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestResult doExecuteRestAction(CallContext callContext, String action, String source) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActionTask> getActionTaskPage(String queue, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AaaContext process(String ticket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AaaContext release(String ticket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AaaContext process(Account ac, Trust trust, boolean admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AaaContext release(Account ac) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AaaContext release(AaaContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetContext() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AaaContext getCurrent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account getCurrenAccount() throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ace findGlobalAce(String account, String key) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ace findAce(String account, String type, UUID id) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Ace> findGlobalAcesForAccount(String account, String key) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Ace> findAcesForAccount(String account, String type) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canRead(DbMetadata obj) throws MException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canUpdate(DbMetadata obj) throws MException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDelete(DbMetadata obj) throws MException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canCreate(DbMetadata obj) throws MException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Ace getAce(DbMetadata id) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ace getAce(AaaContext account, DbMetadata id) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account getAccount(String account) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends DbMetadata> T getObject(Class<T> type, UUID id) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends DbMetadata> T getObject(String type, UUID id) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends DbMetadata> T getObject(String type, String id) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Entry<String, DbSchemaService>> getController() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDelete(DbMetadata object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String processAdminSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void collectRefereces(DbMetadata object, ReferenceCollector collector) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean validatePassword(Account account, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String createTrustTicket(AaaContext user) {
		// TODO Auto-generated method stub
		return null;
	}
	
}