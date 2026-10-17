package frenchNER4Entities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import com.jerolba.carpet.CarpetReader;

import frenchNER4Entities.FrenchNER4EntitiesDataSchema.FrenchNer4EntitiesDataRecord;

public class FrenchNER4EntitiesData extends FrenchNER4EntitiesDataTable {
	
	/**
	 * source https://huggingface.co/datasets/CATIE-AQ/frenchNER_4entities/tree/main/data
	 */

	String frenchNERinputFileName = "";
	Path frenchNERinputFilePath = null;
	
	public FrenchNER4EntitiesData( final String frenchNERinputFileName ) {
		this.frenchNERinputFileName = frenchNERinputFileName;
		
		Path resourceDirectory = Paths.get("src","test","resources","Catie-AQ-FR-NER-4Entities");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();
		System.out.println( absolutePath );
		
		this.frenchNERinputFilePath = resourceDirectory.resolve(this.frenchNERinputFileName);
		System.out.println( this.frenchNERinputFilePath.toFile().getAbsolutePath() );
	}
	
	/**
	 * read the flight list from a parquet file
	 * 27th December 2025 - filter on one aircraft type code
	 * @throws IOException
	 */
	public void readParquet( ) throws IOException {
		
		this.createEmptyNerDataTable();
		
		//String frenchNERinputFilePath = "/Catie-AQ-FE-NER-4Entities/train-00000-of-00001.parquet";
		//InputStream is = getClass().getResourceAsStream(this.frenchNERinputFilePath);
		
		File file =  this.frenchNERinputFilePath.toFile();
		
		CarpetReader<?> carpetReader = null;
		try {
			
			carpetReader = new CarpetReader<>(file, FrenchNer4EntitiesDataRecord.class );
			@SuppressWarnings("unchecked")
			Iterator<FrenchNer4EntitiesDataRecord> iterator = ((CarpetReader<FrenchNer4EntitiesDataRecord>) carpetReader).iterator();

			int count = 0;
			while (iterator.hasNext() && ( count < 100) ) {
				count = count + 1;
				//FrenchNer4EntitiesDataRecord record = iterator.next();
				System.out.println(" ---------------- " + String.valueOf(count) + " ------------ " );

				//System.out.println(record.tokens());
				//System.out.println(record.ner_tags());
				//System.out.println(record.tokens());
				
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		
	}
}
