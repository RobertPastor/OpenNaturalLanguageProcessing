package lemmatizer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import docxToTextConverter.DocxToTextConverter;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


public class TestLemmatizer {

	private static final Logger logger = Logger.getLogger(TestLemmatizer.class.getName());

	@Test
	public void givenFrenchModel_thenLemmatizer() throws Exception {

		logger.info("--- Running a Lemmatizer ---");
		
		try {

			// sentence detector
			InputStream sentenceInputStream = getClass().getResourceAsStream("/models/opennlp-fr-ud-gsd-sentence-1.3-2.5.4.bin");
			SentenceModel sentenceModel = new SentenceModel(sentenceInputStream);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
			
			// read word input file
			String wordInputfilePath = "/inputs/Le village englouti.docx";
			String extractedText = new DocxToTextConverter().convertDocxToText(wordInputfilePath);
			String sentences[] = sentenceDetector.sentDetect(extractedText);
			
			// tokenizer model
			InputStream tokenizerModelIn = getClass().getResourceAsStream("/models/opennlp-fr-ud-gsd-tokens-1.3-2.5.4.bin");
			TokenizerModel tokenizerModel = new TokenizerModel(tokenizerModelIn);
			TokenizerME tokenizerME = new TokenizerME(tokenizerModel);
	
			// part of speech tagging
			InputStream posModelIn = getClass().getResourceAsStream("/models/opennlp-fr-ud-gsd-pos-1.3-2.5.4.bin");
			POSModel posModel = new POSModel(posModelIn);
			POSTaggerME tagger = new POSTaggerME(posModel);
			
			// Dictionnary based lemmatizer to correct model errors
			// tarisse -> VERB -> tarir
			
			InputStream dictLemmatizerIn = getClass().getResourceAsStream("/lemmas/fr-lemmatizer.txt");
			DictionaryLemmatizer dictLemmatizer = new DictionaryLemmatizer( dictLemmatizerIn , StandardCharsets.UTF_8 );
			
			// max entropy lemmatizer as a binay model
			InputStream lemmatizerModelIn = getClass().getResourceAsStream("/models/opennlp-fr-ud-gsd-lemmas-1.3-2.5.4.bin");
			LemmatizerModel  lemmatizerModel = new LemmatizerModel(lemmatizerModelIn);
			LemmatizerME modelLemmatizerME = new LemmatizerME(lemmatizerModel);
			
			for (int i = 0; i < sentences.length; i++) {
				System.out.println(" ------------ ");
				System.out.println( sentences[i] );
				
				String[] tokens = tokenizerME.tokenize(sentences[i]);
				String[] posTags = tagger.tag(tokens);
				
				// dictionary lemmatizer
	            String[] dictLemmas = dictLemmatizer.lemmatize(tokens, posTags);
	            // model lemmatizer
	            String[] modelLemmas = modelLemmatizerME.lemmatize(tokens, posTags);
				
				for ( int k = 0 ; k < tokens.length ; k++ ) {
					System.out.println(" =========== ");
					
					System.out.println( tokens[k] );
					System.out.println( posTags[k] );

					if ( dictLemmas[k].equals("O") ) {
						System.out.println( modelLemmas[k] );
					} else {
						System.out.println( dictLemmas[k] );
					}
				}
				// close streams
				tokenizerModelIn.close();
				posModelIn.close();
				dictLemmatizerIn.close();
				lemmatizerModelIn.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
