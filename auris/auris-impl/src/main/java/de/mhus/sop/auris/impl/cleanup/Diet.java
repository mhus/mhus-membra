package de.mhus.sop.auris.impl.cleanup;

import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbResult;
import de.mhus.lib.sql.DbStatement;
import de.mhus.sop.auris.api.AurisApi;
import de.mhus.sop.mfw.api.Mfw;

public class Diet extends MLog {

	public void doCleanup() {
		// simple diet algo ...
		
		try {
			
			long min = 0;
			long max = System.currentTimeMillis();
			
			DbConnection con = Mfw.getApi(AurisApi.class).getManager().getPool().getConnection();
			{
				DbStatement sth = con.createStatement(
						"select min(created_) AS min, max(created_) AS max FROM sop_LogEntry_");
				DbResult res = sth.executeQuery(null);
				res.next();
				min = res.getLong("min");
				max = res.getLong("max");
				res.close();
				sth.close();
			}
			
			
			// remove 1/10 of all messages
			
			long maxAge = max - min / 10 + min;
			
			{
				DbStatement sth = con.createStatement( "DELETE FROM sop_LogEntry_ WHERE created_ < " + maxAge );
				int res = sth.executeUpdate(null);
				log().i("removed",res);
				
				sth.close();
				con.commit();
			}

			{
				DbStatement sth = con.createStatement( "OPTIMIZE TABLE sop_LogEntry_;" );
				DbResult res = sth.executeQuery(null);
				res.close();
				sth.close();
				con.commit();
			}
			
			con.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
