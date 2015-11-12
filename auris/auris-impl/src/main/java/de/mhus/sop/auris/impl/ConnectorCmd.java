package de.mhus.sop.auris.impl;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.query.Db;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.karaf.MOsgi;
import de.mhus.lib.karaf.MOsgi.Service;
import de.mhus.sop.auris.api.AurisApi;
import de.mhus.sop.auris.api.AurisConnector;
import de.mhus.sop.auris.api.model.LogConnectorConf;
import de.mhus.sop.mfw.api.Mfw;

@Command(scope = "auris", name = "connector", description = "Auris connector configuration management")
public class ConnectorCmd implements Action {

	@Argument(index=0, name="cmd", required=true, description="Command list, set <name> <key=value>*", multiValued=false)
	String cmd;
	
	@Argument(index=1, name="parameters", required=false, description="More Parameters", multiValued=true)
    String[] parameters;

	@Override
	public Object execute(CommandSession session) throws Exception {
		AurisApi api = Mfw.getApi(AurisApi.class);
		if (cmd.equals("list")) {
			for (LogConnectorConf config : api.getConnectorConfigurations()) {
				System.out.println(config.getName() + " " + config.getStatus());
				System.out.println("---------------------");
				for (String name : config.getProperties().keys())
					System.out.println(name + "=" + config.getProperties().getString(name));
				System.out.println();
			}
		} else
		if (cmd.equals("set")) {
			DbManager manager = api.getManager();
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
			DbManager manager = api.getManager();
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
		}
	
		return null;
	}

}
