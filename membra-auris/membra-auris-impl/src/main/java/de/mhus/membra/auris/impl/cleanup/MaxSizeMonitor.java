package de.mhus.membra.auris.impl.cleanup;

import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbPrepared;
import de.mhus.lib.sql.DbResult;
import de.mhus.lib.sql.DbStatement;
import de.mhus.membra.auris.api.AurisApi;
import de.mhus.membra.auris.impl.AurisImpl;
import de.mhus.osgi.sop.api.Sop;

/*

SELECT table_name AS `Table`, 
round(((data_length + index_length) / 1024 / 1024), 2) `size` 
FROM information_schema.TABLES 
WHERE table_schema = "db_mws"
 AND table_name = "sop_LogEntry_";
 
 */
public class MaxSizeMonitor extends MLog {

	private long maxMb = 150;

	public boolean needSlim() {
		
		try {
			DbConnection con = Sop.getApi(AurisApi.class).getDataManager().getPool().getConnection();
			DbStatement sth = con.createStatement(
					"SELECT table_name AS \"Table\", "+
					"round(((data_length + index_length) / 1024 / 1024), 2) size "+
					"FROM information_schema.TABLES " +
					"WHERE table_schema = \"db_mws\" "+
					" AND table_name = \"sop_LogEntry_\"");

			DbResult res = sth.executeQuery(null);

			res.next();
			long sizeMb = res.getLong("size");
			
			res.close();
			sth.close();
			con.close();
			
			log().i("db size MB", sizeMb);
			
			return sizeMb > maxMb;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
