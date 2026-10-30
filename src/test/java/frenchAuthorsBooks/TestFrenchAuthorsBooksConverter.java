package frenchAuthorsBooks;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;


public class TestFrenchAuthorsBooksConverter {
	
	private static final Logger logger = Logger.getLogger(TestFrenchAuthorsBooksConverter.class.getName());

	@Test
	public void ConvertFrenchCityNamesToNER_tokens() throws Exception {

		logger.info("--- Test French Authors Books Converter - start ---");
		
		Path resourceDirectory = Paths.get("src", "test", "resources", "frenchAuthorsBooks");
		
		String inputFileName = "autrices-auteurs-programmes-secondaire-superieur-maj-2018-07-23.csv";
		FrenchAuthorsBooksConverter frenchAuthorsBooksConverter = new FrenchAuthorsBooksConverter( resourceDirectory , inputFileName );
		
		String outputFileName = "french-authors-books-NER-input.txt";
		frenchAuthorsBooksConverter.convertCsvData(outputFileName);
		
		logger.info("--- Test French Authors Books Converter - end ---");

	}

}
