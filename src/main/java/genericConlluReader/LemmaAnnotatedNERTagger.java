package genericConlluReader;

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
	
	private boolean tagIsOfTypeXFamil( final String tag ) {
		return ( tag.equalsIgnoreCase(XFAMIL) );
	}
	
	private boolean tagIsOfTypePropn( final String tag ) {
		return ( tag.equalsIgnoreCase(PROPN));
	}
	
	public String generateAnnotatedTags( ) {
		
		StringBuilder  annotatedSentence = new StringBuilder();
		
		boolean previousTagWasXfamil = false;
		// if 2 PROPN are successive then it is the same as a XFamily
		boolean previousTagWasPropn = false;
		boolean startTagHasBeenInserted = false;
		int i = 0;
		while (  i < this.tokens.length ) {
			String token = this.tokens[i];
			String tag = this.tags[i];
			if ( i == 0 ) {
				previousTagWasPropn = tagIsOfTypePropn(tag);
				// first token and first tag
				logger.info( "it is the first token ");
				// XFamily only tag to convert into START:person
				if ( tagIsOfTypeXFamil ( tag )  ) {
					// current NER Tag value must be annotated
					logger.info( "first token must be annoted ");
					annotatedSentence.append ( generatePersonStartTag() );
					previousTagWasXfamil = true;
					annotatedSentence.append( token );
					startTagHasBeenInserted = true;
				} else {
					
					// if current tag is PROPN AND there is a next one AND next one also then insert a START:person tag
					if ( tagIsOfTypePropn(tag) && ( (i+1) < this.tokens.length ) && tagIsOfTypePropn( this.tags[i+1]) ) {
						annotatedSentence.append ( generatePersonStartTag() );
						// two consecutive PROPN is like a XFamil
						previousTagWasXfamil = true;
						annotatedSentence.append( token );
						startTagHasBeenInserted = true;
					} else {
						previousTagWasXfamil = false;
						annotatedSentence.append( token );
						startTagHasBeenInserted = false;

					}
				}
			} else {
				logger.info( "it is NOT the first token ");
				if ( tagIsOfTypeXFamil ( tag ) ) {
					logger.info("current tag is of type XFamil -> token must be annoted as Start:person");
					if ( previousTagWasXfamil ) {
						// previous token was already annotated
						// append this token in the same previous START Tag
						previousTagWasXfamil = true;
						annotatedSentence.append( token );
						if ( ( i + 1 ) < this.tokens.length ) {
							// there is another token after this one.
							logger.info(" there is another token after this one ");
							previousTagWasPropn = tagIsOfTypePropn(tag);
						} else {
							// it is the last token -> not anyone more after
							// previously token was annotated -> must close the tag
							annotatedSentence.append( generateEndTag() );
							previousTagWasPropn = tagIsOfTypePropn(tag);
							startTagHasBeenInserted = false;
						}
					} else {
						// previous token was not annotated
						annotatedSentence.append ( generatePersonStartTag() );
						annotatedSentence.append( token );
						// next time , retain that the previous token was annotated inside the Start tag
						previousTagWasXfamil = true;
						previousTagWasPropn = tagIsOfTypePropn(tag);
						startTagHasBeenInserted = true;

					}
				} else {
					// tag different from XFamil
					// current token must not be annotated (must not be inserted inside a START END)
					// tag different from XFAMILY
					logger.info("current token must NOT be annotated except if previous one was PROPN and this one also");
					if ( previousTagWasXfamil == true ) {
						logger.info( "previous token was annotated AND current is XFamily -> time to append an END tag");
						// assumption : XFamil is the last after several PROPN
						annotatedSentence.append ( generateEndTag() );
						annotatedSentence.append ( token );
						previousTagWasXfamil = false;
						startTagHasBeenInserted = false;

					} else {
						// previous tag was not of type XFmai
						if ( previousTagWasPropn ) {
							// previous tag was of type PROPN and current also -> the START: person already inserted
							if ( tagIsOfTypePropn(tag) ) {
								// current tag is of type PROPN
								annotatedSentence.append ( token );
								previousTagWasXfamil = false;
								previousTagWasPropn = tagIsOfTypePropn(tag);
								startTagHasBeenInserted = true;
							} else {
								// previous was PROPN and current is not PROPN
								if ( startTagHasBeenInserted ) {
									annotatedSentence.append ( generateEndTag() );
									annotatedSentence.append ( token );
									previousTagWasXfamil = false;
									startTagHasBeenInserted = false;
								}
							}
						} else {
							// previous token was NOT annotated - this one too must not be annotated
							annotatedSentence.append ( token );
							previousTagWasXfamil = false;
						}
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
