package sentenceDetection;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import docxToTextConverter.DocxToTextConverter;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.util.logging.Logger;

public class TestSentenceBoundaryDisambiguation {

	private static final Logger logger = Logger.getLogger(TestSentenceBoundaryDisambiguation.class.getName());

	@Test
	public void givenFrenchModel_whenDetect_thenSentencesAreDetected() throws Exception {

		logger.info("--- Running a sentence detector tool ---");

		InputStream sentenceInputStream = getClass().getResourceAsStream("/models/opennlp-fr-ud-gsd-sentence-1.3-2.5.4.bin");
		SentenceModel model = new SentenceModel(sentenceInputStream);

		SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

		String wordInputfilePath = "/inputs/Le village englouti.docx";
		try {
			String extractedText = new DocxToTextConverter().convertDocxToText(wordInputfilePath);
			String sentences[] = sentenceDetector.sentDetect(extractedText);

			for (int i = 0; i < sentences.length; i++) {
				//System.out.println( sentences[i].replaceAll("”", "").replaceAll("“" , "") );
				System.out.println( sentences[i].trim() );
			}
			//String result = sb.toString();
			//System.out.println(result);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
