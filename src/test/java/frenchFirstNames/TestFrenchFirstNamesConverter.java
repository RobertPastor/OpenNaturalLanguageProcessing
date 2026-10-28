package frenchFirstNames;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

public class TestFrenchFirstNamesConverter {

	private static final Logger logger = Logger.getLogger(TestFrenchFirstNamesConverter.class.getName());

	@Test
	public void givenFrenchModel_whenDetectTokens_thenNER() throws Exception {

		logger.info("--- Test French First Names Reader - start ---");
		
		String inputFileName = "nat2022.csv";
		FrenchFirstNamesConverter frenchFirstNameReader = new FrenchFirstNamesConverter( inputFileName );
		
		String outputFileName = "french-first-names-NER-input.txt";
		frenchFirstNameReader.readCsvData(outputFileName);
		
		logger.info("--- Test French First Names Reader - end ---");

	}
}
