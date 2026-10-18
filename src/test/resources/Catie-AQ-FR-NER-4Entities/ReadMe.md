## source
https://huggingface.co/datasets/CATIE-AQ/frenchNER_4entities/tree/main/data

## three parquet files as inputs

$ ls -al src/test/resources/Catie-AQ-FR-NER-4Entities/  
total 91381  
drwxr-xr-x 1 rober 197609        0 Oct 17 14:42 ./  
drwxr-xr-x 1 rober 197609        0 Oct 15 16:29 ../  
-rw-r--r-- 1 rober 197609      116 Oct 18 09:20 ReadMe.md  
-rw-r--r-- 1 rober 197609  3361643 Oct 15 16:31 test-00000-of-00001.parquet  
-rw-r--r-- 1 rober 197609 35625735 Oct 15 16:32 train-00000-of-00001.parquet  
-rw-r--r-- 1 rober 197609 52047475 Oct 17 20:54 train-NER4Entities-input.txt  
-rw-r--r-- 1 rober 197609  2525435 Oct 15 16:32 validation-00000-of-00001.parquet  

rober@RobertPastor MINGW64 ~/eclipse-2025-09/eclipse-jee-2025-09-R-win32-x86_64/eclipse-workspace/Apache-Open-NLP (master)  
$

## input format   
https://github.com/catie-aq  

https://huggingface.co/collections/CATIE-AQ/catie-french-ner-pack  

1) a String[] array containing the tokens  
2) an Integer[] array containing the integer value of each token


## processing test
FrenchNER4EntitiesData.java

## outputs
train-NER4Entities-input.txt  