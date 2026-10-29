## run model training from a merged input NER files

 mvn -Dtest=TestNameEntityFinderPersonModelTraining test
 
## inputs are taken from
 
String frenchNERinputFileName = "outputNERmergedTextFile.txt";
String resourceDirectoryString = "mergedNERinputs";
			
Path resourceDirectory = Paths.get("src","test","resources",resourceDirectoryString);


## output models 
are generated in the same output folder  
Path resourceDirectory = Paths.get("src","test","resources", "mergedNERinputs");
