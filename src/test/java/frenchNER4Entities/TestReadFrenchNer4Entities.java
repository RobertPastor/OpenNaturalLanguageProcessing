package frenchNER4Entities;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;


public class TestReadFrenchNer4Entities {

	private static final Logger logger = Logger.getLogger(TestReadFrenchNer4Entities.class.getName());

	
	@Test
	public void readFrenchNer4Entities() throws IOException {

		logger.info("--- French NER 4 Entities carpet reader  - start ---");

		String inputFileName = "train-00000-of-00001.parquet";
		String outputFileName = "train-NER4Entities-input.txt";
		FrenchNER4EntitiesData frenchNER4EntitiesData = new FrenchNER4EntitiesData (inputFileName , outputFileName);
		frenchNER4EntitiesData.readParquet();
		
		logger.info("--- French NER 4 Entities carpet reader  - end ---");

	}
}
