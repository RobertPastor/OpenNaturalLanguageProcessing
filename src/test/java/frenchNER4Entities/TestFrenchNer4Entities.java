package frenchNER4Entities;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;


public class TestFrenchNer4Entities {

	private static final Logger logger = Logger.getLogger(TestFrenchNer4Entities.class.getName());

	
	@Test
	public void readFrenchNer4Entities() throws IOException {

		logger.info("--- French NER 4 Entities carpet reader  - start ---");

		String fileName = "train-00000-of-00001.parquet";
		FrenchNER4EntitiesData frenchNER4EntitiesData = new FrenchNER4EntitiesData (fileName);
		frenchNER4EntitiesData.readParquet();
		
		logger.info("--- French NER 4 Entities carpet reader  - end ---");

	}
}
