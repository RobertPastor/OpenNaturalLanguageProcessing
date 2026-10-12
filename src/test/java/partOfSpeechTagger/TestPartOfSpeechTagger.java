package partOfSpeechTagger;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import docxToTextConverter.DocxToTextConverter;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


public class TestPartOfSpeechTagger {

	private static final Logger logger = Logger.getLogger(TestPartOfSpeechTagger.class.getName());

	@Test
	public void givenFrenchModel_then_PartOfSpeechTagger() throws Exception {

		logger.info("--- Running a Part Of Speech Tagger ---");
		
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
			
			InputStream posModelIn = getClass().getResourceAsStream("/models/opennlp-fr-ud-gsd-pos-1.3-2.5.4.bin");
			POSModel posModel = new POSModel(posModelIn);
			
			POSTaggerME tagger = new POSTaggerME(posModel);
			
			for (int i = 0; i < sentences.length; i++) {
				System.out.println(" ------------ ");
				System.out.println( sentences[i]);
				String[] tokens = tokenizer.tokenize(sentences[i]);
				String[] tags = tagger.tag(tokens);
				for ( int j = 0 ; j < tokens.length ; j++ ) {
					System.out.println(" =========== ");
					
					System.out.println( tokens[j] );
					System.out.println( tags[j] );
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
