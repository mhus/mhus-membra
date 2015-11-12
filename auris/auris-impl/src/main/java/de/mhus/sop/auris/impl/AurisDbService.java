package de.mhus.sop.auris.impl;

import java.util.List;

import org.osgi.service.component.ComponentContext;

import de.mhus.lib.adb.DbSchema;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.core.configupdater.ConfigString;
import de.mhus.lib.karaf.adb.DbManagerService;
import de.mhus.lib.karaf.adb.DbManagerServiceImpl;
import de.mhus.sop.auris.api.model.LogConnectorConf;
import de.mhus.sop.auris.api.model.LogEntry;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;


@Component(provide=DbManagerService.class,name="AurisDb",immediate=true)
public class AurisDbService extends DbManagerServiceImpl {

	private final ConfigString dataSourceName = new ConfigString(this, "dataSource", "db_auris");
	
	@Activate
	public void doActivate(ComponentContext ctx) {
		log().i("AurisDbService activate");
		AurisImpl.setDbService(this);
	}
	
	@Deactivate
	public void doDeactivate(ComponentContext context) {
		log().i("AurisDbService deactivate");
		AurisImpl.setDbService(null);
	}

	@Override
	public void doInitialize() throws Exception {
		super.setDataSourceName(dataSourceName.value());
	}

	@Override
	protected DbSchema doCreateSchema() {
		return new AurisDbSchema();
	}

	private class AurisDbSchema extends DbSchema {

		private AurisDbSchema() {
			tablePrefix = "sop_";
		}
		
		@Override
		public void findObjectTypes(List<Class<? extends Persistable>> list) {
			list.add(LogEntry.class);
			list.add(LogConnectorConf.class);
		}

	}
}
