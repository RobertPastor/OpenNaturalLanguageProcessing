package frenchAuthorsBooks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;


public class FrenchAuthorsBooksConverter {

	private static final Logger logger = Logger.getLogger(FrenchAuthorsBooksConverter.class.getName());
	
	private String CsvDelimiter = ",";
	File inputFile = null;
	Path resourceDirectoryPath = null;
	public final static char CR = (char) 0x0D;
	public final static char LF = (char) 0x0A;
	
	
	public FrenchAuthorsBooksConverter ( final Path resourceDirectoryPath, final String sourceDataFileName) {
		
		this.resourceDirectoryPath = resourceDirectoryPath;
		
		// input file name		
		Path frenchNERoutputFilePath = this.resourceDirectoryPath.resolve(sourceDataFileName);
		this.inputFile = frenchNERoutputFilePath.toFile();
		
		logger.info("French Authors names converter -> input file path -> " + this.inputFile.getPath());
	
	}

	private String generateSentence( final String author , final String GenderMaleFemale , final String books) {
		
		StringBuilder sb = new StringBuilder();

		// output pattern l'écrivain / l'autrice (masculin , féminin) dont le nom est .... a écrit l'ouvrages suivant
		if ( GenderMaleFemale.equalsIgnoreCase("M") ) {
			sb.append("L'auteur masculin ");
		} else {
			sb.append("L'autrice féminine ");
		}
		if ( author.trim().length() > 0 ) {
			sb.append("<START:person> " + author.trim() + " <END>");
		}
		String[] booksArray = books.split("/");
		if ( booksArray.length > 0 ) {
			sb.append(" a écrit l'ouvrage " );
			sb.append("<START:misc> " + booksArray[0].replace("\"", "") + " <END>");
		}
		
		sb.append(".");
		sb.append(CR);
		sb.append(LF);
		return sb.toString();
	}
	
	
	/**
	 * Autrice - auteur,Genre,Titres,Nombre de titres,Année,Programme,Niveau d'enseignement,Source,Thématique,Questionnement,Problématique
	 * Lucien de Samosate,M,Histoire véritable / Icaromenippe ou le voyage au-dessus des nuages,1,2016,cinquième,collège,http://cache.media.eduscol.education.fr/file/Regarder_le_monde/47/8/RA16_C4_FRA_3_imaginer_univers_nouveaux_corpus_555478.pdf,"Regarder le monde, inventer des mondes",Imaginer des univers nouveaux,Voyages lunaires
	 * Cyrano de Bergerac,M,Les Etats et empires de la lune,1,2016,cinquième,collège,http://cache.media.eduscol.education.fr/file/Regarder_le_monde/47/8/RA16_C4_FRA_3_imaginer_univers_nouveaux_corpus_555478.pdf,"Regarder le monde, inventer des mondes",Imaginer des univers nouveaux,Voyages lunaires
	 * Jules Verne,M,De la terre à la lune / Autour de la lune,1,2016,cinquième,collège,http://cache.media.eduscol.education.fr/file/Regarder_le_monde/47/8/RA16_C4_FRA_3_imaginer_univers_nouveaux_corpus_555478.pdf,"Regarder le monde, inventer des mondes",Imaginer des univers nouveaux,Voyages lunaires

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
					// skip 1st line as it contains the headers column names
					csvHeaderLine = false;
				} else {
					String[] values = line.split(CsvDelimiter);
					String resultingSentence = "";
					if ( values.length > 2 ) {
						resultingSentence = generateSentence(values[0] , values[1] , values[2] );
					}
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
