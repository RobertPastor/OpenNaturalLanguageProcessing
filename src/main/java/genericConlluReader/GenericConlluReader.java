package genericConlluReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.logging.Logger;

import opennlp.tools.formats.conllu.ConlluLemmaSampleStream;
import opennlp.tools.formats.conllu.ConlluSentence;
import opennlp.tools.formats.conllu.ConlluStream;
import opennlp.tools.formats.conllu.ConlluTagset;
import opennlp.tools.lemmatizer.LemmaSample;
import opennlp.tools.ml.maxent.URLInputStreamFactory;
import opennlp.tools.util.ObjectStream;


public class GenericConlluReader {
	
	private static final Logger logger = Logger.getLogger(GenericConlluReader.class.getName());


	Path resourceDirectoryPath = null;
	String inputConnluFileName = "";
	String outputNERtextFileName = "";
	
	// Custom Checked Exception
		class InconsistentInputArraySizeException extends Exception {

			private static final long serialVersionUID = 997119056345909860L;

			public InconsistentInputArraySizeException(String m) {
				super(m);  
			}
		}
		
	
	public GenericConlluReader( final Path resourceDirectoryPath , final String inputConlluFileName ,
			final String outputNERtextFileName) {
		
		this.resourceDirectoryPath = resourceDirectoryPath;
		this.inputConnluFileName = inputConlluFileName;
		this.outputNERtextFileName = outputNERtextFileName;
		
		logger.info(" input Conllu file name -> " + inputConlluFileName);
		logger.info(" output NER text file name -> " + outputNERtextFileName);
		
	}
	
	public void generateNERtextOutputFile () throws IOException, InconsistentInputArraySizeException {
		
		// input file
		Path inputConlluInputFilePath = this.resourceDirectoryPath.resolve( this.inputConnluFileName );
		File inputConlluFile = inputConlluInputFilePath.toFile();
		URL inputConlluFileUrl = inputConlluFile.toURI().toURL();
		logger.info( inputConlluFileUrl.toExternalForm() );
		
		URLInputStreamFactory inputStreamFactory = new URLInputStreamFactory(inputConlluFileUrl);
		ObjectStream<ConlluSentence> conlluSentences = new ConlluStream( inputStreamFactory );
		
		//file output stream
		Path outputNERtextFilePath = this.resourceDirectoryPath.resolve( this.outputNERtextFileName ) ;
		FileOutputStream fos = new FileOutputStream( outputNERtextFilePath.toFile().getAbsolutePath() );
		
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
			logger.info("output NER file correctly generated !!!");
		}
		fos.close();
		logger.info(" count of lines = " + String.valueOf( lineCount ));
		
	}
}
