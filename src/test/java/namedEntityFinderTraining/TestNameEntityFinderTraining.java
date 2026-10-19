package namedEntityFinderTraining;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import opennlp.tools.ml.maxent.URLInputStreamFactory;
import opennlp.tools.namefind.BioCodec;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class TestNameEntityFinderTraining {

	private static final Logger logger = Logger.getLogger(TestNameEntityFinderTraining.class.getName());
	
	@Test
	public void generateFrenchNerPersonModel () throws IOException {
		
		logger.info("--- French NER 4 Person Model generator  - start ---");
		
		try {
			TokenNameFinderFactory tokenNameFinderFactory = TokenNameFinderFactory.create(null, null, 
					Collections.emptyMap(), 
					new BioCodec());
			// input training file
			String frenchNERinputFileName = "train-NER4Entities-input.txt";
			Path resourceDirectory = Paths.get("src","test","resources","Catie-AQ-FR-NER-4Entities");
			Path frenchNERinputFilePath = resourceDirectory.resolve(frenchNERinputFileName);
			
			File trainingFile = frenchNERinputFilePath.toFile();
			URL trainingFileUrl = trainingFile.toURI().toURL();
			logger.info( trainingFileUrl.toExternalForm() );
			
			URLInputStreamFactory inputStreamFactory = new URLInputStreamFactory(trainingFileUrl);
			ObjectStream<String> lineStream = new PlainTextByLineStream( inputStreamFactory , StandardCharsets.UTF_8);
				
			ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);
			TokenNameFinderModel trainedModel = NameFinderME.train("fr", "person", sampleStream, TrainingParameters.defaultParams(), tokenNameFinderFactory);
			
			// output trained file
			String frenchNERoutputFileName = "fr-ner-train-person.bin";
			Path frenchNERoutputFilePath = resourceDirectory.resolve(frenchNERoutputFileName);
			
			File modelOutputFile = frenchNERoutputFilePath.toFile();
			OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelOutputFile));
			trainedModel.serialize(modelOut);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("--- French NER 4 Person Model generator  - end ---");
	}
}
