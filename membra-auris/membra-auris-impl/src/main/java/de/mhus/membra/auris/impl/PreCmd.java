package de.mhus.membra.auris.impl;

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
import de.mhus.membra.auris.api.AurisPreProcessor;
import de.mhus.membra.auris.api.model.LogConnectorConf;
import de.mhus.membra.auris.api.model.LogPreProcessorConf;
import de.mhus.osgi.sop.api.Sop;

@Command(scope = "auris", name = "pre", description = "Auris pre processor configuration management")
@org.apache.karaf.shell.api.action.lifecycle.Service
public class PreCmd implements Action {

	@Argument(index=0, name="cmd", required=true, description="Command list, set <name> <key=value>, delete, available, update", multiValued=false)
	String cmd;
	
	@Argument(index=1, name="parameters", required=false, description="More Parameters", multiValued=true)
    String[] parameters;

	@Override
	public Object execute() throws Exception {
		AurisApi api = Sop.getApi(AurisApi.class);
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
			DbManager manager = api.getConfManager();
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
			DbManager manager = api.getConfManager();
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
