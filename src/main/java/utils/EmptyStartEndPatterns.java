package utils;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmptyStartEndPatterns {

	private static final Logger logger = Logger.getLogger(EmptyStartEndPatterns.class.getName());

	/**
	 * find a START END pattern with an attribute and an empty content
	 * empty content means containing only Spaces
	 * @param inputToken
	 * @return
	 */
	public boolean findEmptyStartEndPattern( final String inputToken ) {
		
		logger.info( inputToken );
		String regexp = "(<START.*?>)(\s*)(<END>)";
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(inputToken);
	    boolean matchFound = matcher.find();
	    if(matchFound) {
	      logger.info("Match found");
	    } else {
	      logger.info("Match not found");
	    }
		return matchFound;
	}
}
