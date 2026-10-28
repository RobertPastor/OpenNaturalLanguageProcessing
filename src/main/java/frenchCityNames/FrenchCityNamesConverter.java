package frenchCityNames;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

public class FrenchCityNamesConverter {


	private static final Logger logger = Logger.getLogger(FrenchCityNamesConverter.class.getName());

	private String CsvDelimiter = ",";
	File inputFile = null;
	Path resourceDirectoryPath = null;
	public final static char CR = (char) 0x0D;
	public final static char LF = (char) 0x0A;

	/**
	 * generate NER <START:location .... <END> tags
	 */
	public FrenchCityNamesConverter( final Path resourceDirectoryPath, final String sourceDataFileName ) {

		this.resourceDirectoryPath = resourceDirectoryPath;
		
		// input file name		
		Path frenchNERoutputFilePath = this.resourceDirectoryPath.resolve(sourceDataFileName);
		this.inputFile = frenchNERoutputFilePath.toFile();
		
		logger.info("French City names converter -> input file path -> " + this.inputFile.getPath());
	}

	/**
	 * input format : 
	 * Zuydcoote,59,Nord,Hauts-de-France
	 * @return
	 */
	private String generateSentence( final String ville,  final String codeDepartementString , 
			final String nomDuDepartement , final String region) {

		StringBuilder sb = new StringBuilder();

		// output pattern
		// La ville de <START:location> ville <END> dans le département "codeDepartement" de nom "département" appartient à la région <START:location> région <END
		sb.append("La ville de <START:location> ");
		if ( ville.length() > 0 ) {
			sb.append( ville );
		} else {
			sb.append("nom de ville vide");
		}
		sb.append(" <END> ");
		sb.append(" dans le département");
		
		int codeDepartementInt = 0;
		try {
			codeDepartementInt = Integer.parseInt(codeDepartementString);
		} catch (NumberFormatException nfe) {
			codeDepartementInt = 0;
	    }
		// 28th October 2026 - decision not to encode a integer departement code into a location
		if ( codeDepartementInt > 0 ) {
			sb.append(" ");
			sb.append(codeDepartementString);
			sb.append(" ");
		} 
		
		sb.append(" de nom ");
		if ( nomDuDepartement.length() > 0 ) {
			sb.append(" <START:location> ");
			sb.append( nomDuDepartement );
			sb.append(" <END> ");
		}
		sb.append(" appartient à la région ");
		if ( region.length() > 0 ) {
			sb.append(" <START:location> ");
			sb.append( region );
			sb.append(" <END>");
		}
		sb.append(".");
		sb.append(CR);
		sb.append(LF);
		return sb.toString();
	}


	/**
	 * sexe;preusuel;annais;nombre
	 * @throws IOException 
	 */
	public void convertCsvData( final String outputFileName ) throws IOException {

		Path frenchFirstNamesNERoutputFilePath = this.resourceDirectoryPath.resolve(outputFileName);

		//File outputFile = frenchFirstNamesNERoutputFilePath.toFile();
		logger.info( frenchFirstNamesNERoutputFilePath.toFile().getAbsolutePath() );
		FileOutputStream fos = new FileOutputStream(frenchFirstNamesNERoutputFilePath.toFile().getAbsolutePath());

		try (BufferedReader br = new BufferedReader( new FileReader ( this.inputFile ))) {
			String line;
			// header line as 1st line does not contain valuable data
			boolean csvHeaderLine = true;
			while ((line = br.readLine()) != null) {
				logger.info( line );

				if ( csvHeaderLine ) {
					csvHeaderLine = false;
				} else {
					String[] values = line.split(CsvDelimiter);
					String resultingSentence = generateSentence(values[0] , values[1] , values[2] , values[3]);
					
					fos.write(resultingSentence.getBytes());
					fos.flush();
					logger.info( resultingSentence );
				}
			}
			fos.close();
			logger.info( frenchFirstNamesNERoutputFilePath.toFile().getAbsolutePath() );

		}

	}
}