package frenchFirstNames;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

public class TestFrenchFirstNameReader {

	private static final Logger logger = Logger.getLogger(TestFrenchFirstNameReader.class.getName());

	@Test
	public void givenFrenchModel_whenDetectTokens_thenNER() throws Exception {

		logger.info("--- Test French First Names Reader - start ---");
		
		String inputFileName = "nat2022.csv";
		FrenchFirstNameReader frenchFirstNameReader = new FrenchFirstNameReader( inputFileName );
		
		String outputFileName = "french-first-names-NER-input.txt";
		frenchFirstNameReader.readCsvData(outputFileName);
		
		logger.info("--- Test French First Names Reader - end ---");

	}
}
