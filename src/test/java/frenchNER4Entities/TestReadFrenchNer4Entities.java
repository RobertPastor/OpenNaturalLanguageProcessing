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
		String fileType = "train";
		//FrenchNER4EntitiesData frenchNER4EntitiesData = new FrenchNER4EntitiesData ( fileType , inputFileName , outputFileName);
		//frenchNER4EntitiesData.readCATIE_AQ_NER4EntitiesParquet();
		
		inputFileName = "test-00000-of-00001.parquet";
		outputFileName = "test-NER4Entities-input.txt";
		fileType = "test";
		FrenchNER4EntitiesData frenchNER4EntitiesData = new FrenchNER4EntitiesData ( fileType , inputFileName , outputFileName);
		frenchNER4EntitiesData.readCATIE_AQ_NER4EntitiesParquet();
		
		inputFileName = "validation-00000-of-00001.parquet";
		outputFileName = "validation-NER4Entities-input.txt";
		fileType = "validation";
		frenchNER4EntitiesData = new FrenchNER4EntitiesData (fileType , inputFileName , outputFileName);
		frenchNER4EntitiesData.readCATIE_AQ_NER4EntitiesParquet();
		
		logger.info("--- French NER 4 Entities carpet reader  - end ---");
	}
}
