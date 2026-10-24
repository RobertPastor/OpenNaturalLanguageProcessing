package docxToTextConverter;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class DocxToTextConverter {

	private static final Logger logger = Logger.getLogger(DocxToTextConverter.class.getName());

	public String convertDocxToText( final String wordInputFilePath) throws IOException {

		logger.info("--- word to text converter ---");
		//String wordInputfilePath = "/inputs/Le village englouti.docx";

		try (InputStream is = getClass().getResourceAsStream( wordInputFilePath );
				XWPFDocument document = new XWPFDocument(is)) {
			StringBuilder text = new StringBuilder();
			// Iterate through all paragraphs in the document
			for (XWPFParagraph paragraph : document.getParagraphs()) {
				String subText = paragraph.getText().replaceAll("\"", "").replaceAll("”", "").replaceAll("“" , "").trim();
				text.append( subText ).append("\n");
			}
			//System.out.println( text.toString() );
			return text.toString();
		}
	}
}
