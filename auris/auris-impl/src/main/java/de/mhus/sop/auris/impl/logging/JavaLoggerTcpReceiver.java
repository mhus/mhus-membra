package de.mhus.sop.auris.impl.logging;

import java.net.SocketAddress;
import java.util.HashMap;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.logging.auris.AurisConst;

// https://blogs.oracle.com/CoreJavaTechTips/entry/socket_logging

public class JavaLoggerTcpReceiver extends TcpReceiver {

	public JavaLoggerTcpReceiver(IProperties config, LogProcessor processor) {
		super(config, processor);
	}

	@Override
	protected void doProcess(TcpConnection con, SocketAddress remote, String line) {
		line = line.trim();
		if (line.equals("</log>")) {
			con.close();
			return;
		}
		
		if (line.equals("<record>")) {
			// start
			con.setData(new StringBuffer().append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n").append(line));
		} else
		if (line.equals("</record>")) {
			// end
			Object data = con.getData();
			if (data != null && data instanceof StringBuffer) {
				//try {
					String raw = ((StringBuffer)data).toString();
				//	Document doc = MXml.loadXml(raw);
					HashMap<String, String> msg = new HashMap<>();
					msg.put(AurisConst.MSG, raw);
					msg.put(AurisConst.CONNECTOR, name);
					msg.put(AurisConst.CONNECTOR_TYPE, getClass().getSimpleName());
					msg.put(AurisConst.REMOTE, remote.toString());
					processor.fireMessage(msg);
//				} catch (ParserConfigurationException | SAXException
//						| IOException e) {
//					log().e(data,e);
//				}
			}
			con.setData(null);
			
		} else {
			Object data = con.getData();
			if (data != null && data instanceof StringBuffer)
				((StringBuffer)data).append(line).append('\n');
		}
		
	}

}
