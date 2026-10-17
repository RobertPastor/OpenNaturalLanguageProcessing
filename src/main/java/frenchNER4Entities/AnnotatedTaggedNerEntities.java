package frenchNER4Entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class AnnotatedTaggedNerEntities {
	
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
					// clean the annotated token that is suppose to fill with the START END tacgs
					annotatedToken = "";
					i = i + 1;
				} else {
					// current NER Tag value different from 0
					logger.info( " first token must be annoted ");
					annotatedToken = OpenNlpNerTagsGenerator.generateStartTag(NerTagAsInt);
					if ( sentenceToken.endsWith("'") == false ) {
						annotatedToken += sentenceToken + " ";
					} else {
						annotatedToken += sentenceToken;
					}
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
						if ( sentenceToken.endsWith("'") == false ) {
							annotatedToken += sentenceToken + " ";
						} else {
							annotatedToken += sentenceToken;
						}
						// insertion will occur later in the next loop step
						previousNerTagValue = sentenceNerTags[i];
						i = i + 1;
						
					} else {
						logger.info( "  previous NER tag value different of 0 ");
						// current NER Tag value = 0
						annotatedToken += OpenNlpNerTagsGenerator.generateEndTag();
						annotatedToken += " ";
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
								if ( sentenceToken.endsWith("'") == false ) {
									annotatedToken += sentenceToken + " ";
								} else {
									annotatedToken += sentenceToken;
								}
								// insertion will occur later in the next loop step
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
								annotatedToken += sentenceToken + " ";
								annotatedToken += OpenNlpNerTagsGenerator.generateEndTag();
								logger.info( annotatedToken );
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
