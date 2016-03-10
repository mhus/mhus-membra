package de.mhus.membra.auris.impl.post;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Map;

import de.mhus.lib.core.MDate;
import de.mhus.membra.auris.api.AurisApi;
import de.mhus.membra.auris.api.AurisPostProcessor;
import de.mhus.membra.auris.api.model.LogEntry;

public class Stdout extends AurisPostProcessor {

	private String[] fields;
	private File file;
	private PrintStream out;
	private long maxFileSize;

	@Override
	public void doPostProcess(Map<String, String> parts, LogEntry entry) {
		StringBuffer out = new StringBuffer();
		out.append("LOG");
		for (String field : fields)
			out.append(",").append(entry.getValue(field));
		
		synchronized(this) {
			if (file != null) {
				
				this.out.close();
				
				if (file.length() > maxFileSize) {
					file.renameTo(new File( file.getParentFile(), MDate.toFileFormat(new Date()) + "_" + file.getName() ));
					
					try {
						this.out = new PrintStream(file);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
			this.out.println(out.toString());
		}
	}

	@Override
	public void doActivate() {
		fields = config.getString("fields",AurisApi.FORMATED_DATE + "," + AurisApi.LEVEL + "," + AurisApi.MESSAGE0).split(",");
		String fileName = config.getString("file",null);
		maxFileSize = config.getLong("maxFileSize", 1024 * 1024 * 200);
		out = System.out;
		if (fileName != null) {
			file = new File(fileName);
			try {
				out = new PrintStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void doDeactivate() {
		
	}

}
