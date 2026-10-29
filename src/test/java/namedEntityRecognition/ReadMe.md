## Purpose : analyse a set of sentences extracted from a WORD file

## Input
a Word docx document , whose sentences are extracted using Apache POI library

## Process step : cut document into sentences
## Process step : cut each sentence into tokens (including punctuation)
## Process step : use the NER model (build in the appropriate test package)

## launch the test 
mvn -Dtest=TestNamedEntityRecognitionPerson test  

mvn -Dtest=TestNamedEntityRecognitionLocation test

## output
list of Persons or Locations with the greater occurrences counts