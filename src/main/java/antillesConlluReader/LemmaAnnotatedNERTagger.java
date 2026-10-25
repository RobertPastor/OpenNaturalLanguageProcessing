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
	
	private boolean tagMustBeAnnotatedAsPerson( final String tag ) {
		return ( tag.equalsIgnoreCase(XFAMIL) );
	}
	
	public String generateAnnotatedTags( ) {
		
		StringBuilder  annotatedSentence = new StringBuilder();
		
		boolean previousTagWasXfamil = false;
		int i = 0;
		while (  i < this.tokens.length ) {
			String token = this.tokens[i];
			String tag = this.tags[i];
			if ( i == 0 ) {
				// first token and first tag
				logger.info( "it is the first token ");
				// XFamily only tag to convert into START:person
				if ( tagMustBeAnnotatedAsPerson ( tag )  ) {
					// current NER Tag value must be annotated
					logger.info( "first token must be annoted ");
					annotatedSentence.append ( generatePersonStartTag() );
					previousTagWasXfamil = true;
					annotatedSentence.append( token );
				} else {
					previousTagWasXfamil = false;
					annotatedSentence.append( token );
				}
			} else {
				logger.info( "it is NOT the first token ");
				if ( tagMustBeAnnotatedAsPerson ( tag ) ) {
					logger.info("current token must be annoted as Start:person");
					if ( previousTagWasXfamil ) {
						// previous token was already annotated
						// append this token in the same previous START Tag
						previousTagWasXfamil = true;
						annotatedSentence.append( token );
						if ( ( i + 1 ) < this.tokens.length ) {
							// there is another token after this one.
							logger.info(" there is another token after this one ");
						} else {
							// it is the last token -> not anyone more after
							// previously token was annotated -> must close the tag
							annotatedSentence.append( generateEndTag() );
						}
					} else {
						// previous token was not annotated
						annotatedSentence.append ( generatePersonStartTag() );
						annotatedSentence.append( token );
						// next time , retain that the previous token was annotated inside the Start tag
						previousTagWasXfamil = true;
					}
				} else {
					// current token must not be annotated (must not be inserted inside a START END)
					// tag different from XFAMILY
					logger.info("current token must NOT be annotated");
					if ( previousTagWasXfamil ) {
						logger.info( "previous token was annotated -> time to append an END tag");
						annotatedSentence.append ( generateEndTag() );
						annotatedSentence.append ( token );
						previousTagWasXfamil = false;
					} else {
						// previous token was NOT annotated - this one too must not be annotated
						annotatedSentence.append ( token );
						previousTagWasXfamil = false;
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
