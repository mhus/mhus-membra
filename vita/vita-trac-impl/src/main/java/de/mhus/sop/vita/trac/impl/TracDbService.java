package de.mhus.sop.vita.trac.impl;

import java.util.List;

import org.osgi.service.component.ComponentContext;

import de.mhus.lib.adb.DbSchema;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.karaf.adb.DbManagerService;
import de.mhus.lib.karaf.adb.DbManagerServiceImpl;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;


@Component(provide=DbManagerService.class,name="AurisDb",immediate=true)
public class TracDbService extends DbManagerServiceImpl {

	private final CfgString dataSourceName = new CfgString(this, "dataSource", "db_auris");
	
	@Activate
	public void doActivate(ComponentContext ctx) {
		log().i("AurisDbService activate");
		TracCortex.setDbService(this);
	}
	
	@Deactivate
	public void doDeactivate(ComponentContext context) {
		log().i("AurisDbService deactivate");
		TracCortex.setDbService(null);
	}

	@Override
	public void doInitialize() throws Exception {
		super.setDataSourceName(dataSourceName.value());
	}

	@Override
	protected DbSchema doCreateSchema() {
		return new SequorDbSchema();
	}

	private class SequorDbSchema extends DbSchema {

		private SequorDbSchema() {
			tablePrefix = "sop_";
		}
		
		@Override
		public void findObjectTypes(List<Class<? extends Persistable>> list) {

		}

	}
}
