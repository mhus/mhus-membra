package de.mhus.sop.auris.impl.logging;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.logging.auris.AurisSender;
import de.mhus.lib.logging.auris.SimpleUdpSender;

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
			return new SimpleUdpSender(config);
		}
	}
	
	public AurisReceiver createReceiver(IProperties config, LogProcessor processor) {
		switch(config.getString("type","").toLowerCase()) {
		
		case "javaloggertcpreceiver":
		case "javalogger":
			return new JavaLoggerTcpReceiver(config, processor);
		case "udp":
		case"simpleudpreceiver":
			return new SimpleUdpReceiver(config, processor);
		default:
			return new SimpleTcpReceiver(config, processor);
		}
		
	}
	
}
