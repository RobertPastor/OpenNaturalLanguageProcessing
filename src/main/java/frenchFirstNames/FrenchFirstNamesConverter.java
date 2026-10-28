package frenchFirstNames;

/**
 * create a french NER text files with the START:person containing the french First Name
 * List of french first names provided by the french governement see ReadMe
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class FrenchFirstNamesConverter {
	
	private static final Logger logger = Logger.getLogger(FrenchFirstNamesConverter.class.getName());

	private String CsvDelimiter = ";";
	File inputFile = null;
	Path resourceDirectory = Paths.get("src","test","resources","frenchFirstNames");
	
	public final static char CR = (char) 0x0D;
	public final static char LF = (char) 0x0A;

	public FrenchFirstNamesConverter( final String sourceDataFileName ) {

		// input file name		
		Path frenchNamesInputFilePath = resourceDirectory.resolve(sourceDataFileName);
		this.inputFile = frenchNamesInputFilePath.toFile();
	}
	
	/**
	 * generate a text line such as the following
	 * Pendant l'année <START:date> 2014 <END>, le prénom masculin <START:person> DAVY <END> fut attribué 33 fois.
	 * 
	 * @param MasculinFeminin
	 * @param firstName
	 * @param yearString
	 * @param countString
	 * @return
	 */
	private String generateSentence( final String MasculinFeminin,  final String firstName , 
			final String yearString , final String countString) {
		
		StringBuilder sb = new StringBuilder();
		int yearInt = 0;
		try {
			yearInt = Integer.parseInt(yearString);
		} catch (NumberFormatException nfe) {
			yearInt = 0;
	    }
		if ( yearInt > 0 ) {
			sb.append("Pendant l'année <START:date> ");
			sb.append(String.valueOf(yearInt));
			sb.append(" <END> ");
		} else {
			sb.append("Pendant l'année <START:date> 2026 <END> ");
		}
		sb.append(", le prénom ");
		if ( MasculinFeminin.equalsIgnoreCase("1") ) {
			sb.append("masculin");
		}else {
			sb.append("féminin");
		}
		if ( firstName.length() > 0 ) {
			sb.append(" <START:person> ");
			sb.append(firstName);
			sb.append(" <END> ");
		} else {
			sb.append(" <START:person> Robert <END> ");
		}
		try {
			yearInt = Integer.parseInt(yearString);
		} catch (NumberFormatException nfe) {
			yearInt = 0;
	    }
		int countInt = 0;
		try {
			countInt = Integer.parseInt(countString);
		} catch (NumberFormatException nfe) {
			countInt = 0;
	    }
		if ( countInt > 0 ) {
			sb.append("fut attribué ");
			sb.append(countString);
			sb.append(" fois.");
		} else {
			sb.append("ne fut jamais attribué.");
		}
		sb.append(CR);
		sb.append(LF);
		return sb.toString();
	}

	/**
	 * sexe;preusuel;annais;nombre
	 * @throws FileNotFoundException 
	 */
	public void readCsvData( final String outputFileName ) throws FileNotFoundException {
		
		Path frenchFirstNamesNERoutputFilePath = this.resourceDirectory.resolve(outputFileName);
		//File outputFile = frenchFirstNamesNERoutputFilePath.toFile();
		logger.info( frenchFirstNamesNERoutputFilePath.toFile().getAbsolutePath() );
		FileOutputStream fos = new FileOutputStream(frenchFirstNamesNERoutputFilePath.toFile().getAbsolutePath());
		
		try (BufferedReader br = new BufferedReader( new FileReader ( this.inputFile ))) {
			String line;
			boolean csvHeaderLine = true;
			while ((line = br.readLine()) != null) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
