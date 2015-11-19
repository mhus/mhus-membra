package de.mhus.sop.auris.impl.post;

import java.util.Map;
import java.util.Properties;

import javax.mail.Message;

import de.mhus.osgi.mail.core.MailUtil;
import de.mhus.sop.auris.api.AurisPostProcessor;
import de.mhus.sop.auris.api.model.LogEntry;

public class SendMail extends AurisPostProcessor {

	private String to;
	private String subject;

	@Override
	public void doPostProcess(Map<String, String> parts, LogEntry entry) {
//		MailUtil.sendEmailWithAttachments(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	@Override
	public void doActivate() {
		to = config.getString("to", null);
		subject = config.getString("subject","Auris Log");
		Properties properties = new Properties();
//		Message msg;
//		MailUtil.getSendQueueManager().sendMail(msg, to);
	}

	@Override
	public void doDeactivate() {
		
	}

}
