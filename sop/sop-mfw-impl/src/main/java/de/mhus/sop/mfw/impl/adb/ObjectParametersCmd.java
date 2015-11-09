package de.mhus.sop.mfw.impl.adb;

import java.util.UUID;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.query.Db;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.sop.mfw.api.Mfw;
import de.mhus.sop.mfw.api.MfwApi;
import de.mhus.sop.mfw.api.model.DbMetadata;
import de.mhus.sop.mfw.api.model.ObjectParameter;
import de.mhus.sop.mfw.api.util.ObjectUtil;

@Command(scope = "mhus", name = "parameter", description = "Handle object parameters")
public class ObjectParametersCmd implements Action {

	@Argument(index=0, name="type", required=true, description="Type of the object", multiValued=false)
    String type;

	@Argument(index=1, name="id", required=true, description="Object's UUID or - for the empty id (global)", multiValued=false)
    String id;

	@Argument(index=2, name="cmd", required=true, description="Command: list, set, remove, clean, get, recusive", multiValued=false)
    String cmd;
	
	@Argument(index=3, name="params", required=false, description="Parameters to set key=value", multiValued=true)
    String[] params;
	
	@Override
	public Object execute(CommandSession session) throws Exception {

		DbManager manager = Mfw.getApi(MfwApi.class).getDbManager();
				
		if (type.equals("parameter")) {
			
			switch(cmd) {
			case "list": {
	
				ConsoleTable out = new ConsoleTable();
				out.setHeaderValues("Key","Type","Object","Value","Id");
				
				for (ObjectParameter p : manager.getByQualification(Db.query(ObjectParameter.class).eq(Db.attr("key"), Db.value(id))))
					out.addRowValues( p.getKey(), p.getObjectType(), p.getObjectId(), p.getValue(), p.getId() );
				
				out.print(System.out);
			} break;
			}
			return null;
		} 
		
		Class<?> cType = null;
		UUID rId = MConstants.EMPTY_UUID;
		if (!"-".equals(id)) {
			DbMetadata obj = Mfw.getApi(MfwApi.class).getObject(type, id);
			if (obj == null) {
				System.out.println("Object not found");
				return null;
			}
			
			cType = obj.getClass();
			rId = obj.getId();
		} else {
		}
		
		switch(cmd) {
		case "list": {
			ConsoleTable table = new ConsoleTable();
			table.setHeaderValues("KEY","VALUE");

			for (ObjectParameter param : Mfw.getApi(MfwApi.class).getParameters(cType, rId)) {
				table.addRowValues(param.getKey(),param.getValue());
			}
			table.print(System.out);
		} break;
		case "set": {
			for (String pair : params) {
				String key = MString.beforeIndex(pair, '=');
				String value = MString.afterIndex(pair, '=');
				ObjectUtil.setParameter(cType,rId,key, value);
			}
			System.out.println("OK");
		} break;
		case "remove": {
			for (String p : params) {
				ObjectParameter pp = Mfw.getApi(MfwApi.class).getParameter(cType, rId, p);
				if (pp != null)
					pp.delete();
				else
					System.out.println("Parameter not found: " + p);
			}
			System.out.println("OK");
		} break;
		case "clean": {
			for (ObjectParameter param : Mfw.getApi(MfwApi.class).getParameters(cType, rId)) {
				param.delete();
			}
			System.out.println("OK");
		} break;
		case "get": {
			ObjectParameter p = Mfw.getApi(MfwApi.class).getParameter(cType, rId, params[0]);
			if (p != null)
				System.out.println(p.getValue());
		} break;
		case "recursive": {
			ObjectParameter p = Mfw.getApi(MfwApi.class).getParameter(cType, rId, params[0]);
			if (p != null)
				System.out.println(p.getValue());
		} break;
		}
		
		
		return null;
	}

}
