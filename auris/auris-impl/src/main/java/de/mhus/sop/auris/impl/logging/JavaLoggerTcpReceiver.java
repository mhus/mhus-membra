package de.mhus.sop.auris.impl.logging;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MXml;
import de.mhus.lib.logging.auris.AurisConst;
import de.mhus.sop.auris.api.AurisApi;

// https://blogs.oracle.com/CoreJavaTechTips/entry/socket_logging

public class JavaLoggerTcpReceiver extends TcpReceiver {

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
				((StringBuffer)data).append(line);
				try {
					String raw = ((StringBuffer)data).toString();
					Document doc = MXml.loadXml(raw);
					HashMap<String, String> msg = new HashMap<>();
					msg.put(AurisConst.MSG, raw);
					msg.put(AurisConst.CONNECTOR, name);
					msg.put(AurisConst.CONNECTOR_TYPE, getClass().getSimpleName());
					msg.put(AurisConst.REMOTE, remote.toString());
					msg.put(AurisConst.LEVEL, MXml.getValue(doc.getDocumentElement(), "level", ""));
					msg.put(AurisApi.SOURCE0, MXml.getValue(doc.getDocumentElement(), "logger", ""));
					msg.put(AurisApi.SOURCE1, MXml.getValue(doc.getDocumentElement(), "class", ""));
					msg.put(AurisApi.SOURCE2, MXml.getValue(doc.getDocumentElement(), "method", ""));
					msg.put(AurisApi.TRACE, MXml.getValue(doc.getDocumentElement(), "thread", ""));

					msg.put(AurisApi.MESSAGE0,MXml.getValue(doc.getDocumentElement(), "message", ""));
					Element ex = MXml.getElementByPath(doc.getDocumentElement(), "exception");
					if (ex != null)
						msg.put(AurisApi.MESSAGE5, toExceptionString(ex) );
						
					fireMessage(msg);
				} catch (ParserConfigurationException | SAXException
						| IOException e) {
					log().e(data,e);
				}
			}
			con.setData(null);
			
		} else {
			Object data = con.getData();
			if (data != null && data instanceof StringBuffer)
				((StringBuffer)data).append(line).append('\n');
		}
		
	}

	private String toExceptionString(Element ex) {
		StringBuffer sb = new StringBuffer();
		sb.append(MXml.getValue(ex, "message" , "") ).append("\n");
		for (Element frame : MXml.getLocalElementIterator(ex, "frame"))
			sb	.append("\tat ")
				.append(MXml.getValue(frame, "class",""))
				.append(".")
				.append(MXml.getValue(frame, "method",""))
				.append("(")
				.append(MString.afterLastIndex(MXml.getValue(frame, "class",""), '.') )
				.append(":")
				.append(MXml.getValue(frame, "line",""))
				.append(")\n");
		
		return sb.toString();
	}

}

/*
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<record><date>2015-11-19T12:03:16</date>
<millis>1447930996681</millis>
<sequence>2</sequence>
<logger>demo</logger>
<level>INFO</level>
<class>demo.FireMainJavaLogger</class>
<method>main</method>
<thread>1</thread>
<message>WAIT
second line</message>
<exception>
 <message>java.lang.Exception: Hoho!</message>
 <frame>
  <class>demo.FireMainJavaLogger</class>
  <method>main</method>
  <line>15</line>
 </frame>
</exception>
</record>
*/