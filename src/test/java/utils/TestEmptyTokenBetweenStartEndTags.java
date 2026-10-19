package utils;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;


public class TestEmptyTokenBetweenStartEndTags {

	private static final Logger logger = Logger.getLogger(TestEmptyTokenBetweenStartEndTags.class.getName());

	
	@Test
	public void findEmptyToken() throws IOException {
		
		EmptyStartEndPatterns emptyStartEndPatterns = new EmptyStartEndPatterns();
		
		String testOne = "<START:misc>   <END>";
		logger.info(  Boolean.toString ( emptyStartEndPatterns.findEmptyStartEndPattern( testOne ) ) );;
		
		String testTwo = "<START:person> ' <END>";
		logger.info(  Boolean.toString ( emptyStartEndPatterns.findEmptyStartEndPattern( testTwo ) ) );

		String testThree = "<START:location> , <END>";
		logger.info(  Boolean.toString ( emptyStartEndPatterns.findEmptyStartEndPattern( testThree ) ) );

	}
}
