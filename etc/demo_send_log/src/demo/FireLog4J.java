package demo;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class FireLog4J {

	public static void main(String[] args) throws IOException {
		
		BasicConfigurator.configure();
		
		Logger logger = Logger.getLogger(FireLog4J.class);

		for (int i = 0; i < 10; i++)
		logger.info("Moin moin");
		logger.error("Moin moin");
		
		System.in.read();
	}
	
}
