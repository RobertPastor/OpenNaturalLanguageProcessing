package frenchNER4Entities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.logging.Logger;

import com.jerolba.carpet.CarpetReader;

import frenchNER4Entities.FrenchNER4EntitiesDataSchema.FrenchNer4EntitiesDataRecord;

public class FrenchNER4EntitiesData extends FrenchNER4EntitiesDataTable {
	
	private static final Logger logger = Logger.getLogger(FrenchNER4EntitiesData.class.getName());

	
	/**
	 * source https://huggingface.co/datasets/CATIE-AQ/frenchNER_4entities/tree/main/data
	 */

	String frenchNERinputFileName = "";
	String frenchNERoutputFileName = "";
	Path frenchNERinputFilePath = null;
	Path frenchNERoutputFilePath = null;
	FileOutputStream fos = null;
	
	public final static char CR = (char) 0x0D;
	public final static char LF = (char) 0x0A;
	
	public FrenchNER4EntitiesData( final String frenchNERinputFileName , final String frenchNERoutputFileName) 
			throws FileNotFoundException {
		this.frenchNERinputFileName = frenchNERinputFileName;
		this.frenchNERoutputFileName = frenchNERoutputFileName;
		
		Path resourceDirectory = Paths.get("src","test","resources","Catie-AQ-FR-NER-4Entities");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();
		System.out.println( absolutePath );
		
		this.frenchNERinputFilePath = resourceDirectory.resolve(this.frenchNERinputFileName);
		System.out.println( this.frenchNERinputFilePath.toFile().getAbsolutePath() );
		
		this.frenchNERoutputFilePath = resourceDirectory.resolve(this.frenchNERoutputFileName);
		this.fos = new FileOutputStream(this.frenchNERoutputFilePath.toFile().getAbsolutePath());
		
	}
	
	/**
	 * read the data from a parquet file
	 * @throws IOException
	 */
	public void readParquet( ) throws IOException {
		
		logger.info("--- read parquet  - start ---");
		
		this.createEmptyNerDataTable();
		
		//String frenchNERinputFilePath = "/Catie-AQ-FE-NER-4Entities/train-00000-of-00001.parquet";
		//InputStream is = getClass().getResourceAsStream(this.frenchNERinputFilePath);
		
		File file =  this.frenchNERinputFilePath.toFile();
		
		CarpetReader<FrenchNer4EntitiesDataRecord> carpetReader = null;
		try {
			AnnotatedTaggedNerEntities annotatedTaggedNerEntities = new AnnotatedTaggedNerEntities();
			
			carpetReader = new CarpetReader<>(file, FrenchNer4EntitiesDataRecord.class );
			Iterator<FrenchNer4EntitiesDataRecord> iterator = ((CarpetReader<FrenchNer4EntitiesDataRecord>) carpetReader).iterator();

			int count = 0;
			while (iterator.hasNext() && ( count < 1000000000 ) ) {
				count = count + 1;
				FrenchNer4EntitiesDataRecord record = iterator.next();
				System.out.println(" ---------------- " + String.valueOf(count) + " ------------ " );

				String[] inputSentenceTokens = record.tokens().toArray(new String[record.tokens().size()]);
				Integer[] inputSentenceNerTagsValues = record.ner_tags().toArray(new Integer[record.ner_tags().size()]);
				
				String[] annotatedSentence = annotatedTaggedNerEntities.annotateOpenNlpSentence(inputSentenceTokens , inputSentenceNerTagsValues );
				logger.info( String.join( " ", annotatedSentence) );
				String annotatedSentenceAsString = String.join( " ", annotatedSentence) + CR + LF;
				// replace ' followed by SPACE
				annotatedSentenceAsString = annotatedSentenceAsString.replaceAll("' ","'");
				annotatedSentenceAsString = annotatedSentenceAsString.replaceAll(" ,",",");
				annotatedSentenceAsString = annotatedSentenceAsString.replaceAll(" .",".");
				annotatedSentenceAsString = annotatedSentenceAsString.replaceAll("  "," ");
				byte[] byteArray = annotatedSentenceAsString.getBytes();
				this.fos.write(byteArray);
				this.fos.flush();
				System.out.println(" ---------------- " + String.valueOf(count) + " ------------ " );

				//System.out.println(record.tokens());
				//System.out.println(record.ner_tags());
				//System.out.println(record.tokens());
			}
			this.fos.flush();
			
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		
	}
}
