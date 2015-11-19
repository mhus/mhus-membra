package de.mhus.sop.auris.impl.logging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import javax.net.ServerSocketFactory;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MThreadDaemon;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.sop.auris.api.AurisConnector;

public abstract class TcpReceiver extends AurisConnector implements Runnable {

	private MThreadDaemon thread;
	private ServerSocket serverSocket;
	private String charset;

	@Override
	public void doActivate() {
		ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
		charset = config.getString("charset",MString.CHARSET_UTF_8);
		try {
			serverSocket = serverSocketFactory.createServerSocket(port);
			thread = new MThreadDaemon(this);
			thread.start();

		} catch (IOException e) {
			log().e(name,e);
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

	protected abstract void doProcess(TcpConnection con, SocketAddress remote, String line);

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
			try {
				InputStream is = socket.getInputStream();
		        BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
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
					
			        String line = br.readLine();
			        if (line != null)
			        	doProcess(this, remote, line);
			        else
			        	MThread.sleep(100);
				}
			} catch (IOException e) {
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

}
