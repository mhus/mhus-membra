package de.mhus.membra.auris.impl.logging;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;

import javax.net.ServerSocketFactory;

import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import org.osgi.framework.FrameworkUtil;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MThreadDaemon;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.io.MObjectInputStream;
import de.mhus.lib.core.util.Rfc1738;
import de.mhus.membra.auris.api.AurisApi;
import de.mhus.membra.auris.api.AurisConnector;
import de.mhus.membra.auris.api.AurisConst;

// Sender:
// http://www.docjar.com/html/api/org/apache/log4j/net/SocketAppender.java.html

// Receiver:
// http://www.docjar.com/html/api/org/apache/log4j/net/SocketServer.java.html
// http://www.docjar.com/html/api/org/apache/log4j/net/SocketNode.java.html

public class Log4JTcpReceiver extends AurisConnector implements Runnable {

	private static final String LEVEL_STR = "level";
	private static final String LEVEL_INT = "leveli";
	private static final String LOGGER_NAME = "name";
	private static final String NDC = "ndc";
	private static final String THREAD_NAME = "thread";
	private static final String MESSAGE = "msg";
	private static final String MESSAGE_RENDERED = "msgr";
	private static final String LOC_CLASS = "class";
	private static final String LOC_FILE = "file";
	private static final String LOC_LINE = "line";
	private static final String LOC_METHOD = "meth";
	private static final String LOC_FULL = "full";
	private static final String THROWABLE = "thrown";
	private MThreadDaemon thread;
	private ServerSocket serverSocket;

	public void doActivate() {
		ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
		try {
			serverSocket = serverSocketFactory.createServerSocket(port);
			thread = new MThreadDaemon(this);
			thread.start();
		} catch (IOException e) {
			log().e(name,e);
		}
	}

	@Override
	public void doUpdate(boolean portChanged) {
		if (portChanged) {
			try {
				serverSocket.close();
				ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
				serverSocket = serverSocketFactory.createServerSocket(port);
			} catch (IOException e) {
				log().e(name,e);
			}
		}
	}

	@Override
	public void run() {
		log().i("listen",name,port);
		try {
			while(true) {
				if (thread == null) {
					try {
						log().i("close listener",name,port);
						if (serverSocket != null && !serverSocket.isClosed())
							serverSocket.close();
					} catch (IOException e) {
						log().e(name,e);
					}
					serverSocket = null;
					return;
				}
				try {
					Socket socket = serverSocket.accept();
					new TcpConnection(socket);
				} catch (SocketTimeoutException ste) {
					
				} catch (InterruptedIOException ie) {
					
				}
			}
		} catch (IOException e) {
			log().e(name,e);
		}

		try {
			log().d("close listener after error",name,port);
			if (serverSocket != null && !serverSocket.isClosed())
				serverSocket.close();
		} catch (IOException e) {
			log().e(name,e);
		}
		serverSocket = null;


	}

	protected void doProcess(TcpConnection con, SocketAddress remote, LoggingEvent event) {
		Rfc1738 map = new Rfc1738();
		map.put(LEVEL_STR, event.getLevel().toString());
		map.put(LEVEL_INT, String.valueOf( event.getLevel().toInt()) );
		map.put(LOGGER_NAME, event.getLoggerName());
		map.put(NDC, event.getNDC());
		map.put(THREAD_NAME, event.getThreadName());
		map.put(MESSAGE, String.valueOf( event.getMessage()) );
		map.put(MESSAGE_RENDERED, event.getRenderedMessage());
		map.put(LOC_CLASS, event.getLocationInformation().getClassName());
		map.put(LOC_FILE, event.getLocationInformation().getFileName());
		map.put(LOC_LINE, event.getLocationInformation().getLineNumber());
		map.put(LOC_METHOD, event.getLocationInformation().getMethodName());
		map.put(LOC_FULL, event.getLocationInformation().fullInfo);
		if (event.getThrowableStrRep() != null)
			map.put(THROWABLE, Arrays.deepToString( event.getThrowableStrRep() ) ); 
		
		
		HashMap<String, String> parts = new HashMap<>();
		parts.put(AurisConst.CONNECTOR, name);
		parts.put(AurisConst.MSG, map.toString());
		parts.put(AurisConst.REMOTE, remote.toString());
		parts.put(AurisConst.CONNECTOR_TYPE, getClass().getSimpleName());
		parts.put(AurisConst.LEVEL, event.getLevel().toString() );
		parts.put(AurisApi.SOURCE0, event.getLoggerName() );
		parts.put(AurisApi.SOURCE1, event.getLocationInformation().getClassName() );
		parts.put(AurisApi.SOURCE2, event.getLocationInformation().getMethodName() );
		parts.put(AurisApi.TRACE, event.getThreadName());
		
		parts.put(AurisApi.MESSAGE0, event.getRenderedMessage() );
		
		if (event.getThrowableStrRep() != null)
			parts.put(AurisApi.MESSAGE5, toExceptionString(event) ); 
		
		fireMessage(parts);
	}

	private String toExceptionString(LoggingEvent event) {
		StringBuffer sb = new StringBuffer();
		for (String line : event.getThrowableStrRep())
			sb.append(line).append('\n');
		return sb.toString();
	}

	public class TcpConnection implements Runnable {

		private Socket socket;
		private MThreadDaemon t;
		private SocketAddress remote;
		private Object data;

		public TcpConnection(Socket socket) {
			this.socket = socket;
			remote = socket.getRemoteSocketAddress();
			t = new MThreadDaemon(this);
			t.start();
		}

		@Override
		public void run() {
			log().i("new connection", name, remote);
//			LoggingEvent le = new LoggingEvent("a", null, Priority.DEBUG, "a", null);
//			doProcess(this, remote, le);
			try {
				InputStream is = socket.getInputStream();
				ClassLoader cl = LoggingEvent.class.getClassLoader();
				ObjectInputStream ois = new MObjectInputStream( new BufferedInputStream( is ), cl );
				while(true) {
					if (t == null || thread == null) {
						log().i("close connection", name, remote);
						try {
							if (!socket.isClosed())
								socket.close();
						} catch (IOException e) {
							log().e(name,e);
						}
						return;
					}
					
					LoggingEvent event = (LoggingEvent) ois.readObject();
					// ois.reset();
			        if (event != null)
			        	doProcess(this, remote, event);
			        else
			        	MThread.sleep(100);
				}
			} catch(java.io.EOFException | java.net.SocketException e) {
				log().i("Exception will close the connection",e);
				t = null;
			} catch (Exception e) {
				log().e(name,e);
			}
		}

		public void setData(Object data) {
			this.data = data;
		}
		
		public Object getData() {
			return data;
		}

		public void close() {
			t = null;
		}
	}

	@Override
	public void doDeactivate() {
		if (thread == null) return;
		thread = null;
		// no more working t.throwException(new InterruptedIOException("stop listening"));
		try {
			if (serverSocket != null)
				serverSocket.close();
			long start = System.currentTimeMillis();
			while (serverSocket != null) {// wait for timeout
				MThread.sleep(100);
				if (System.currentTimeMillis() - start > MTimeInterval.SECOUND_IN_MILLISECOUNDS * 30) {
					log().i("stop timeout", name);
					break;
				}
			}
			log().d("listener stopped",name);
		} catch (IOException e) {
			log().e(name,e);
		}
	}

	@Override
	public boolean isActive() {
		return thread != null;
	}
}
