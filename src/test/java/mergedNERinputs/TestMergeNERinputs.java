package mergedNERinputs;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;


public class TestMergeNERinputs {

	/**
	 * merge / append all text lines from the NER inputs
	 * to generate a resulting NER input text file to provide as input to the NER model training
	 */
	
	private static final Logger logger = Logger.getLogger(TestMergeNERinputs.class.getName());

	@Test
	public void MergeNER_inputTextFiles() throws Exception {

		logger.info("--- Test French NER input merger - start ---");
		
		String mergedOutputTextFileName = "outputNERmergedTextFile.txt";
		
		// list of text files to merge
		String[] inputFileNamesToMerge = new String[]{"french-first-names-NER-input.txt", 
				"train-Antilles-Conllu-NER-input.txt", 
				"train-CATIE-AQ-NER4Entities-input.txt",
				"fr_gsd-ud-NER-train.txt"};
		
		// directory under src / test / resources where to find input text files
		String resourceDirectoryString = "mergedNERinputs";
		
		MergeNERinputTextFiles mergeNERinputTextFiles = new MergeNERinputTextFiles(resourceDirectoryString , inputFileNamesToMerge ,
				mergedOutputTextFileName );
		mergeNERinputTextFiles.MergeTextFiles();
		
		logger.info("files are merged correctly");
		
		logger.info("--- Test French NER input merger - end ---");

	}
}
