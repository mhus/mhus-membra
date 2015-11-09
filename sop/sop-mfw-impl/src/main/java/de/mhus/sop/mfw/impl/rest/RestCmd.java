package de.mhus.sop.mfw.impl.rest;

import java.util.Arrays;
import java.util.Map.Entry;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Command;

import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.sop.mfw.api.Mfw;
import de.mhus.sop.mfw.api.rest.RestNodeService;
import de.mhus.sop.mfw.api.rest.RestService;

@Command(scope = "mhus", name = "rest", description = "REST Call")
public class RestCmd implements Action {

	@Override
	public Object execute(CommandSession session) throws Exception {

        RestService restService = Mfw.getApi(RestService.class);

        ConsoleTable table = new ConsoleTable();
        table.setHeaderValues("Registered","Node Id","Parents","Class");
        for (Entry<String, RestNodeService> entry : restService.getRestNodeRegistry().entrySet()) {
        	table.addRowValues(entry.getKey(),entry.getValue().getNodeId(), Arrays.toString( entry.getValue().getParentNodeIds() ),entry.getValue().getClass().getCanonicalName() );
        }
        table.print(System.out);
		return null;
	}

}
