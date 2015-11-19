package demo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FireMainJavaLogger {

	public static void main(String[] args) throws IOException {
		
		System.setProperty("java.util.logging.config.file","logging.properties");
		
		Logger log = Logger.getLogger("demo");
		log.finest("Finest");
		log.finer("Finer");
		log.fine("Fine");
		log.info("Info");
		log.warning("Warn");
		log.severe("Severe");
		log.entering("class.entering", "method");
		log.info("Info in class.entering");
		log.exiting("class.exiting", "method");
		log.config("Config");
		try {
			throw new Exception("The Exception");
		} catch (Throwable t) {
			log.throwing("Exception Thrown", "main", t);
		}

		log.info("WAIT\nsecond line");

//		System.in.read();
		
		log.info("ENDE");
		
	}

}
