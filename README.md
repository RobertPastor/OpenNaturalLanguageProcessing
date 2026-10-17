# OpenNaturalLanguageProcessing
Process French Natural Language to understand actions and emotions

## NER 
## use as input HuggingFace CATIE AQ French NER 4 Entities
https://huggingface.co/datasets/CATIE-AQ/frenchNER_4entities/tree/main/data

## Read parquet files
## convert ner tags into START END tags expected by NER training format

-> <START:person> Robert PASTOR <END>
-> <START:location> Issy les Moulineaux <END>
-> <START:organization> HOME <END>
-> <STRAT:misc> miscellaneous <END>

use the generated text file as input to the Apache Open NLP NER training
