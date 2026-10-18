#!/bin/bash
echo "Hello, Open NLP train a French NER model!"

#  -model modelFile ->                 output model file.
# fr for french model
# opennlp TokenNameFinderTrainer -model fr-ner-person.bin -lang fr -data train-NER4Entities-input.txt -encoding UTF-8