package de.mhus.membra.auris.impl;

import java.util.HashMap;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.query.Db;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.karaf.MOsgi;
import de.mhus.lib.karaf.MOsgi.Service;
import de.mhus.membra.auris.api.AurisApi;
import de.mhus.membra.auris.api.AurisConnector;
import de.mhus.membra.auris.api.AurisConst;
import de.mhus.membra.auris.api.model.LogConnectorConf;
import de.mhus.osgi.sop.api.Sop;

@Command(scope = "auris", name = "connector", description = "Auris connector configuration management")
@org.apache.karaf.shell.api.action.lifecycle.Service
public class ConnectorCmd implements Action {

	@Argument(index=0, name="cmd", required=true, description="Command list, set <name> <key=value>, delete, available, update", multiValued=false)
	String cmd;
	
	@Argument(index=1, name="parameters", required=false, description="More Parameters", multiValued=true)
    String[] parameters;

	@Override
	public Object execute() throws Exception {
		AurisApi api = Sop.getApi(AurisApi.class);
		if (cmd.equals("list")) {
			for (String configName : api.getConnectorNames()) {
				
				AurisConnector connector = api.getConnector(configName);
				if (connector == null) continue;
				LogConnectorConf config = connector.getConfig();
				
				System.out.println(config.getName() + " " + config.getStatus());
				System.out.println("---------------------");
				for (String name : config.getProperties().keys())
					System.out.println(name + "=" + config.getProperties().getString(name));
				System.out.println();
			}
		} else
		if (cmd.equals("set")) {
			DbManager manager = api.getConfManager();
			LogConnectorConf def = manager.getObjectByQualification(Db.query(LogConnectorConf.class).eq("name", parameters[0]));
			if (def == null) {
				def = manager.inject(new LogConnectorConf());
				def.setName(parameters[0]);
			}
			for (int i = 1; i < parameters.length; i++) {
				String key = MString.beforeIndex(parameters[i], '=');
				String value = MString.afterIndex(parameters[i], '=');
				def.getProperties().setString(key, value);
			}
			def.save();
			api.updateConnectors();
		} else
		if (cmd.equals("delete")) {
			DbManager manager = api.getConfManager();
			LogConnectorConf def = manager.getObjectByQualification(Db.query(LogConnectorConf.class).eq("name", parameters[0]));
			if (def != null) {
				def.delete();
				api.updateConnectors();
			}
		} else
		if (cmd.equals("available")) {
			for (Service<AurisConnector> ref : MOsgi.getServiceRefs(AurisConnector.class, null)) {
				String name = ref.getName();
				System.out.println(name + ": " + ref.getService().isActive());
			}
		} else
		if (cmd.equals("update")) {
			api.updateConnectors();
		} else
		if (cmd.equals("debug") || cmd.equals("info") || cmd.equals("error") || cmd.equals("trace") || cmd.equals("fatal")) {
			HashMap<String, String> parts = new HashMap<>();
			parts.put(AurisConst.CONNECTOR,"manual");
			parts.put(AurisConst.CONNECTOR_TYPE,"manual");
			parts.put(AurisConst.REMOTE,"manual");
			parts.put(AurisConst.LEVEL,cmd);
			parts.put(AurisConst.MSG,parameters[0]);
			parts.put(AurisApi.MESSAGE0,parameters[0]);

			api.fireMessage(parts);
		} else
		if (cmd.equals("chkdb")) {
			api.doCheckDatabaseSize();
		}
	
		return null;
	}

}
