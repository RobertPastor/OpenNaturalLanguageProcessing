package frenchNER4Entities;

public class OpenNlpNerTagsGenerator {
	
	/**
	 * generates Apache Open NLP V3 NER tags
	 * such as <START:person> <END>
	 * from an integer 
	 * 
	 * entities should be surrounded by tags in format <START:entity-name> Entity <END>. 
	 * Entity name is a label of entities your learning model is going to look for.
	 * add spaces between tags (START/END) and labeled data.
	 */
	
	
	public static String generateStartTag( final Integer tagAsInt ) {
		String startTag = "";
		switch (tagAsInt) {
		case 0:
			startTag = "";
			break;
		case 1:
			startTag = generatePersonStartTag();
			break;
		case 2:
			startTag = generateOrganisationStartTag();
			break;
		case 3:
			startTag = generateLocationStartTag();
			break;
		case 4:
			startTag = generateMiscStartTag();
			break;
		}
		// add space between TAG and token value
		// 18-October-2026 - open NLP 3.00 M6 crash when token with ' (article elidé) before START tag
		return " " + startTag + " ";
	}

	public static String generateEndTag() {
		return "<END> ";
	}
	private static String generateMiscStartTag() {
		return "<START:misc> ";
	}
	private static String generatePersonStartTag() {
		return "<START:person> ";
	}
	private static String generateLocationStartTag() {
		return "<START:location> ";
	}
	private static String generateOrganisationStartTag() {
		return "<START:organization> ";
	}
}
