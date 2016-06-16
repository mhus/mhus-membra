package de.mhus.membra.auris.impl;

import java.util.List;

import org.osgi.service.component.ComponentContext;

import de.mhus.lib.adb.DbSchema;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.karaf.adb.DbManagerService;
import de.mhus.lib.karaf.adb.DbManagerServiceImpl;
import de.mhus.membra.auris.api.model.LogConnectorConf;
import de.mhus.membra.auris.api.model.LogEntry;
import de.mhus.membra.auris.api.model.LogPostProcessorConf;
import de.mhus.membra.auris.api.model.LogPreProcessorConf;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;


@Component(provide=DbManagerService.class,name="AurisDataDb",immediate=true)
public class AurisDataDbService extends DbManagerServiceImpl {

	private final CfgString dataSourceName = new CfgString(this, "dataSourceData", "db_auris");
	
	@Activate
	public void doActivate(ComponentContext ctx) {
		log().i("AurisDataDbService activate");
		AurisImpl.setDbData(this);
	}
	
	@Deactivate
	public void doDeactivate(ComponentContext context) {
		log().i("AurisDbService deactivate");
		AurisImpl.setDbData(null);
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
		}

	}
}
