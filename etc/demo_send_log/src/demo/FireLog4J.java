package demo;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class FireLog4J {

	public static void main(String[] args) throws IOException {
		
//		BasicConfigurator.configure();
		PropertyConfigurator.configureAndWatch("log4j.properties");
		
		Logger logger = Logger.getLogger(FireLog4J.class);

		logger.trace("Trace");
		logger.debug("Debug");
		logger.info("Info");
		logger.warn("Warn");
		logger.error("Error");
		logger.fatal("Fatal");
		
		try {
			throw new Exception("The Exception");
		} catch (Throwable t) {
			logger.error("Exception Thrown", t);
		}

		logger.error("E END");
	}
	
}
