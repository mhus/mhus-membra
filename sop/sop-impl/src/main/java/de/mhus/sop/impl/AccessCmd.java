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
import de.mhus.sop.api.adb.DbSchemaService;
import de.mhus.sop.api.model.DbMetadata;
import de.mhus.sop.api.util.ObjectUtil;

@Command(scope = "sop", name = "access", description = "Access actions")
public class AccessCmd implements Action {

	@Argument(index=0, name="cmd", required=true, description=
			"Command login <account>,"
			+ " logout, id, info,"
			+ " cache, cache.clear, local.cache.clear, local.cache.cleanup,"
			+ " synchronize <account>,"
			+ " validate <account> <password>,"
			+ " synchronizer <type>,"
			+ " access <account> <name> [<action>]"
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
		if (cmd.equals("group")) {
			Account ac = api.getAccount(parameters[0]);
			return ac.hasGroup(parameters[1]);
		} else
		if (cmd.equals("access")) {
			Account ac = api.getAccount(parameters[0]);
			if (parameters.length > 3)
				return api.isGroupMapping(ac, parameters[1], parameters[2], parameters[3]);
			else
			if (parameters.length > 2)
				return api.isGroupMapping(ac, parameters[1], null, parameters[2]);
			else
				return api.isGroupMapping(ac, parameters[1], null, null);
		} else
		if (cmd.equals("info")) {
			Account ac = api.getAccount(parameters[0]);
			System.out.println(ac);
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
