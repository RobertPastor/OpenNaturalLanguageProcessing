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
				// first token and first tag
				logger.info( "it is the first token ");
				previousTagWasPropn = tagIsOfTypePropn(tag);
				previousTagWasXfamil = tagIsOfTypeXFamil ( tag );
				// XFamily only tag to convert into START:person OR 2 consecutives PROPN
				if ( tagIsOfTypeXFamil ( tag )  ) {
					// current NER Tag value must be annotated
					logger.info( "first token must be annoted ");
					annotatedSentence.append ( generatePersonStartTag() );
					annotatedSentence.append( token );
					startTagHasBeenInserted = true;
				} else {
					// if current tag is PROPN AND there is a another one AND next one is PROPN or XFAMIL 
					// then insert a START:person tag
					if ( tagIsOfTypePropn(tag) && ( (i+1) < this.tokens.length ) && 
							( tagIsOfTypePropn( this.tags[i+1] ) ||  tagIsOfTypeXFamil ( this.tags[i+1] ) ) ) {
						annotatedSentence.append ( generatePersonStartTag() );
						// two consecutive PROPN is like a XFamil
						annotatedSentence.append( token );
						startTagHasBeenInserted = true;
					} else {
						// do not insert Start Tag
						annotatedSentence.append( token );
						startTagHasBeenInserted = false;
					}
				}
			} else {
				// it is NOT the first token
				logger.info( "it is NOT the first token ");
				if ( startTagHasBeenInserted == true ) {
					logger.info( "a start tag has been already inserted ");				
					if (  tagIsOfTypePropn(tag) || tagIsOfTypeXFamil ( tag ) ) {
						// continue inserting TAG after the START
						annotatedSentence.append( token );
						startTagHasBeenInserted = true;
						
					} else {
						// it is the end of annotated tag
						// insert END tag followed by the token
						annotatedSentence.append ( generateEndTag() );
						annotatedSentence.append ( token );
						
						startTagHasBeenInserted = false;
						previousTagWasPropn = tagIsOfTypePropn(tag);
						previousTagWasXfamil = tagIsOfTypeXFamil ( tag );
					}
					
				} else {
					logger.info("there is no ongoing START tag around ");
					// the current stream is not inside a START tag
					// consider inserting a START tag
					if ( tagIsOfTypeXFamil ( tag )  ) {
						// current NER Tag value must be annotated
						logger.info( "first token must be annoted ");
						annotatedSentence.append ( generatePersonStartTag() );
						annotatedSentence.append( token );
						startTagHasBeenInserted = true;
						previousTagWasPropn = tagIsOfTypePropn(tag);
						previousTagWasXfamil = tagIsOfTypeXFamil ( tag );
					
					} else {
						
						// if current tag is PROPN AND there is a another token after this one AND next one is PROPN or XFAMIL 
						// then insert a START:person tag
						if ( tagIsOfTypePropn(tag) && ( (i+1) < this.tokens.length ) && 
								( tagIsOfTypePropn( this.tags[i+1] ) ||  tagIsOfTypeXFamil ( this.tags[i+1] ) ) ) {
							annotatedSentence.append ( generatePersonStartTag() );
							// two consecutive PROPN is like a XFamil
							annotatedSentence.append( token );
							startTagHasBeenInserted = true;
							
							previousTagWasPropn = tagIsOfTypePropn(tag);
							previousTagWasXfamil = tagIsOfTypeXFamil ( tag );
							
							if ( ( i + 1 ) >= this.tokens.length ) {
								// it is the last token
								logger.info( " it is the last token -> insert END tag ");
								annotatedSentence.append( generateEndTag() );
								
								startTagHasBeenInserted = false;
								
							}
							
						} else {
							// do not insert Start Tag
							annotatedSentence.append( token );
							startTagHasBeenInserted = false;
							
							previousTagWasPropn = tagIsOfTypePropn(tag);
							previousTagWasXfamil = tagIsOfTypeXFamil ( tag );
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
