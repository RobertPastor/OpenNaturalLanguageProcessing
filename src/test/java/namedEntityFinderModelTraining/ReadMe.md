## run model running from a merged input NER files

 mvn -Dtest=TestNameEntityFinderPersonModelTraining test
 
 ## inputs are taken from
 
 String frenchNERinputFileName = "outputNERmergedTextFile.txt";
String resourceDirectoryString = "mergedNERinputs";
			
Path resourceDirectory = Paths.get("src","test","resources",resourceDirectoryString);
