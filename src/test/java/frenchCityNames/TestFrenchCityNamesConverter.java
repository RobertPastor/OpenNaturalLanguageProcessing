package frenchCityNames;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;


public class TestFrenchCityNamesConverter {

	private static final Logger logger = Logger.getLogger(TestFrenchCityNamesConverter.class.getName());

	@Test
	public void ConvertFrenchCityNamesToNER_tokens() throws Exception {

		logger.info("--- Test French City Names Converter - start ---");
		
		Path resourceDirectory = Paths.get("src","test","resources","frenchCityNames");
		
		String inputFileName = "communes-francaises.csv";
		FrenchCityNamesConverter frenchCityNamesReader = new FrenchCityNamesConverter( resourceDirectory , inputFileName );
		
		String outputFileName = "french-city-names-NER-input.txt";
		frenchCityNamesReader.convertCsvData(outputFileName);
		
		logger.info("--- Test French City Names Converter - end ---");

	}
	
}
