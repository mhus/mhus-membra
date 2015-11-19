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
import de.mhus.sop.auris.api.AurisPreProcessor;
import de.mhus.sop.auris.api.model.LogConnectorConf;
import de.mhus.sop.auris.api.model.LogPreProcessorConf;
import de.mhus.sop.mfw.api.Mfw;

@Command(scope = "auris", name = "pre", description = "Auris pre processor configuration management")
public class PreCmd implements Action {

	@Argument(index=0, name="cmd", required=true, description="Command list, set <name> <key=value>, delete, available, update", multiValued=false)
	String cmd;
	
	@Argument(index=1, name="parameters", required=false, description="More Parameters", multiValued=true)
    String[] parameters;

	@Override
	public Object execute(CommandSession session) throws Exception {
		AurisApi api = Mfw.getApi(AurisApi.class);
		if (cmd.equals("list")) {
			for (AurisPreProcessor pre : api.getPreProcessors()) {
				
				LogPreProcessorConf config = pre.getConfig();
				
				System.out.println(config.getName());
				System.out.println("  filter:          sourceHost=" + config.getSourceHost());
				System.out.println("  filter:     sourceConnector=" + config.getSourceConnector());
				System.out.println("  filter: sourceConnectorType=" + config.getSourceConnectorType());
				System.out.println("    sort: " + config.getSort());
				System.out.println("---------------------");
				for (String name : config.getProperties().keys())
					System.out.println(name + "=" + config.getProperties().getString(name));
				System.out.println("---------------------");
				System.out.println();
			}
		} else
		if (cmd.equals("set")) {
			DbManager manager = api.getManager();
			LogPreProcessorConf def = manager.getObjectByQualification(Db.query(LogPreProcessorConf.class).eq("name", parameters[0]));
			if (def == null) {
				def = manager.inject(new LogPreProcessorConf());
				def.setName(parameters[0]);
			}
			for (int i = 1; i < parameters.length; i++) {
				String key = MString.beforeIndex(parameters[i], '=');
				String value = MString.afterIndex(parameters[i], '=');
				switch(key) {
				case "sort":
					def.setSort(value);
					break;
				case "sourceHost":
					def.setSourceHost(value);
					break;
				case "sourceConnector":
					def.setSourceConnector(value);
					break;
				case "sourceConnectorType":
					def.setSourceConnectorType(value);
					break;
				default:
					def.getProperties().setString(key, value);
				}
				
			}
			def.save();
			api.updatePreProcessors();
		} else
		if (cmd.equals("delete")) {
			DbManager manager = api.getManager();
			LogPreProcessorConf def = manager.getObjectByQualification(Db.query(LogPreProcessorConf.class).eq("name", parameters[0]));
			if (def != null) {
				def.delete();
				api.updatePreProcessors();
			}
		} else
		if (cmd.equals("update")) {
			api.updatePreProcessors();
		}
	
		return null;
	}

}
