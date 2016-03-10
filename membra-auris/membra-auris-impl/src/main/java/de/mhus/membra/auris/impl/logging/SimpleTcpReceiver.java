package de.mhus.membra.auris.impl.logging;

import java.net.SocketAddress;
import java.util.HashMap;

import de.mhus.lib.core.IProperties;
import de.mhus.membra.auris.api.AurisConst;

public class SimpleTcpReceiver extends TcpReceiver {

	@Override
	protected void doProcess(TcpConnection con, SocketAddress remote, String line) {
		HashMap<String,String> parts = new HashMap<>();
		parts.put(AurisConst.CONNECTOR, name);
		parts.put(AurisConst.MSG, line);
		parts.put(AurisConst.REMOTE, remote.toString());
		parts.put(AurisConst.CONNECTOR_TYPE, getClass().getSimpleName());
		fireMessage(parts);
	}

}
