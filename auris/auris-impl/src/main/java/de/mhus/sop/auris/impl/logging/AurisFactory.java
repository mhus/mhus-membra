package de.mhus.sop.auris.impl.logging;

import de.mhus.lib.core.IProperties;
import de.mhus.sop.auris.api.AurisConnector;
import de.mhus.sop.auris.impl.AurisSender;
import de.mhus.sop.auris.impl.SimpleUdpSender;

public class AurisFactory {

	private static AurisFactory instance;

	public static synchronized AurisFactory instance() {
		if (instance == null) instance = new AurisFactory(); // TODO load from 'Base'
		return instance;
	}
	
	public AurisSender createSender(IProperties config) {
		switch(config.getString("type","")) {
		case "udp":
			return new SimpleUdpSender(config);
		default:
			return null;
		}
	}
	
	public AurisConnector createReceiver(IProperties config) {
		switch(config.getString("type","").toLowerCase()) {
		
		case "javaloggertcpreceiver":
		case "javalogger":
			return new JavaLoggerTcpReceiver();
		case "log4jtcpreceiver":
		case "log4j":
			return new Log4JTcpReceiver();
		case "udp":
		case"simpleudpreceiver":
			return new SimpleUdpReceiver();
		default:
			return null;
		}
		
	}
	
}
