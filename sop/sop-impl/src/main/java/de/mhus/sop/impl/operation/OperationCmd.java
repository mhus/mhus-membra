package de.mhus.sop.impl.operation;

import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.OperationDescription;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.karaf.jms.JmsUtil;
import de.mhus.sop.api.Sop;
import de.mhus.sop.api.SopApi;
import de.mhus.sop.api.aaa.AaaContext;
import de.mhus.sop.api.operation.OperationApi;
import de.mhus.sop.api.operation.OperationBpmDefinition;

@Command(scope = "mhus", name = "operation", description = "Operation commands")
public class OperationCmd implements Action {

	@Argument(index=0, name="cmd", required=true, description="Command list, bpm, info <path>, execute <path> [key=value]*", multiValued=false)
	String cmd;
	
	@Argument(index=1, name="path", required=false, description="Path to Operation", multiValued=false)
    String path;
	
	@Argument(index=2, name="parameters", required=false, description="More Parameters", multiValued=true)
    String[] parameters;

	@Option(name="-c", aliases="--connection", description="JMS Connection Name",required=false)
	String conName = null;

	@Option(name="-q", aliases="--queue", description="JMS Connection Queue OperationChannel",required=false)
	String queueName = null;
	
	@Override
	public Object execute(CommandSession session) throws Exception {

		JmsConnection con = Sop.getDefaultJmsConnection();
		if (conName != null)
			con = JmsUtil.getConnection(conName);
		
		AaaContext acc = Sop.getApi(SopApi.class).getCurrent();
		
		OperationApi api = Sop.getApi(OperationApi.class);
		if (cmd.equals("list")) {
			if (MString.isEmpty(path) && MString.isEmpty(queueName)) {
				for (String path : api.getOperations()) {
					System.out.println(path);
				}
			} else {
				if (MString.isSet(path)) queueName = path;
				List<String> list = api.doGetOperationList(con, queueName, acc);
				if (list != null) {
					for (String item : list)
						System.out.println(item);
					System.out.println("OK");
				} else {
					System.out.println("ERROR");
				}
			}
		} else
		if (cmd.equals("bpm")) {
			ConsoleTable table = new ConsoleTable();
			table.setHeaderValues("Name","Register name", "Service Class");
			for (OperationBpmDefinition def : api.getActionDefinitions()) {
				table.addRowValues(def.getName(), def.getRegisterName(), def.getServiceClass());
			}
			table.print(System.out);
		}
		if (cmd.equals("info")) {
			
			if (MString.isEmpty(queueName)) {
				Operation oper = api.getOperation(path);
				if (oper == null) {
					System.out.println("Operation not found");
				} else {
					OperationDescription des = oper.getDescription();
					System.out.println("Description  : " + des);
					System.out.println("Form         : " + des.getForm());
//				System.out.println("BpmDefinition: " + oper.get);
				}
			} else {
				IProperties pa = new MProperties();
				pa.setString("id", path);
				OperationResult ret = api.doExecuteOperation(con, queueName, "_get", pa, acc, true);
				if (ret.isSuccessful()) {
					Object res = ret.getResult();
					if (res != null && res instanceof Map<?,?>) {
						Map<?, ?> map = (Map<?,?>)res;
						System.out.println("Description  : " + map.get("group") + "," + map.get("id"));
						System.out.println("Form         : " + map.get("form") );
					} else {
						System.out.println("Result not a map");
					}
				} else {
					System.out.println("ERROR " + ret.getMsg());
				}
			}
		} else
		if (cmd.equals("execute")) {
			MProperties properties = MProperties.explodeToMProperties(parameters);
			OperationResult res = api.doExecute(path, properties);
			System.out.println("Result: "+res);
			System.out.println("RC: " + res.getReturnCode());
			System.out.println("Object: " + res.getResult());
		}
		return null;
	}

	
}
