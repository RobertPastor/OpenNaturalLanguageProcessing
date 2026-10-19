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
import opennlp.tools.util.Span;


public class TestNamedEntityRecognition {

	private static final Logger logger = Logger.getLogger(TestNamedEntityRecognition.class.getName());

	@Test
	public void givenFrenchModel_whenDetectTokens_thenNER() throws Exception {

		logger.info("--- Running a Named Entity Recognition tool ---");
		
		try {

			// sentence detector
			InputStream sentenceInputStream = getClass().getResourceAsStream("/models/opennlp-fr-ud-gsd-sentence-1.3-2.5.4.bin");
			SentenceModel sentenceModel = new SentenceModel(sentenceInputStream);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
			
			// read input word file
			String wordInputfilePath = "/inputs/Le village englouti.docx";
			String extractedText = new DocxToTextConverter().convertDocxToText(wordInputfilePath);
			String sentences[] = sentenceDetector.sentDetect(extractedText);

			// tokenizer model
			InputStream tokenizerModelIn = getClass().getResourceAsStream("/models/opennlp-fr-ud-gsd-tokens-1.3-2.5.4.bin");
			TokenizerModel tokenizerModel = new TokenizerModel(tokenizerModelIn);
			TokenizerME tokenizerME = new TokenizerME(tokenizerModel);
	
			// Named Entity Recognition
			InputStream nameEntityRecogIn = getClass().getResourceAsStream("/models/fr-ner-train-person.bin");
			TokenNameFinderModel nameFinderModel = new TokenNameFinderModel(nameEntityRecogIn);
			NameFinderME nameFinderME = new NameFinderME(nameFinderModel);
			
			//List<Span> spans = Arrays.asList(nameFinderME.find(tokens));
			
			for (int i = 0; i < sentences.length; i++) {
				logger.info(" ------------ " + String.valueOf(i) + " ------------ ");
				System.out.println( sentences[i] );
				String[] tokens = tokenizerME.tokenize(sentences[i]);
				
				Span nameSpans[] = nameFinderME.find( tokens );
				for ( int j = 0 ; j < nameSpans.length ; j++ ) {
					logger.info(" ================== " + String.valueOf(j) + " ================== ");
					logger.info( tokens[nameSpans[j].getStart()]  );
					logger.info(  nameSpans[j].getType() );
				}
			}
			nameFinderME.clearAdaptiveData();
			
			// close streams
			sentenceInputStream.close();
			tokenizerModelIn.close();
			nameEntityRecogIn.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
