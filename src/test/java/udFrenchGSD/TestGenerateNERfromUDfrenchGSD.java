package udFrenchGSD;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import genericConlluReader.GenericConlluReader;


public class TestGenerateNERfromUDfrenchGSD {

	private static final Logger logger = Logger.getLogger(TestGenerateNERfromUDfrenchGSD.class.getName());

	
	@Test
	public void generateNERfromConlluFormat () throws Exception {
		
		logger.info("--- generate NER from UD French GSD - start  ---");

		Path resourceDirectoryPath = Paths.get("src","test","resources");
		resourceDirectoryPath = resourceDirectoryPath.resolve( "UD-French-GSD");
		
		String prefix = "train";
		String inputConlluFileName = "fr_gsd-ud-" + prefix + ".conllu";
		logger.info( " source input file name -> " + inputConlluFileName);
		
		String inputConlluFilePath = resourceDirectoryPath.resolve( inputConlluFileName ).toString();
		logger.info( " source input file path -> " + inputConlluFilePath);
		
		String outputNERtextFileName = "fr_gsd-ud-NER-" + prefix + ".txt";
		logger.info(" output NER text file name -> " + outputNERtextFileName);
		
		String outputNERtextFilePath = resourceDirectoryPath.resolve(outputNERtextFileName  ).toString();
		logger.info(" output NER text file path -> " + outputNERtextFilePath);
		
		
		GenericConlluReader genericConlluReader = new GenericConlluReader( resourceDirectoryPath , inputConlluFileName , outputNERtextFileName );
		genericConlluReader.generateNERtextOutputFile();
		

		logger.info("--- generate NER from UD French GSD - end  ---");

	}
}
