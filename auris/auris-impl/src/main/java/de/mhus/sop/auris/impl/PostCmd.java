package de.mhus.sop.auris.impl;

import java.io.PrintStream;
import java.util.Map;

import org.apache.activemq.console.util.SimpleConsole;
import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.query.Db;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.console.ANSIConsole;
import de.mhus.lib.core.console.Console;
import de.mhus.lib.core.console.Console.COLOR;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.karaf.MOsgi;
import de.mhus.lib.karaf.MOsgi.Service;
import de.mhus.sop.auris.api.AurisApi;
import de.mhus.sop.auris.api.AurisConnector;
import de.mhus.sop.auris.api.AurisPostProcessor;
import de.mhus.sop.auris.api.AurisPreProcessor;
import de.mhus.sop.auris.api.model.LogConnectorConf;
import de.mhus.sop.auris.api.model.LogEntry;
import de.mhus.sop.auris.api.model.LogPostProcessorConf;
import de.mhus.sop.auris.api.model.LogPreProcessorConf;
import de.mhus.sop.auris.api.util.VirtualPostProcessor;
import de.mhus.sop.mfw.api.Mfw;

@Command(scope = "auris", name = "post", description = "Auris post processor configuration management")
public class PostCmd implements Action {

	@Argument(index=0, name="cmd", required=true, description="Command list, set <name> <key=value>, delete, available, update", multiValued=false)
	String cmd;
	
	@Argument(index=1, name="parameters", required=false, description="More Parameters", multiValued=true)
    String[] parameters;

	@Override
	public Object execute(CommandSession session) throws Exception {
		AurisApi api = Mfw.getApi(AurisApi.class);
		if (cmd.equals("list")) {
			for (AurisPostProcessor post : api.getPostProcessors()) {
				
				LogPostProcessorConf config = post.getConfig();
				
				System.out.println(config.getName() + (post instanceof VirtualPostProcessor ? " [virtual]" : ""));
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
			LogPostProcessorConf def = manager.getObjectByQualification(Db.query(LogPostProcessorConf.class).eq("name", parameters[0]));
			if (def == null) {
				def = manager.inject(new LogPostProcessorConf());
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
			api.updatePostProcessors();
		} else
		if (cmd.equals("delete")) {
			DbManager manager = api.getManager();
			LogPostProcessorConf def = manager.getObjectByQualification(Db.query(LogPostProcessorConf.class).eq("name", parameters[0]));
			if (def != null) {
				def.delete();
				api.updatePostProcessors();
			}
		} else
		if (cmd.equals("update")) {
			api.updatePostProcessors();
		} else
		if (cmd.equals("console")) {
			final MProperties pe = new MProperties(parameters);
			final Console os = (Console) (pe.getString("console","ansi").equals("ansi") ?
					new ANSIConsole(System.in, System.out) :
					new de.mhus.lib.core.console.SimpleConsole(System.in, System.out));
			
			api.removePostProcessor("console show " + session.toString() );
			
			if (parameters != null && parameters.length > 0 && parameters[0].equals("on")) {
				
				
				
				api.addVirtualPostProcessor(new VirtualPostProcessor("console show " + session.toString(),new MProperties()) {
					
					private String[] fields;
	
					@Override
					public void doPostProcess(Map<String, String> parts, LogEntry entry) {
						StringBuffer out = new StringBuffer();
						out.append("LOG");
						for (String field : fields)
							out.append(",").append(entry.getValue(field));
						
						switch(entry.getLogLevel()) {
						case ERROR:
							os.setBold(true);
							os.setColor(COLOR.RED , COLOR.UNKNOWN);
							break;
						case FATAL:
							os.setBlink(true);
							os.setColor(COLOR.RED , COLOR.UNKNOWN);
							break;
						case WARN:
							os.setColor(COLOR.RED , COLOR.UNKNOWN);
							break;
						case DEBUG:
							os.setColor(COLOR.YELLOW , COLOR.UNKNOWN);
							break;
						case INFO:
							os.setColor(COLOR.GREEN , COLOR.UNKNOWN);
							break;
						case TRACE:
							os.setColor(COLOR.WHITE , COLOR.UNKNOWN);
							break;
						case UNKNOWN:
							os.setColor(COLOR.WHITE , COLOR.UNKNOWN);
							break;
						default:
							break;
						}
						os.println(out.toString());
						os.cleanup();
					}
					
					@Override
					public void doDeactivate() {
					}
					
					@Override
					public void doActivate() {
						fields = pe.getString("fields", 
								config.getString("fields",AurisApi.FORMATED_DATE + "," + AurisApi.LEVEL + "," + AurisApi.TRACE + "," + AurisApi.MESSAGE0) ).split(",");
						def.setSourceHost(pe.getString("host",def.getSourceHost()) );
						def.setSourceConnector(pe.getString("connector",def.getSourceConnector()));
						def.setSourceConnectorType(pe.getString("connectorType",def.getSourceConnectorType()));
					}
				});
			}
		}
	
		return null;
	}

}
