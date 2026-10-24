package namedEntityRecognition;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.Map.Entry;

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
	
	 /**
     * Sorts a Map<String, Integer> by its values in ascending order.
     * Returns a LinkedHashMap preserving the sorted order.
     */
    public static Map<String, Integer> sortByValue(Map<String, Integer> map) {
        // Convert map entries to a list
        List<Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

        // Sort list using a comparator for values
        list.sort(Entry.comparingByValue());

        // Create a LinkedHashMap to preserve insertion order
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    

	@Test
	public void givenFrenchModel_whenDetectTokens_thenNER() throws Exception {

		logger.info("--- Running a Named Entity Recognition tool ---");
		
		try {
			Map<String, Integer> personCounterMap = new HashMap<String, Integer>(); 
			
			// test resources path to the docx input file
			String wordInputfilePath = "/inputs/Le village englouti.docx";
			wordInputfilePath = "/inputs/Flaubert-Bovary.docx";
			logger.info( " ----- document name = " + wordInputfilePath + " ---- ");

			// sentence detector
			InputStream sentenceInputStream = getClass().getResourceAsStream("/models/opennlp-fr-ud-gsd-sentence-1.3-2.5.4.bin");
			SentenceModel sentenceModel = new SentenceModel(sentenceInputStream);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
			
			// read input word file
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
					String token = tokens[nameSpans[j].getStart()];
					logger.info( "token -> " +  token );
					String type =  nameSpans[j].getType();
					logger.info( "type -> " + type );
					if ( type.equalsIgnoreCase("person") ) {
						personCounterMap.compute(token, (k, v) -> v == null ? 1 : v + 1); 
					}
				}
			}
			nameFinderME.clearAdaptiveData();
			
			// close streams
			sentenceInputStream.close();
			tokenizerModelIn.close();
			nameEntityRecogIn.close();
			
			// sort the map according to count of occurrences
			Map<String, Integer> sortedPersonCounterMap = sortByValue(personCounterMap);
			// show occurrences
			sortedPersonCounterMap.forEach ( (token , count) -> {
				logger.info( "token -> " + token + " -> count -> " + String.valueOf(count));
			});
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
