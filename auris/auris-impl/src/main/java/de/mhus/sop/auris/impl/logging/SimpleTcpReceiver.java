package de.mhus.sop.auris.impl.logging;

import java.net.SocketAddress;
import java.util.HashMap;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.logging.auris.AurisConst;

public class SimpleTcpReceiver extends TcpReceiver {

	public SimpleTcpReceiver(IProperties config, LogProcessor processor) {
		super(config, processor);
	}

	@Override
	protected void doProcess(TcpConnection con, SocketAddress remote, String line) {
		HashMap<String,String> parts = new HashMap<>();
		parts.put(AurisConst.CONNECTOR, name);
		parts.put(AurisConst.MSG, line);
		parts.put(AurisConst.REMOTE, remote.toString());
		parts.put(AurisConst.CONNECTOR_TYPE, getClass().getSimpleName());
		processor.fireMessage(parts);
	}

}
