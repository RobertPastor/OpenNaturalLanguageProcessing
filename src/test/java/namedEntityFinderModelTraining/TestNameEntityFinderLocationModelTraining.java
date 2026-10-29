package namedEntityFinderModelTraining;

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
import opennlp.tools.util.model.ModelType;

public class TestNameEntityFinderLocationModelTraining {

	
private static final Logger logger = Logger.getLogger(TestNameEntityFinderLocationModelTraining.class.getName());
	
	@Test
	public void generateFrenchNerLocationModel () throws IOException {
		
		logger.info("--- French NER 4 Person Model generator  - start ---");
		
		String modelName = "location";
		try {
			TokenNameFinderFactory tokenNameFinderFactory = TokenNameFinderFactory.create(null, null, 
					Collections.emptyMap(), 
					new BioCodec());
			
			// input training file with START END xml like tags
			String frenchNERinputFileName = "outputNERmergedTextFile.txt";
			String resourceDirectoryString = "mergedNERinputs";
			
			Path resourceDirectory = Paths.get("src","test","resources", resourceDirectoryString);
			Path frenchNERinputFilePath = resourceDirectory.resolve(frenchNERinputFileName);
			
			File trainingFile = frenchNERinputFilePath.toFile();
			URL trainingFileUrl = trainingFile.toURI().toURL();
			logger.info( trainingFileUrl.toExternalForm() );
			
			URLInputStreamFactory inputStreamFactory = new URLInputStreamFactory(trainingFileUrl);
			ObjectStream<String> lineStream = new PlainTextByLineStream( inputStreamFactory , StandardCharsets.UTF_8);
			
			// training parameters
			TrainingParameters trainingParameters = new TrainingParameters();
			trainingParameters.put(TrainingParameters.ITERATIONS_PARAM, 100);// number of training iterations
			trainingParameters.put(TrainingParameters.CUTOFF_PARAM, 3);//minimal number of times a feature must be seen
			trainingParameters.put(TrainingParameters.ALGORITHM_PARAM, ModelType.MAXENT.toString());
				
			//output model
			ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);
			TokenNameFinderModel trainedModel = NameFinderME.train("fr", modelName , sampleStream, 
					trainingParameters , tokenNameFinderFactory);
			
			// output trained file
			String frenchNERoutputFileName = "fr-ner-train-" + modelName + ".bin";
			Path frenchNERoutputFilePath = resourceDirectory.resolve(frenchNERoutputFileName);
			
			// serialize the model file
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
