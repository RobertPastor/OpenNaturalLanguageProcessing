# Open Natural Language Processing
Purpose : Process French Natural Language to understand actions and emotions

## Warning 
All maven tests are run with some exclude as defined at the bottom of the pom.xml

## inputs for the Named Entity Recognition 
1) Antilles Conllu
2) French First Names as obtained from the french government database
3) CATIE AQ french NER 4 Entities




## Named Entities Recognition 
## use as input HuggingFace CATIE AQ French NER 4 Entities contained in parquet files
https://huggingface.co/datasets/CATIE-AQ/frenchNER_4entities/tree/main/data

## Read parquet files obtained from HuggingFace
## convert NER tags into START END tags expected by Apache NLP NER training format

-> START:person> Robert PASTOR END>  
-> START:location> Issy les Moulineaux END>  
-> START:organization> HOME END>  
-> STRAT:misc> miscellaneous END>  

use the generated text file as input to the Apache Open NLP NER training
generated text file -> train-NER4Entities-input.txt

## train a french person model.
use TestNameEntityFinderModelTraining

## finally use the NER model as one of the step to analye a docx file such as Madame Bovary from Flaubert
use TestNamedEntityRecognition 
