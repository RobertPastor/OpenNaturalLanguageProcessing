package antillesConlluReader;

import java.util.logging.Logger;

import opennlp.tools.lemmatizer.LemmaSample;

public class LemmaAnnotatedNERTagger {
	
	private static final Logger logger = Logger.getLogger(LemmaAnnotatedNERTagger.class.getName());
	
	String PROPN = "PROPN";
	String XFAMIL = "XFAMIL";
	
	public final static char CR = (char) 0x0D;
	public final static char LF = (char) 0x0A;
	
	LemmaSample lemmaSample = null;
	String[] tokens = null;
	String[] tags = null;
	
	public LemmaAnnotatedNERTagger ( final LemmaSample lemmaSample ) {
		this.lemmaSample = lemmaSample;
		this.tokens = lemmaSample.getTokens();
		this.tags = lemmaSample.getTags();
	}

	
	private static String generatePersonStartTag() {
		return " <START:person> ";
	}
	
	private static String generateEndTag() {
		return " <END> ";
	}
	
	public String generateAnnotatedTags( ) {
		
		StringBuilder  annotatedSentence = new StringBuilder();
		
		boolean previousTagWasPropnOrXfamil = false;
		int i = 0;
		while (  i < this.tokens.length ) {
			String token = this.tokens[i];
			String tag = this.tags[i];
			if ( i == 0 ) {
				// first token and first tag
				logger.info( "it is the first token ");
				if ( tag.equalsIgnoreCase(PROPN) || tag.equalsIgnoreCase(XFAMIL) ) {
					// current NER Tag value must be annotated
					logger.info( "first token must be annoted ");
					annotatedSentence.append ( generatePersonStartTag() );
					previousTagWasPropnOrXfamil = true;
					annotatedSentence.append( token );
				} else {
					previousTagWasPropnOrXfamil = false;
					annotatedSentence.append( token );
				}
			} else {
				logger.info( "it is NOT the first token ");
				if ( tag.equalsIgnoreCase(PROPN) || tag.equalsIgnoreCase(XFAMIL) ) {
					logger.info("token must be annoted ");
					if ( previousTagWasPropnOrXfamil ) {
						// previous token was already annotated
						previousTagWasPropnOrXfamil = true;
						annotatedSentence.append( token );
					} else {
						// previous token was not annotated
						annotatedSentence.append ( generatePersonStartTag() );
						previousTagWasPropnOrXfamil = true;
						annotatedSentence.append( token );
					}
					
				} else {
					// current token must not be annotated (must not be inserted in START END)
					if ( previousTagWasPropnOrXfamil ) {
						// previous token was annotated -> time to append an END tag
						annotatedSentence.append ( generateEndTag() );
						annotatedSentence.append ( token );
						previousTagWasPropnOrXfamil = false;
						
					} else {
						annotatedSentence.append ( token );
						previousTagWasPropnOrXfamil = false;
					}
				}
			}
			annotatedSentence.append(" ");
			logger.info( annotatedSentence.toString() );
			i = i + 1;
		}
		annotatedSentence.append(CR);
		annotatedSentence.append(LF);
		return annotatedSentence.toString();
	}

}
