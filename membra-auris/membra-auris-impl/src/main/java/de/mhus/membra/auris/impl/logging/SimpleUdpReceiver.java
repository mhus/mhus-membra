package de.mhus.membra.auris.impl.logging;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Map;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.util.Rfc1738;
import de.mhus.membra.auris.api.AurisConst;

public class SimpleUdpReceiver extends UdpReceiver {

	@Override
	protected void doProcess(DatagramPacket receivePacket) throws Exception {
			InetAddress ip = receivePacket.getAddress();
	        String msg = new String (receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength(), MString.CHARSET_UTF_8);
	        Map<String, String> parts = Rfc1738.explode(msg);
	        parts.put(AurisConst.CONNECTOR, name);
	        parts.put(AurisConst.REMOTE, ip.getHostName());
	        fireMessage(parts);
	}

}
