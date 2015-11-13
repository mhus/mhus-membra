package demo;

import java.io.IOException;
import java.util.logging.Logger;

public class FireMainJavaLogger {

	public static void main(String[] args) throws IOException {
		
		System.setProperty("java.util.logging.config.file","logging.properties");
		
		Logger log = Logger.getLogger("demo");
		log.info("Eine Information");
		try {
			throw new Exception("Hoho!");
		} catch (Throwable t) {
			log.throwing("FireMain", "main", t);
		}

		log.info("WAIT\nsecond line");

		System.in.read();
		
		log.info("ENDE");
		
	}

}
