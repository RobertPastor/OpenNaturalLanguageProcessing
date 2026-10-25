package mergedNERinputs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class MergeNERinputTextFiles {

	private static final Logger logger = Logger.getLogger(MergeNERinputTextFiles.class.getName());

	String[] inputTextFileNames = null;
	String mergedOutputTextFileName = "";
	Path frenchNERoutputFilePath = null;

	Path resourceDirectoryPath = null;

	public MergeNERinputTextFiles(final String resourceDirectoryString , final String[] inputTextFileNames ,
			final String mergedOutputTextFileName ) {

		this.inputTextFileNames = inputTextFileNames;
		this.mergedOutputTextFileName = mergedOutputTextFileName;

		this.resourceDirectoryPath = Paths.get("src","test","resources", resourceDirectoryString);
		logger.info( this.resourceDirectoryPath.toString() );

		this.frenchNERoutputFilePath = this.resourceDirectoryPath.resolve(mergedOutputTextFileName);

	}

	public void MergeTextFiles ( ) throws IOException {

		File outputFile = this.frenchNERoutputFilePath.toFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

		for (String sourceFileName : this.inputTextFileNames) {
			Path sourceFilePath = this.resourceDirectoryPath.resolve(sourceFileName);
			logger.info(sourceFilePath.toString());

			Files.lines(sourceFilePath).forEach(line -> {
				try {
					writer.write(line);
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		writer.close();
	}

}
