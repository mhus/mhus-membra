package de.mhus.sop.api.util;

import java.io.File;

import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.logging.FileLogger;
import de.mhus.sop.api.Mfw;
import de.mhus.sop.api.MfwApi;
import de.mhus.sop.api.aaa.AaaContext;

// TODO implement asynchrony logging
public class MfwFileLogger extends FileLogger {

	private static CfgString logDir = new CfgString(MfwFileLogger.class, "logDirectory", null) {
		@Override
		protected void onPreUpdate(String newValue) {
			if (newValue == null) return;
					new File(newValue).mkdirs();
		}
	};
	private String logName;
	
	public MfwFileLogger(String name, String logName) {
		super(name, null );
		this.logName = logName;
	}

	@Override
	protected String getInfo() {
		StringBuffer out = new StringBuffer();
		out.append(Thread.currentThread().getId()).append(',');
		out.append(Thread.currentThread().getName()).append(',');
		
		AaaContext context = Mfw.getApi(MfwApi.class).getCurrent();
		out.append(context.getAccountId());
		
		return out.toString();
	}

	@Override
    protected void prepare(StringBuffer sb) {
	}

	@Override
	protected void doUpdateFile() {
		file = logDir.value() == null ? null : new File(logDir.value(), logName + ".log");
	}

}
