package de.mhus.sop.auris.impl.post;

import java.util.Map;
import java.util.Properties;

import javax.mail.Message;

import de.mhus.lib.core.MTimeInterval;
import de.mhus.osgi.mail.core.MailUtil;
import de.mhus.osgi.mail.core.SendQueueManager;
import de.mhus.sop.auris.api.AurisPostProcessor;
import de.mhus.sop.auris.api.model.LogEntry;

public class OutOfMemoryMail extends AurisPostProcessor {

	private String to;
	private String subject;
	private String from;
	private long lastSend = 0;

	@Override
	public void doPostProcess(Map<String, String> parts, LogEntry entry) {
		
		if (entry.getLogMessage0().indexOf("OutOfMemoryException") > 0) {
			
			if (System.currentTimeMillis() - lastSend > MTimeInterval.MINUTE_IN_MILLISECOUNDS * 30 ) {
				String message = "OutOfMemoryException: " + entry.getSourceHost();
				try {
					MailUtil.sendEmailWithAttachments(SendQueueManager.QUEUE_DEFAULT, from, to, subject, message);
				} catch (Exception e) {
					log().e(e);
				}
				lastSend = System.currentTimeMillis();
			}
		}
	}

	@Override
	public void doActivate() {
		to = config.getString("to", null);
		subject = config.getString("subject","Auris Log");
		from = config.getString("from", "no_reply@noreply.org");
	}

	@Override
	public void doDeactivate() {
		
	}

}
