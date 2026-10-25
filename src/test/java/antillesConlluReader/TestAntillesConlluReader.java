package antillesConlluReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import opennlp.tools.formats.conllu.ConlluLemmaSampleStream;
import opennlp.tools.formats.conllu.ConlluSentence;
import opennlp.tools.formats.conllu.ConlluStream;
import opennlp.tools.formats.conllu.ConlluTagset;
import opennlp.tools.lemmatizer.LemmaSample;
import opennlp.tools.ml.maxent.URLInputStreamFactory;
import opennlp.tools.util.ObjectStream;

public class TestAntillesConlluReader {

	private static final Logger logger = Logger.getLogger(TestAntillesConlluReader.class.getName());
	
	// Custom Checked Exception
	class InconsistentInputArraySizeException extends Exception {

		private static final long serialVersionUID = 997119056345909860L;

		public InconsistentInputArraySizeException(String m) {
			super(m);  
		}
	}

	@Test
	public void testReadAntillesConlluFormat () throws Exception {

		logger.info("--- Reading an Antilles Conllu formatted text file  ---");
		
		try {
			
			Path resourceDirectory = Paths.get("src","test","resources","Antilles-ConLLu");
			String prefix = "train";
			
			String outputNerFileName = prefix + "-Antilles-Conllu-NER-input.txt";
			Path frenchNERoutputFilePath = resourceDirectory.resolve(outputNerFileName);
			FileOutputStream fos = new FileOutputStream( frenchNERoutputFilePath.toFile().getAbsolutePath() );
			
			String antillesConlluInputFileName = prefix + ".conllu";
			
			Path antillesConlluInputFilePath = resourceDirectory.resolve(antillesConlluInputFileName);
			File inputTrainingFile = antillesConlluInputFilePath.toFile();
			URL trainingFileUrl = inputTrainingFile.toURI().toURL();
			logger.info( trainingFileUrl.toExternalForm() );
			
			URLInputStreamFactory inputStreamFactory = new URLInputStreamFactory(trainingFileUrl);
			ObjectStream<ConlluSentence> conlluSentences = new ConlluStream( inputStreamFactory );
			
			int lineCount = 0;
			try (ConlluLemmaSampleStream conlluWordLineStream = new ConlluLemmaSampleStream(conlluSentences, ConlluTagset.U)) {
				LemmaSample lemmaSample = conlluWordLineStream.read();
				while ( lemmaSample != null ) {
					lineCount = lineCount + 1;
					logger.info( " ------------------------- ");
					logger.info( lemmaSample.toString() );
					String[] tokens = lemmaSample.getTokens();
					logger.info(" ------ tokens size = " + String.valueOf ( tokens.length ));

					String[] lemmas = lemmaSample.getLemmas();
					logger.info(" ------ lemmas size = " + String.valueOf ( lemmas.length ));

					String[] tags  = lemmaSample.getTags();
					logger.info(" ------ tags size = " + String.valueOf ( tags.length ));
					
					if ( ( tokens.length !=  lemmas.length ) || 
							( tokens.length != tags.length ) || 
							( lemmas.length != tags.length ) ) {
							throw new InconsistentInputArraySizeException("Size of tokens, lemmas, and tags must be identical.");
					}
					for ( int i = 0 ; i < tokens.length ; i++ ) {
						logger.info( "token -> " + tokens[i] + " -> lemma -> " + lemmas[i] + " -> tag -> " + tags[i] );
					}
					LemmaAnnotatedNERTagger lemmaAnnotatedNERTagger = new LemmaAnnotatedNERTagger(lemmaSample);
					String annotatedNERtaggedSentence = lemmaAnnotatedNERTagger.generateAnnotatedTags();

					fos.write(annotatedNERtaggedSentence.getBytes());
					fos.flush();
					lemmaSample = conlluWordLineStream.read();
				}
				conlluWordLineStream.close();
			}
			fos.close();
			logger.info(" count of lines = " + String.valueOf( lineCount ));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
