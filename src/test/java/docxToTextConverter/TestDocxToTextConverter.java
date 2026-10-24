package docxToTextConverter;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.jupiter.api.Test;


public class TestDocxToTextConverter {
	
	private static final Logger logger = Logger.getLogger(TestDocxToTextConverter.class.getName());

	@Test
	public void convertDocxToText() throws IOException {
		
		logger.info("--- word to text converter - start ---");
		String wordInputfilePath = "/inputs/Le village englouti.docx";
						
        try (InputStream is = getClass().getResourceAsStream(wordInputfilePath);
            XWPFDocument document = new XWPFDocument(is)) {
            StringBuilder text = new StringBuilder();
            // Iterate through all paragraphs in the document
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                text.append(paragraph.getText()).append("\n");
            }
            //System.out.println( text.toString() );
            assert ( text.toString().length() > 0 );
        }
		logger.info("--- word to text converter - end ---");
    }

}
