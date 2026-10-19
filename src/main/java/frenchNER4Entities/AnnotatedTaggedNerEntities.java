package frenchNER4Entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnotatedTaggedNerEntities {
	
	/**
	 * Purpose : transform the String[] array containing the tokens
	 * and the Integer[] array containing the Tag integer values
	 * integer tag values are defined by the CATIE AQ french NER project
	 * into a String[] array of annotated tokens
	 */
	
	private static final Logger logger = Logger.getLogger(AnnotatedTaggedNerEntities.class.getName());

	
	// Custom Checked Exception
	class InconsistentInputArraySizeException extends Exception {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1581659561710415443L;

		public InconsistentInputArraySizeException(String m) {
	        super(m);  
	    }
	}
	
	private static String cleanAnnotatedEmptyStartEndToken( final String inputSentenceToken ) {
		
		logger.info( inputSentenceToken );
		String regexp = "(<START.*?>)(\s*)(<END>)";
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(inputSentenceToken);
	    boolean matchFound = matcher.find();
	    if(matchFound) {
	      logger.info("Empty token found between START and END -> Match found");
	      return " ";
	    } else {
	      logger.info("token not empty between START and END -> Match not found");
	      return inputSentenceToken;
	    }
	}
	
	/**
	 * correct sentenceToken after <START> to avoid model training crashes in Open NLP 3.0.0.M6
	 * @param inputSentenceToken
	 * @return
	 */
	private String correctSentenceTokenAfterStart( final String inputSentenceToken ) {
		
		String correctedInputSentenceToken = inputSentenceToken;
		if ( correctedInputSentenceToken.endsWith("'") == false ) {
			correctedInputSentenceToken = correctedInputSentenceToken + " ";
		} else {
			correctedInputSentenceToken = correctedInputSentenceToken + " ";
		}
		if ( correctedInputSentenceToken.startsWith(",") ) {
			correctedInputSentenceToken = correctedInputSentenceToken.replace(",", " ");
		}
		if ( correctedInputSentenceToken.startsWith("\"") ) {
			// suppress double quotes
			correctedInputSentenceToken = correctedInputSentenceToken.replace("\"", " ");
		}
		return correctedInputSentenceToken;
	}
	
	/**
	 * retrieve an annotated (with START and END tags including person, location, organization and misc
	 * @param sentenceTokens
	 * @param sentenceNerTags
	 * @return
	 * @throws InconsistentInputArraySizeException
	 */
	public String[] annotateOpenNlpSentence ( final String[] sentenceTokens , final Integer[] sentenceNerTags ) 
			throws InconsistentInputArraySizeException {
		
		logger.info( Arrays.toString(sentenceTokens) );

		//String[] annotedSentenceTokens = null;
		List<String> annotatedSentenceTokensList = new ArrayList<String>();
		
		if ( sentenceTokens.length != sentenceNerTags.length ) {
			throw new InconsistentInputArraySizeException("Size of sentence tokens must match Size of NER tags.");
		}
		int i = 0;
		
		int previousNerTagValue = 0;
		// annotated token fills up with START and END 
		// or annotated token contains only the source token
		String annotatedToken = "";
		while ( i < sentenceTokens.length ) {
			logger.info( annotatedSentenceTokensList.toString() );
			String sentenceToken = sentenceTokens[i];
			int NerTagAsInt = sentenceNerTags[i];
			// 
			if ( i == 0 ) {
				// first token and first tag
				logger.info( " it is the first token ");
				if ( NerTagAsInt == 0 ) {
					logger.info( " it is a normal token -> add it to the list ");
					annotatedSentenceTokensList.add( sentenceToken );
					previousNerTagValue = sentenceNerTags[i];
					// clean the annotated token that is suppose to fill with the START END tags
					annotatedToken = "";
					i = i + 1;
				} else {
					// current NER Tag value different from 0
					logger.info( " first token must be annoted ");
					annotatedToken = OpenNlpNerTagsGenerator.generateStartTag(NerTagAsInt);
					annotatedToken += this.correctSentenceTokenAfterStart(sentenceToken);
					// insertion will occur later in the next loop step
					previousNerTagValue = sentenceNerTags[i];
					i = i + 1;
				}
			} else {
				// it is not the first token
				logger.info( " it is not the first token -> " + sentenceToken );
				if ( previousNerTagValue == NerTagAsInt ) {
					logger.info( " previous NER Tag value and current NER Tag value are identical ");
					if ( NerTagAsInt == 0 ) {
						logger.info( " insert the current token without any tag ");
						annotatedSentenceTokensList.add( sentenceToken );
						annotatedToken = "";
						previousNerTagValue = sentenceNerTags[i];
						i = i + 1;
					} else {
						logger.info(" previous token had a TAG continue adding to the compound annotated token " );
						// NER tag value = 0
						// add the current token
						annotatedToken += sentenceToken;
						annotatedToken += " ";
						logger.info( annotatedToken );
						previousNerTagValue = sentenceNerTags[i];
						i = i + 1;
					}
				} else {
					logger.info( " previous NER Tag value and current NER Tag value are NOT identical ");
					if ( previousNerTagValue == 0) {
						logger.info( " previous NER Tag value was 0 -> no need for an END Tag ");
						annotatedToken = OpenNlpNerTagsGenerator.generateStartTag(NerTagAsInt);
						annotatedToken += this.correctSentenceTokenAfterStart(sentenceToken);
						// insertion will occur later in the next loop step
						previousNerTagValue = sentenceNerTags[i];
						i = i + 1;
					} else {
						logger.info( "  previous NER tag value different of 0 ");
						// current NER Tag value = 0
						annotatedToken += " ";
						annotatedToken += OpenNlpNerTagsGenerator.generateEndTag();
						annotatedToken = AnnotatedTaggedNerEntities.cleanAnnotatedEmptyStartEndToken(annotatedToken);
						logger.info( annotatedToken );
						// insert the previous token ended by END tag
						annotatedSentenceTokensList.add( annotatedToken );
						
						if ( i < sentenceTokens.length ) {
							logger.info( " more tokens to come ");
							// clean resulting annotated token
							annotatedToken = "";
							if ( NerTagAsInt == 0 ) {
								logger.info( " current token does not need a Start Tag ");
								annotatedSentenceTokensList.add( sentenceToken );
								annotatedToken = "";
								previousNerTagValue = sentenceNerTags[i];
								i = i + 1;
							} else {
								annotatedToken = OpenNlpNerTagsGenerator.generateStartTag(NerTagAsInt);
								annotatedToken += this.correctSentenceTokenAfterStart(sentenceToken);
								// insertion will occur later in the next loop step
								// do not clean the annotated token
								previousNerTagValue = sentenceNerTags[i];
								i = i + 1 ;
							}
						} else {
							logger.info(" it is the last token ");
							if ( NerTagAsInt == 0 ) {
								logger.info( " last token without a START and END tag ");
								annotatedSentenceTokensList.add( sentenceToken );
								i = i + 1;
							} else {
								// should not arrive here as last token is mainly a Punctuation
								logger.info( " last token with a START and END tag ");
								annotatedToken = OpenNlpNerTagsGenerator.generateStartTag(NerTagAsInt);
								annotatedToken += this.correctSentenceTokenAfterStart(sentenceToken);
								annotatedToken += OpenNlpNerTagsGenerator.generateEndTag();
								annotatedToken = AnnotatedTaggedNerEntities.cleanAnnotatedEmptyStartEndToken(annotatedToken);
								logger.info( annotatedToken );
								annotatedSentenceTokensList.add( annotatedToken );
								i = i + 1;
							}
						}
					}
				}
			}
		}
		return  annotatedSentenceTokensList.toArray( new String[annotatedSentenceTokensList.size()] );
	}
}
