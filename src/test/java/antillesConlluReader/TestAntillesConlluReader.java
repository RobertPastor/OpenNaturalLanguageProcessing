package antillesConlluReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import opennlp.tools.formats.conllu.ConlluPOSSampleStream;
import opennlp.tools.formats.conllu.ConlluSentence;
import opennlp.tools.formats.conllu.ConlluStream;
import opennlp.tools.formats.conllu.ConlluTagset;
import opennlp.tools.formats.conllu.ConlluTokenSampleStream;
import opennlp.tools.ml.maxent.URLInputStreamFactory;
import opennlp.tools.postag.POSSample;
import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;

public class TestAntillesConlluReader {

	private static final Logger logger = Logger.getLogger(TestAntillesConlluReader.class.getName());

	@Test
	public void testReadAntillesConlluFormat () throws Exception {

		logger.info("--- Reading an Antilles Conllu formatted text file  ---");
		
		try {
			
			String antillesConlluInputFileName = "test.conllu";
			Path resourceDirectory = Paths.get("src","test","resources","Antilles-ConLLu");
			
			Path antillesConlluInputFilePath = resourceDirectory.resolve(antillesConlluInputFileName);
			File inputTrainingFile = antillesConlluInputFilePath.toFile();
			URL trainingFileUrl = inputTrainingFile.toURI().toURL();
			logger.info( trainingFileUrl.toExternalForm() );
			
			URLInputStreamFactory inputStreamFactory = new URLInputStreamFactory(trainingFileUrl);
			
			ObjectStream<ConlluSentence> conlluSentencesPOSSample = new ConlluStream( inputStreamFactory );
			ConlluPOSSampleStream conlluPOSSampleStream = new ConlluPOSSampleStream( conlluSentencesPOSSample , ConlluTagset.U);
			
			ObjectStream<ConlluSentence> conlluSentencesTokenSample = new ConlluStream( inputStreamFactory );
			ConlluTokenSampleStream conlluTokenSampleStream = new ConlluTokenSampleStream( conlluSentencesTokenSample );
			
			// each read retrieves a sentence
			POSSample posSample = conlluPOSSampleStream.read();
			TokenSample tokenSample = conlluTokenSampleStream.read();
			
			while ( ( posSample != null ) && (  tokenSample != null ) ) {
				logger.info(" ------------POS sample--------------- ");
				logger.info( Arrays.asList ( posSample.getSentence() ).toString() );
				String[] tags = posSample.getTags();
				logger.info(" ------ tags size = " + String.valueOf ( tags.length ));
				String sentence = tokenSample.getText();
				Span[] spans = tokenSample.getTokenSpans();
				logger.info(" ------ spans size = " + String.valueOf ( spans.length ));
				
				List<String> tagsAsList = Arrays.asList( tags );
				Iterator<String> iterTags = tagsAsList.iterator();
				
				List<Span> spansAsList = Arrays.asList( spans );
				Iterator<Span> iterSpans = spansAsList.iterator();
				
				while ( iterTags.hasNext() && iterSpans.hasNext() ) {
					logger.info(" ------- tags and spans --------- ");
					String tag = iterTags.next();
					logger.info( "Tag -> " + tag );
					Span span = iterSpans.next();
					logger.info( "Start -> " +  String.valueOf ( span.getStart() )  + " End -> " + String.valueOf(span.getEnd()) );
					logger.info( "Token -> " + sentence.substring(span.getStart(), span.getEnd()) );
				}

				posSample = conlluPOSSampleStream.read();
				tokenSample = conlluTokenSampleStream.read();
			}
			
			//tokenSample = null;
			//while ( ) {
			//	logger.info(" ------------token sample--------------- ");
			//	logger.info( tokenSample.getText() );
			//	String sentence = tokenSample.getText();
			//	Span[] spans = tokenSample.getTokenSpans();
			//	logger.info(" ------ token size = " + String.valueOf ( spans.length ));
			//}
			
			conlluPOSSampleStream.close();
			conlluTokenSampleStream.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
