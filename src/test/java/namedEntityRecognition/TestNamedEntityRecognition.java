package namedEntityRecognition;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import docxToTextConverter.DocxToTextConverter;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


public class TestNamedEntityRecognition {

	private static final Logger logger = Logger.getLogger(TestNamedEntityRecognition.class.getName());

	@Test
	public void givenFrenchModel_whenDetectTokens_thenLemmatize() throws Exception {

		logger.info("--- Running a Named Entity Recognition tool ---");
		
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
			
			InputStream inputStreamNameFinder = getClass().getResourceAsStream("/models/en-ner-person.bin");
			TokenNameFinderModel nameFinderModel = new TokenNameFinderModel(inputStreamNameFinder);
			
			NameFinderME nameFinderME = new NameFinderME(nameFinderModel);
			//List<Span> spans = Arrays.asList(nameFinderME.find(tokens));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
