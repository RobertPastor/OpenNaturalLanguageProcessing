package tokenizer;

import java.util.logging.Logger;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import docxToTextConverter.DocxToTextConverter;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;



import sentenceDetection.TestSentenceBoundaryDisambiguation;

public class TestTokenizer {

	
	private static final Logger logger = Logger.getLogger(TestSentenceBoundaryDisambiguation.class.getName());
	
	@Test
	public void givenFrenchModel_whenDetectSentence_thenToken() throws Exception {

		logger.info("--- Running a tokenizer detector tool ---");
		
		try {

			InputStream sentenceInputStream = getClass().getResourceAsStream("/models/opennlp-fr-ud-gsd-sentence-1.3-2.5.4.bin");
			SentenceModel sentenceModel = new SentenceModel(sentenceInputStream);
			
			InputStream tokenizerModelIn = getClass().getResourceAsStream("/models/opennlp-fr-ud-gsd-tokens-1.3-2.5.4.bin");
			TokenizerModel tokenizerModel = new TokenizerModel(tokenizerModelIn);
			TokenizerME tokenizer = new TokenizerME(tokenizerModel);
	
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
			String wordInputfilePath = "/inputs/Le village englouti.docx";
		
			String extractedText = new DocxToTextConverter().convertDocxToText(wordInputfilePath);
			String sentences[] = sentenceDetector.sentDetect(extractedText);
			
			for (int i = 0; i < sentences.length; i++) {
				System.out.println(" ------------ ");
				System.out.println( sentences[i]);
				String[] tokens = tokenizer.tokenize(sentences[i]);
				for ( int j = 0 ; j < tokens.length ; j++ ) {
					System.out.println( tokens[j] );
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
