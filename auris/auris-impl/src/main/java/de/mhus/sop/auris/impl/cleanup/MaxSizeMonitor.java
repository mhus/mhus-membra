package de.mhus.sop.auris.impl.cleanup;

/*

SELECT table_name AS `Table`, 
round(((data_length + index_length) / 1024 / 1024), 2) `size` 
FROM information_schema.TABLES 
WHERE table_schema = "db_mws"
 AND table_name = "sop_LogEntry_";
 
 */
public class MaxSizeMonitor {

	public boolean needSlim() {
		return false;
	}
}
