package frenchNER4Entities;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import frenchNER4Entities.AnnotatedTaggedNerEntities.InconsistentInputArraySizeException;

public class TestConvertNerToOpenNlp {

	private static final Logger logger = Logger.getLogger(TestConvertNerToOpenNlp.class.getName());

	@Test
	public void addNlpNerTagsToFrenchNer4Entities() throws IOException {

		logger.info("--- add NLP NER tags to CATIE AQ NER sentences  - start ---");

		String[] inputSentenceTokensOne = { "L'", "Humanité", "parait" , "clandestinement", "pendant", "cinq", "ans", "(", 
				"383", "numéros", "diffusés", "à", "200000", "exemplaires", ")", 
				"jouant", "un", "rôle", "important", "dans", "la", "Résistance", "." };

		// 1 person, 2 organization  , 3 location ,  4 misc
		Integer[] inputSentenceNerTagsOne = {2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0};

		AnnotatedTaggedNerEntities annotatedTaggedNerEntities = new AnnotatedTaggedNerEntities();

		String[] annotatedSentence;
		try {
			annotatedSentence = annotatedTaggedNerEntities.annotateOpenNlpSentence(inputSentenceTokensOne , inputSentenceNerTagsOne );
			logger.info( Arrays.toString(annotatedSentence) );
			
			logger.info( " ==================================== ");

		} catch (InconsistentInputArraySizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] inputSentenceTokensTwo = {
				"Ses",				"velléités",				"de",				"liberté",  " " ,
				"le",				"conduiront",				"à",				"s'",
				"affranchir",				"des",				"textes",				"qu'",
				"il",				"met",				"en",				"musique",				"au",
				"point",				"de",				"les",				"écrire",				"lui-même",
				"comme",				"le",				"fit",				"aussi",				
				"Richard",				"Wagner",				"."		};
		// NER tag = 1 -> person
		Integer[] inputSentenceNerTagsTwo = {
				0,				0,				0,				0, 				4 ,
				0,				0,				0,				0,
				0,				0,				0,				0,
				0,				0,				0,				0,				0,
				0,				0,				0,				0,				0,
				0,				0,				0,				0,
				1,				1,				0};

		try {
			annotatedSentence = annotatedTaggedNerEntities.annotateOpenNlpSentence(inputSentenceTokensTwo , inputSentenceNerTagsTwo );
			logger.info( Arrays.toString(annotatedSentence) );
			logger.info( " ==================================== ");


		} catch (InconsistentInputArraySizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] inputSentenceTokensThree = {    "Sept",		                                    "des",
		                                    "huit",		                                    "agences",
		                                    "de",		                                    "communication",
		                                    "les",		                                    "plus",
		                                    "importantes",		                                    "du",
		                                    "monde",		                                    "ont",
		                                    "pour",		                                    "siège",
		                                    "Manhattan",		                                    "."};
		Integer[] inputSentenceNerTagsThree = { 0,		                                        0,
		                                        0,		                                        0,
		                                        0,		                                        0,
		                                        0,		                                        0,
		                                        0,		                                        0,
		                                        0,		                                        0,
		                                        0,		                                        0,
		                                        3,		                                        0};
		                                     
		try {
			annotatedSentence = annotatedTaggedNerEntities.annotateOpenNlpSentence(inputSentenceTokensThree , inputSentenceNerTagsThree );
			logger.info( Arrays.toString(annotatedSentence) );

		} catch (InconsistentInputArraySizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("--- add NLP NER tags to CATIE AQ NER sentences  - end ---");
	}
}
