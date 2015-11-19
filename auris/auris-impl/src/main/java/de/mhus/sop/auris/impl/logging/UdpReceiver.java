package de.mhus.sop.auris.impl.logging;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MThreadDaemon;
import de.mhus.sop.auris.api.AurisConnector;

// https://systembash.com/a-simple-java-udp-server-and-udp-client/

public abstract class UdpReceiver extends AurisConnector implements Runnable {

	private DatagramSocket serverSocket;
	private MThreadDaemon thread;
	private byte[] receiveData;
	private int bufferSize;

	
	public void doActivate() {
		
		bufferSize = config.getInt("buffer", 8192);
		receiveData = new byte[bufferSize];
		try {
			serverSocket = new DatagramSocket(port);
			thread = new MThreadDaemon(this);
			thread.start();
		} catch (SocketException e) {
			log().e(name,e);
		}
		
	}

	@Override
	public void doDeactivate() {
		if (serverSocket == null) return;
		thread.stop();
		serverSocket.close();
		thread = null;
		serverSocket = null;
	}

	@Override
	public boolean isActive() {
		return serverSocket != null;
	}
	
	@Override
	public void run() {
		while (true) {
			if (thread == null) return;
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
				serverSocket.receive(receivePacket);
                doProcess(receivePacket);
                
			} catch (Throwable e) {
				log().e(name,e);
			}
		}
	}

	protected abstract void doProcess(DatagramPacket receivePacket) throws Exception;
	
}
