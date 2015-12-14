package de.mhus.sop.impl;

import java.util.Map.Entry;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.sop.api.Sop;
import de.mhus.sop.api.SopApi;
import de.mhus.sop.api.aaa.AaaContext;
import de.mhus.sop.api.aaa.Account;
import de.mhus.sop.api.aaa.Ace;
import de.mhus.sop.api.adb.DbSchemaService;
import de.mhus.sop.api.model.DbMetadata;
import de.mhus.sop.api.util.ObjectUtil;

@Command(scope = "sop", name = "access", description = "Access actions")
public class AccessCmd implements Action {

	@Argument(index=0, name="cmd", required=true, description=
			"Command login <account>,"
			+ " logout, id, info, acl,"
			+ " ace <type> <uuid>,"
			+ " cache, cache.clear, local.cache.clear, local.cache.cleanup,"
			+ " synchronize <account>,"
			+ " validate <account> <password>,"
			+ " synchronizer <type>,"
			+ " reloadconfig", multiValued=false)
	String cmd;
	
	@Argument(index=1, name="parameters", required=false, description="More Parameters", multiValued=true)
    String[] parameters;
	
	@Option(name="-a", aliases="--admin", description="Connect user as admin",required=false)
	boolean admin = false;

	@Override
	public Object execute(CommandSession session) throws Exception {

		SopApi api = Sop.getApi(SopApi.class);
		if (api == null) {
			System.out.println("SOP API not found");
			return null;
		}
		
		if (cmd.equals("validate")) {
			Account account = api.getAccount(parameters[0]);
			System.out.println("Result: " + api.validatePassword(account, parameters[1] ) );
		} else
		if (cmd.equals("login")) {
			Account ac = api.getAccount(parameters[0]);
			AaaContext cur = api.process(ac, null, admin);
			System.out.println(cur);
		} else
		if (cmd.equals("logout")) {
			AaaContext cur = api.getCurrent();
			cur = api.release(cur.getAccount());
			System.out.println(cur);
		} else
		if (cmd.equals("id")) {
			AaaContext cur = api.getCurrent();
			System.out.println(cur);
		} else
		if (cmd.equals("root")) {
			api.resetContext();
			AaaContext cur = api.getCurrent();
			System.out.println(cur);
		} else
		if (cmd.equals("info")) {
			Account ac = api.getAccount(parameters[0]);
			System.out.println(ac);
		} else
		if (cmd.equals("acl")) {
			AaaContext cur = api.getCurrent();
			ConsoleTable table = new ConsoleTable();
			table.setHeaderValues("Type","Taget","Key","Rights");
//			for (Ace ace : aa.findGlobalAcesForAccount(cur.getAccountId(), null)) {
//				table.addRowValues(ace.getOrganization(),ace.getType(),ace.getTarget(),ace.getKey(),ace.getRights());
//			}
//			table.addRowValues("---","---","---","---","---");
			for (Ace ace : api.findAcesForAccount(cur.getAccountId(), null)) {
				table.addRowValues(ace.getType(),ace.getTarget(),ace.getKey(),ace.getRights());
			}
			table.print(System.out);
		} else
		if(cmd.equals("controllers")) {
			ConsoleTable table = new ConsoleTable();
			table.setHeaderValues("Type","Controller","Bundle");
			for (Entry<String, DbSchemaService> entry : api.getController()) {
				Bundle bundle = FrameworkUtil.getBundle(entry.getValue().getClass());
				table.addRowValues(entry.getKey(), entry.getValue().getClass(), bundle.getSymbolicName() );
			}
			table.print(System.out);
		} else
		if (cmd.equals("ace")) {
			DbMetadata obj = api.getObject( parameters[0], parameters[1] );
			System.out.println(api.getAce( obj ) );
		} else
		if (cmd.equals("cache")) {
			AaaContextImpl context = (AaaContextImpl) api.getCurrent();
			System.out.println("Cache Size: " + context.cacheSize());
		} else
		if (cmd.equals("local.cache.clear")) {
			AaaContextImpl context = (AaaContextImpl) api.getCurrent();
			System.out.println("Cache Size: " + context.cacheSize());
			context.clearCache();
			System.out.println("Cache Size: " + context.cacheSize());
		} else
		if (cmd.equals("local.cache.cleanup")) {
			AaaContextImpl context = (AaaContextImpl) api.getCurrent();
			System.out.println("Cache Size: " + context.cacheSize());
			context.cleanupCache();
			System.out.println("Cache Size: " + context.cacheSize());
		} else
			System.out.println("Command not found: " + cmd);
			
		
		return null;
	}

}
